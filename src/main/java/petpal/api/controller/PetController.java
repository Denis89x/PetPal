package petpal.api.controller;

import com.backblaze.b2.client.exceptions.B2Exception;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import petpal.api.dto.AccountDTO;
import petpal.api.dto.PetDTO;
import petpal.security.AccountDetails;
import petpal.api.service.LinkServiceImp;
import petpal.api.service.PetServiceImp;
import petpal.api.service.PhotosServiceImp;
import petpal.store.model.Account;
import petpal.store.model.Pet;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PetController {

    PetServiceImp petServiceImp;
    LinkServiceImp linkServiceImp;
    PhotosServiceImp photosServiceImp;
    ModelMapper modelMapper;

    private static final String CREATE_PROFILE = "/pet/create";
    private static final String EDIT_PROFILE = "/pet/edit";

    private static final String UPLOAD_PICTURE = "/pet/upload-picture";

    @PostMapping(CREATE_PROFILE)
    public ResponseEntity<PetDTO> createProfile(@RequestBody @Valid PetDTO petDTO) throws AccountNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();

        HttpHeaders headers = new HttpHeaders();

        if (petDTO == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        petServiceImp.createProfile(petDTO, accountDetails.account().getAccountId());
        return new ResponseEntity<>(petDTO, headers, HttpStatus.CREATED);
    }

    @PatchMapping(EDIT_PROFILE)
    public ResponseEntity<String> editProfile(
            @RequestParam(value = "pet_id") Optional<Integer> optionalPetId,
            @RequestParam(value = "name", required = false) Optional<String> optionalName,
            @RequestParam(value = "breed", required = false) Optional<String> optionalBreed,
            @RequestParam(value = "age", required = false) Optional<Integer> optionalAge) {
        if (optionalPetId.isPresent()) {
            Integer petId = optionalPetId.get();

            Optional<Pet> optionalPet = petServiceImp.findById(petId);

            if (optionalPet.isPresent()) {
                Pet pet = optionalPet.get();

                optionalName.ifPresent(pet::setName);
                optionalBreed.ifPresent(pet::setBreed);
                optionalAge.ifPresent(pet::setAge);

                petServiceImp.save(pet);

                return ResponseEntity.status(HttpStatus.OK).body("Changes have been made");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pet must exist");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id shouldn`t be empty.");
        }
    }

    @PostMapping(UPLOAD_PICTURE)
    public ResponseEntity<String> uploadPicture(@RequestParam("file") MultipartFile file, @RequestParam("pet_id") Integer petId) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload.");
        }
        try {
            Optional<Pet> petOptional = petServiceImp.findById(petId);
            if (petOptional.isPresent()) {
                Pet pet = petOptional.get();
                photosServiceImp.save(pet, linkServiceImp.uploadProfilePicture(file));
                return ResponseEntity.ok("Image was successfully uploaded");
            } else {
                return ResponseEntity.badRequest().body("Incorrect pet_id.");
            }
        } catch (B2Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile picture.");
        }
    }
}

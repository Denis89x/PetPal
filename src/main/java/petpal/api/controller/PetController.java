package petpal.api.controller;

import com.backblaze.b2.client.exceptions.B2Exception;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import petpal.api.dto.PetDTO;
import petpal.security.AccountDetails;
import petpal.api.service.LinkServiceImp;
import petpal.api.service.PetServiceImp;
import petpal.api.service.PhotosServiceImp;
import petpal.store.model.Pet;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/pets")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PetController {

    PetServiceImp petServiceImp;
    LinkServiceImp linkServiceImp;
    PhotosServiceImp photosServiceImp;

    // CRUD
    private static final String FETCH_PROFILE = "/{pet_id}";
    private static final String EDIT_PROFILE = "/{pet_id}";
    private static final String DELETE_PROFILE = "/{pet_id}";

    private static final String UPLOAD_PICTURE = "/upload-picture";
    private static final String FETCH_WITH_PARAMETERS = "/find";

    @Transactional(readOnly = true)
    @GetMapping(FETCH_PROFILE)
    public ResponseEntity<PetDTO> fetchPet(@PathVariable("pet_id") Optional<Integer> optionalId) {
        if (optionalId.isPresent()) {
            int id = optionalId.get();
            Optional<Pet> optionalPet = petServiceImp.findById(id);
            if (optionalPet.isPresent()) {
                HttpHeaders headers = new HttpHeaders();

                Pet pet = optionalPet.get();
                return new ResponseEntity<>(petServiceImp.convertToPetDto(pet), headers, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional(readOnly = true)
    @GetMapping(FETCH_WITH_PARAMETERS)
    public List<PetDTO> fetchPets(
            @RequestParam(value = "name", required = false) Optional<String> optionalName,
            @RequestParam(value = "breed", required = false) Optional<String> optionalBreed,
            @RequestParam(value = "age", required = false) Optional<Integer> optionalAge) {

        Stream<Pet> petStream = petServiceImp.searchPets(
                optionalName.orElse(null),
                optionalBreed.orElse(null),
                optionalAge.orElse(null)
        );

        return petStream
                .map(petServiceImp::convertToPetDto)
                .toList();
    }

    @PostMapping()
    public ResponseEntity<PetDTO> createProfile(
            @RequestBody @Valid PetDTO petDTO,
            @RequestParam(value = "file", required = false) Optional<MultipartFile> file) throws AccountNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();

        HttpHeaders headers = new HttpHeaders();

        if (petDTO == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (file.isPresent()) {
            petServiceImp.createProfile(petDTO, accountDetails.account().getAccountId(), file);
        } else {
            petServiceImp.createProfile(petDTO, accountDetails.account().getAccountId(), Optional.empty());
        }

        return new ResponseEntity<>(petDTO, headers, HttpStatus.CREATED);
    }

    @PatchMapping(EDIT_PROFILE)
    public ResponseEntity<String> editProfile(@PathVariable("pet_id") Optional<Integer> optionalPetId,
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

    @DeleteMapping(DELETE_PROFILE)
    public ResponseEntity<String> deleteProfile(@PathVariable("pet_id") Optional<Integer> optionalPetId) {
        if (optionalPetId.isPresent()) {
            int petId = optionalPetId.get();
            Optional<Pet> optionalPet = petServiceImp.findById(petId);
            if (optionalPet.isPresent()) {
                petServiceImp.delete(petId);

                return ResponseEntity.ok("Pet profile was successfully deleted.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("A profile with that id does not exist.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The pet_id should not be empty");
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
                photosServiceImp.save(pet, linkServiceImp.uploadPicture(file));
                return ResponseEntity.ok("Image was successfully uploaded.");
            } else {
                return ResponseEntity.badRequest().body("Incorrect pet_id.");
            }
        } catch (B2Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile picture.");
        }
    }
}

package petpal.api.controller;

import com.backblaze.b2.client.exceptions.B2Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.Optional;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetServiceImp petServiceImp;
    private final LinkServiceImp linkServiceImp;
    private final PhotosServiceImp photosServiceImp;

    @Autowired
    public PetController(PetServiceImp petServiceImp, LinkServiceImp linkServiceImp, PhotosServiceImp photosServiceImp) {
        this.petServiceImp = petServiceImp;
        this.linkServiceImp = linkServiceImp;
        this.photosServiceImp = photosServiceImp;
    }

    @PostMapping("/create")
    public ResponseEntity<PetDTO> createProfile(@RequestBody @Valid PetDTO petDTO) throws AccountNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();

        HttpHeaders headers = new HttpHeaders();

        if (petDTO == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        petServiceImp.createProfile(petDTO, accountDetails.account().getAccountId());
        return new ResponseEntity<>(petDTO, headers, HttpStatus.CREATED);
    }

    @PostMapping("/upload-picture")
    public ResponseEntity<String> uploadPicture(@RequestParam("file") MultipartFile file, @RequestParam("pet_id") Integer petId) throws B2Exception {
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
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile picture.");
        }
    }
}

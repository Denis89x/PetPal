package petpal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import petpal.dto.PetDTO;
import petpal.service.PetServiceImp;

import javax.validation.Valid;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetServiceImp petServiceImp;

    @Autowired
    public PetController(PetServiceImp petServiceImp) {
        this.petServiceImp = petServiceImp;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetDTO> createProfile(@RequestBody @Valid PetDTO petDTO) {
        HttpHeaders headers = new HttpHeaders();

        if (petDTO == null)
            return new ResponseEntity<PetDTO>(HttpStatus.BAD_REQUEST);

        petServiceImp.createProfile(petDTO);
        return new ResponseEntity<>(petDTO, headers, HttpStatus.CREATED);
    }


}

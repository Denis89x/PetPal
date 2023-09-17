package petpal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import petpal.dto.PetDTO;
import petpal.security.AccountDetails;
import petpal.service.PetServiceImp;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetServiceImp petServiceImp;

    @Autowired
    public PetController(PetServiceImp petServiceImp) {
        this.petServiceImp = petServiceImp;
    }

    @PostMapping("/create")
    public ResponseEntity<PetDTO> createProfile(@RequestBody @Valid PetDTO petDTO) throws AccountNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();

        HttpHeaders headers = new HttpHeaders();

        if (petDTO == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        petServiceImp.createProfile(petDTO, accountDetails.getAccount().getAccountId());
        return new ResponseEntity<>(petDTO, headers, HttpStatus.CREATED);
    }
}

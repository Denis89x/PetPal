package petpal.api.service;

import petpal.api.dto.PetDTO;
import petpal.store.model.Pet;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

public interface PetService {
    void createProfile(PetDTO petDTO, Integer accountId) throws AccountNotFoundException;

    void save(Pet pet);

    void update(Optional<Pet> optionalPet, Optional<String> optionalName, Optional<String> optionalBreed, Optional<Integer> optionalAge);

}

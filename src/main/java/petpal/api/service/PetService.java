package petpal.api.service;

import org.springframework.web.multipart.MultipartFile;
import petpal.api.dto.PetDTO;
import petpal.store.model.Pet;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;
import java.util.stream.Stream;

public interface PetService {
/*    void createProfile(PetDTO petDTO, Integer accountId, MultipartFile file) throws AccountNotFoundException;*/

    void save(Pet pet);

    Stream<Pet> streamAllBy();

    Optional<Pet> findById(Integer id);

    void delete(Integer id);
}

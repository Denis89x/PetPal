package petpal.api.service;

import com.backblaze.b2.client.exceptions.B2Exception;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import petpal.api.dto.PetDTO;
import petpal.store.model.Account;
import petpal.store.model.Pet;
import petpal.store.repository.AccountRepository;
import petpal.store.repository.PetRepository;

import javax.persistence.criteria.Predicate;
import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PetServiceImp implements PetService {

    AccountRepository accountRepository;
    PetRepository petRepository;
    LinkServiceImp linkServiceImp;
    ModelMapper modelMapper;

    @Override
    public Optional<Pet> findById(Integer id) {
        return petRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        petRepository.deleteById(id);
    }

    public void createProfile(PetDTO petDTO, Integer accountId, Optional<MultipartFile> optionalMultipartFile) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            Pet pet = new Pet();
            if (optionalMultipartFile.isPresent()) {
                try {
                    pet.setPhotoUrl(linkServiceImp.uploadProfilePicture(optionalMultipartFile.get()));
                } catch (B2Exception e) {
                    throw new RuntimeException(e);
                }
            }
            pet.setName(petDTO.getName());
            pet.setAge(petDTO.getAge());
            pet.setBreed(petDTO.getBreed());
            pet.setAccount(account);
            petRepository.save(pet);
        } else {
            throw new AccountNotFoundException("Аккаунт не найден");
        }
    }

    @Override
    public Stream<Pet> streamAllBy() {
        return petRepository.streamAllBy();
    }

    @Override
    public void save(Pet pet) {
        petRepository.saveAndFlush(pet);
    }

    public PetDTO convertToPetDto(Pet pet) {
        return this.modelMapper.map(pet, PetDTO.class);
    }

    public Stream<Pet> searchPets(String name, String breed, Integer age) {
        return petRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null) {
                predicates.add(criteriaBuilder.equal(root.get("name"), name));
            }
            if (breed != null) {
                predicates.add(criteriaBuilder.equal(root.get("breed"), breed));
            }
            if (age != null) {
                predicates.add(criteriaBuilder.equal(root.get("age"), age));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }).stream();
    }
}

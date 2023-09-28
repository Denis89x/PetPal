package petpal.api.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import petpal.api.dto.PetDTO;
import petpal.store.model.Account;
import petpal.store.model.Pet;
import petpal.store.repository.AccountRepository;
import petpal.store.repository.PetRepository;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PetServiceImp implements PetService {

    AccountRepository accountRepository;
    PetRepository petRepository;
    ModelMapper modelMapper;

    @Override
    public Optional<Pet> findById(Integer id) {
        return petRepository.findById(id);
    }


    public void createProfile(PetDTO petDTO, Integer accountId) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            Pet pet = new Pet();
            pet.setName(petDTO.getName());
            pet.setAge(petDTO.getAge());
            pet.setBreed(petDTO.getBreed());
            pet.setPhotoUrl(petDTO.getPhotoUrl());
            pet.setAccount(account);
            petRepository.save(pet);
        } else {
            throw new AccountNotFoundException("Аккаунт не найден");
        }
    }

    @Override
    public void update(Optional<Pet> optionalPet,
                       Optional<String> optionalName,
                       Optional<String> optionalBreed,
                       Optional<Integer> optionalAge) {
        Pet pet = optionalPet.get();

        optionalName.ifPresent(pet::setName);
        optionalBreed.ifPresent(pet::setBreed);
        optionalAge.ifPresent(pet::setAge);

        save(pet);
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
}

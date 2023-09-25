package petpal.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petpal.api.dto.PetDTO;
import petpal.store.model.Account;
import petpal.store.model.Pet;
import petpal.store.repository.AccountRepository;
import petpal.store.repository.PetRepository;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

@Service
public class PetServiceImp implements PetService {

    private final AccountRepository accountRepository;
    private final PetRepository petRepository;

    @Autowired
    public PetServiceImp(AccountRepository accountRepository, PetRepository petRepository) {
        this.accountRepository = accountRepository;
        this.petRepository = petRepository;
    }

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
    public void save(Pet pet) {
        petRepository.saveAndFlush(pet);
    }
}

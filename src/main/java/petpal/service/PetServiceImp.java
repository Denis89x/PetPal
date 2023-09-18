package petpal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petpal.dto.PetDTO;
import petpal.model.Account;
import petpal.model.Pet;
import petpal.repository.AccountRepository;
import petpal.repository.PetRepository;

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
}

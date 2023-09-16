package petpal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petpal.dto.PetDTO;
import petpal.model.Account;
import petpal.model.Pet;
import petpal.repository.AccountRepository;
import petpal.repository.PetRepository;

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

    public void createProfile(PetDTO petDTO) {
        Pet pet = new Pet();
        pet.setName(petDTO.getName());
        pet.setAge(petDTO.getAge());
        pet.setBreed(petDTO.getBreed());
        pet.setPhotoUrl(petDTO.getPhotoUrl());
        Optional<Account> account = accountRepository.findById(petDTO.getAccountId());
        pet.setAccount(account.get());
        petRepository.save(pet);
    }
}

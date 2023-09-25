package petpal.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import petpal.security.AccountDetails;
import petpal.store.repository.AccountRepository;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/show")
    public String showAccountInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();

        return accountDetails.getUsername();
    }
}
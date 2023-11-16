package petpal.api.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import petpal.api.service.AccountServiceImp;
import petpal.security.AccountDetails;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/account")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AccountController {

    AccountServiceImp accountServiceImp;

    private static final String SHOW_ACCOUNT_INFO = "/show";
    private static final String CHANGE_ACCOUNT_INFO = "/change";

    @GetMapping(SHOW_ACCOUNT_INFO)
    public String showAccountInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();

        return accountDetails.getUsername();
    }

    @PostMapping(CHANGE_ACCOUNT_INFO)
    public ResponseEntity<String> changeAccountInfo(
            @RequestParam(value = "username", required = false) Optional<String> optionalUsername,
            @RequestParam(value = "current_password", required = false) Optional<String> optionalCurrentPassword,
            @RequestParam(value = "new_password", required = false) Optional<String> optionalNewPassword) {

        if (optionalCurrentPassword.isPresent() && optionalNewPassword.isPresent())
            accountServiceImp.changeAccountPassword(optionalCurrentPassword.get(), optionalNewPassword.get());

        optionalUsername.ifPresent(accountServiceImp::changeAccountUsername);

        return ResponseEntity.status(HttpStatus.OK).body("Account information changed successfully");
    }
}
package petpal.api.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import petpal.security.AccountDetails;
import petpal.store.model.Account;
import petpal.store.repository.AccountRepository;
import petpal.util.AccountNotFoundException;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class AccountServiceImp implements AccountService {

    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public void changeAccountPassword(String currentPassword, String newPassword) {
        if (passwordEncoder.matches(currentPassword, getPrincipal().getPassword())) {
            Account account = getAccountFromPrincipleUsername();
            account.setPassword(passwordEncoder.encode(newPassword));
            accountRepository.save(account);
        }
    }

    @Override
    public void changeAccountUsername(String username) {
        Account account = getAccountFromPrincipleUsername();
        account.setUsername(username);
        accountRepository.save(account);

        updateAuthenticationUsername(username);
    }

    @Override
    public void updateAuthenticationUsername(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
                username, authentication.getCredentials(), authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
    }

    @Override
    public AccountDetails getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (AccountDetails) authentication.getPrincipal();
    }

    @Override
    public Account getAccountFromPrincipleUsername() {
        return accountRepository.findByUsername(getPrincipal().getUsername())
                .orElseThrow(() -> new AccountNotFoundException("Account was not founded!"));
    }
}
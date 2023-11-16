package petpal.api.service;

import petpal.security.AccountDetails;
import petpal.store.model.Account;

public interface AccountService {

    void changeAccountPassword(String currentPassword, String newPassword);

    void changeAccountUsername(String username);

    void updateAuthenticationUsername(String username);

    AccountDetails getPrincipal();

    Account getAccountFromPrincipleUsername();
}

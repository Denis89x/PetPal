package petpal.api.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AccountDTO {

    @Size(min = 3, max = 20, message = "Username should be 3 - 20 symbols!")
    private String username;

    @Email(message = "Email should be correct!")
    private String email;

    private String password;
}
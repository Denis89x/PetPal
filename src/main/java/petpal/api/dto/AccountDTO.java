package petpal.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {

    private String username;
    private String email;
    private String password;
}
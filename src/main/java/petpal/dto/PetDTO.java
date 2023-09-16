package petpal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetDTO {
    private String name;
    private Integer accountId;
    private String breed;
    private Integer age;
    private String photoUrl;
}

package petpal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetDTO {
    private String name;
    private String breed;
    private Integer age;
    private String photoUrl;
}

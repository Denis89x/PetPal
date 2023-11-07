package petpal.api.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PetDTO {
    @NotEmpty(message = "Name shouldn`t be empty")
    @Size(min = 2, max = 20, message = "Name should be 2 - 20 symbols size")
    private String name;

    @NotEmpty(message = "Breed shouldn`t be empty")
    @Size(min = 3, max = 30, message = "Breed should be 3 - 30 symbols size")
    private String breed;

    @NotEmpty(message = "Age shouldn`t be empty")
    @Min(value = 1, message = "Age should be greater than 1")
    @Max(value = 25, message = "Age should be smaller than 20")
    private Integer age;
}

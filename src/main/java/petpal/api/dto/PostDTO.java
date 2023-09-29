package petpal.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import petpal.store.model.Photos;

import java.util.List;

@Getter
@Setter
@Builder
public class PostDTO {
    private String text;
}

package petpal.api.dto;

import lombok.*;
import petpal.store.model.Photos;
import petpal.store.model.PostPhoto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private String text;
    private List<PostPhotoDTO> postPhotos;
}

package petpal.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private String text;
    private List<FetchPostPhotoDTO> postPhotos;
}

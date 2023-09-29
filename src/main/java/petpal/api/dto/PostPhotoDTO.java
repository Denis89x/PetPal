package petpal.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostPhotoDTO {
    String photoUrl;

    String description;

    LocalDateTime uploadDate;
}

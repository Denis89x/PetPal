package petpal.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchPostPhotoDTO {
    String photoUrl;
    String description;
    Integer accountId;
    LocalDateTime uploadDate;
}

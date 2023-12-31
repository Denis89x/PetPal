package petpal.store.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_photo")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostPhoto {

    @Id
    @Column(name = "post_photo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer postPhotoId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "photo_url")
    String photoUrl;

    @Column(name = "description")
    String description;

    @Column(name = "upload_date")
    LocalDateTime uploadDate;
}
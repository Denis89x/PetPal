package petpal.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "photos")
@Getter
@Setter
@ToString
public class Photos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Integer photoId;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Column(name = "url")
    private String url;

    @Column(name = "description")
    private String description;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
}

package petpal.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "post")
@Getter
@Setter
@ToString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "text")
    private String text;

    @Column(name = "photo_url")
    private String photoUrl;
}

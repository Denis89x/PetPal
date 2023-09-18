package petpal.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pet")
@Getter
@Setter
@ToString
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Integer petId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "name")
    private String name;

    @Column(name = "breed")
    private String breed;

    @Column(name = "age")
    private Integer age;

    @Column(name = "photo_url")
    private String photoUrl;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<Photos> photos = new ArrayList<>();
}

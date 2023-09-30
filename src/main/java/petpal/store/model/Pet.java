package petpal.store.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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
    @NotEmpty(message = "Name shouldn`t be empty")
    @Size(min = 2,max = 16,message = "Name should be 2 - 16 symbols size")
    private String name;

    @Column(name = "breed")
    @NotEmpty(message = "Breed shouldn`t be empty")
    @Size(min = 2, max = 20, message = "Breed should be 2 - 16 symbols size")
    private String breed;

    @Column(name = "age")
    @Min(value = 1, message = "Age should be more then 1")
    private Integer age;

    @Column(name = "photo_url")
    private String photoUrl;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Photos> photos = new ArrayList<>();
}

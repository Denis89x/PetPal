package petpal.store.model;

import javax.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    Integer accountId;

    @Column(name = "username")
    String username;

    @Column(name = "email")
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "role")
    String role;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    List<Post> posts = new ArrayList<>();
}

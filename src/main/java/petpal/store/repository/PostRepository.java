package petpal.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petpal.store.model.Pet;
import petpal.store.model.Post;

import java.util.stream.Stream;


@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Stream<Post> streamAllBy();
}

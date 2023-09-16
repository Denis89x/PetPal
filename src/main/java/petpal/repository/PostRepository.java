package petpal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petpal.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
}

package petpal.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petpal.store.model.Post;
import petpal.store.model.PostPhoto;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostPhotoRepository extends JpaRepository<PostPhoto, Integer> {
    Optional<List<PostPhoto>> findAllByPost(Post post);
}

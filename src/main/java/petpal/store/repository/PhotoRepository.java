package petpal.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petpal.store.model.Photos;


@Repository
public interface PhotoRepository extends JpaRepository<Photos, Integer> {
}

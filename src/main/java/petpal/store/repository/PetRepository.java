package petpal.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petpal.store.model.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {

}

package petpal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petpal.model.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {

}

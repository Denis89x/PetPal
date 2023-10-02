package petpal.store.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petpal.store.model.Pet;

import java.util.List;
import java.util.stream.Stream;


@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {

    Stream<Pet> streamAllBy();

    List<Pet> findAll(Specification<Pet> specification);
}

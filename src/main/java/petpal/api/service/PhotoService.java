package petpal.api.service;
import petpal.store.model.Pet;
import petpal.store.model.Photos;

import java.util.Optional;

public interface PhotoService {
    Optional<Photos> findById(Integer id);
    void save(Pet pet, String profilePictureUrl);
}

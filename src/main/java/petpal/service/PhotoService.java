package petpal.service;

import petpal.model.Pet;
import petpal.model.Photos;

import java.util.Optional;

public interface PhotoService {
    Optional<Photos> findById(Integer id);
    void save(Pet pet, String profilePictureUrl);
}

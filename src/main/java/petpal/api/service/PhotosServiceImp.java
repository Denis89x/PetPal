package petpal.api.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petpal.store.model.Pet;
import petpal.store.model.Photos;
import petpal.store.repository.PhotoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class PhotosServiceImp implements PhotoService {

    PhotoRepository photoRepository;

    @Override
    public Optional<Photos> findById(Integer id) {
        return photoRepository.findById(id);
    }

    @Override
    public void save(Pet pet, String profilePictureUrl) {
        Photos photo = new Photos();
        photo.setUrl(profilePictureUrl);
        photo.setPet(pet);
        photo.setUploadDate(LocalDateTime.now());
        photoRepository.save(photo);
    }
}

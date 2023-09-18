package petpal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petpal.model.Pet;
import petpal.model.Photos;
import petpal.repository.PhotoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PhotosServiceImp implements PhotoService {

    private final PhotoRepository photoRepository;

    @Autowired
    public PhotosServiceImp(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

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

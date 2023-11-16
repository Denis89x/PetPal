package petpal.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import petpal.api.service.PhotosServiceImp;
import petpal.store.model.Pet;
import petpal.store.repository.PhotoRepository;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PhotosServiceTest {
/*    private PhotosServiceImp photoService;

    @Mock
    private PhotoRepository photoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        photoService = new PhotosServiceImp(photoRepository);
    }

    @Test
    public void shouldSave() {
        Pet pet = mock(Pet.class);

        String profilePictureUrl = "http://example.com/profile.jpg";

        photoService.save(pet, profilePictureUrl);

        verify(photoRepository).save(argThat(photo ->
                photo.getUrl().equals(profilePictureUrl) &&
                photo.getPet() == pet &&
                photo.getUploadDate() != null));
    }*/
}

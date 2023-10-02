package petpal.api.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import petpal.store.model.Post;
import petpal.store.model.PostPhoto;
import petpal.store.repository.PostPhotoRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PostPhotoService implements IPostPhotoService {

    PostPhotoRepository postPhotoRepository;

    @Override
    public Optional<List<PostPhoto>> findAllByPost(Post post) {
        return postPhotoRepository.findAllByPost(post);
    }

    @Override
    public void deleteAll(List<PostPhoto> photos) {
        postPhotoRepository.deleteAll(photos);
    }
}

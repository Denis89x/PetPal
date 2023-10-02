package petpal.api.service;

import petpal.store.model.Post;
import petpal.store.model.PostPhoto;

import java.util.List;
import java.util.Optional;

public interface IPostPhotoService {
    Optional<List<PostPhoto>> findAllByPost(Post post);

    void deleteAll(List<PostPhoto> photos);
}

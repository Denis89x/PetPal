package petpal.api.service;

import petpal.store.model.Post;

import java.util.Optional;

public interface IPostService {
    Optional<Post> findById(Integer id);
}

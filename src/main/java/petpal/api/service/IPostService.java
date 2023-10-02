package petpal.api.service;

import org.springframework.web.multipart.MultipartFile;
import petpal.api.dto.PostDTO;
import petpal.api.dto.PostPhotoDTO;
import petpal.store.model.Account;
import petpal.store.model.Post;
import petpal.store.model.PostPhoto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface IPostService {
    Optional<Post> findById(Integer id);

    PostDTO convertToPostDto(Post post);

    void addPhotosToPost(Integer postId, List<MultipartFile> files);

    void deletePhotosFromPost(Integer postId, List<Integer> idList);

    void saveText(Post post, String text);

    Account getAccount();

    void savePostPhoto(Post post, String profilePictureUrl);

    void deleteById(Integer id);

    Stream<Post> streamAllBy();

    PostPhotoDTO convertToPostPhotoDTO(PostPhoto postPhoto);

    PostPhoto convertToPostPhoto(PostPhotoDTO postPhotoDTO);
}

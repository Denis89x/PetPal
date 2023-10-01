package petpal.api.service;

import com.backblaze.b2.client.exceptions.B2Exception;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import petpal.api.dto.PetDTO;
import petpal.api.dto.PostDTO;
import petpal.api.dto.FetchPostPhotoDTO;
import petpal.api.dto.PostPhotoDTO;
import petpal.security.AccountDetails;
import petpal.store.model.*;
import petpal.store.repository.AccountRepository;
import petpal.store.repository.PostPhotoRepository;
import petpal.store.repository.PostRepository;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PostService implements IPostService {

    PostRepository postRepository;
    AccountRepository accountRepository;
    LinkServiceImp linkServiceImp;
    ModelMapper modelMapper;
    private final PostPhotoRepository postPhotoRepository;

    @Override
    public Optional<Post> findById(Integer id) {
        return postRepository.findById(id);
    }

    public PostDTO convertToPostDto(Post post) {
        PostDTO postDto = new PostDTO();
        postDto.setText(post.getText());

        List<FetchPostPhotoDTO> postPhotoDtos = post.getPostPhotos().stream()
                .map(postPhoto -> {
                    FetchPostPhotoDTO postPhotoDto = new FetchPostPhotoDTO();
                    postPhotoDto.setPhotoUrl(postPhoto.getPhotoUrl());
                    postPhotoDto.setDescription(postPhoto.getDescription());
                    postPhotoDto.setAccountId(postPhoto.getPost().getAccount().getAccountId());
                    postPhotoDto.setUploadDate(postPhoto.getUploadDate());
                    return postPhotoDto;
                })
                .collect(Collectors.toList());

        postDto.setPostPhotos(postPhotoDtos);

        return postDto;
    }


    public void createPost(String text, Integer accountId, Optional<List<MultipartFile>> listOfFiles) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            Post post = new Post();
            post.setText(text);
            post.setAccount(account);
            postRepository.saveAndFlush(post);
            if (listOfFiles.isPresent()) {
                List<MultipartFile> lists = listOfFiles.get();
                if (lists.size() <= 5) {
                    for (MultipartFile file : lists) {
                        try {
                            savePostPhoto(post, linkServiceImp.uploadProfilePicture(file));
                        } catch (B2Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        } else {
            throw new AccountNotFoundException("Account not found");
        }
    }

    public void addPhotosToPost(Integer postId, List<MultipartFile> files) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            for (MultipartFile file : files) {
                PostPhotoDTO postPhotoDTO = new PostPhotoDTO();
                try {
                    postPhotoDTO.setPhotoUrl(linkServiceImp.uploadProfilePicture(file));
                } catch (B2Exception e) {
                    throw new RuntimeException(e);
                }
                postPhotoDTO.setUploadDate(LocalDateTime.now());
                postPhotoDTO.setPostId(optionalPost.get().getPostId());
                postPhotoRepository.saveAndFlush(convertToPostPhoto(postPhotoDTO));
            }
        }
    }

    public void deletePhotosFromPost(Integer postId, List<Integer> idList) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            for (Integer id : idList) {
                if (postPhotoRepository.findById(id).isPresent()) {
                    postPhotoRepository.deleteById(id);
                }
            }
        }
    }

    public void saveText(Post post, String text) {
        post.setText(text);
        postRepository.saveAndFlush(post);
    }

    public Account getAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();
        return accountDetails.account();
    }

    public void savePostPhoto(Post post, String profilePictureUrl) {
        PostPhoto postPhoto = new PostPhoto();
        postPhoto.setPhotoUrl(profilePictureUrl);
        postPhoto.setPost(post);
        postPhoto.setUploadDate(LocalDateTime.now());
        postPhotoRepository.saveAndFlush(postPhoto);
    }

    public void deleteById(Integer id) {
        postRepository.deleteById(id);
    }

    public Stream<Post> streamAllBy() {
        return postRepository.streamAllBy();
    }

    public PostPhotoDTO convertToPostPhotoDTO(PostPhoto postPhoto) {
        return this.modelMapper.map(postPhoto, PostPhotoDTO.class);
    }

    public PostPhoto convertToPostPhoto(PostPhotoDTO postPhotoDTO) {
        return this.modelMapper.map(postPhotoDTO, PostPhoto.class);
    }
}

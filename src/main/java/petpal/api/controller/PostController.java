package petpal.api.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import petpal.api.dto.PostDTO;
import petpal.api.service.PostPhotoService;
import petpal.api.service.PostService;
import petpal.store.model.Account;
import petpal.store.model.Post;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

    PostService postService;
    PostPhotoService postPhotoService;

    private static final String FETCH_ALL_POST = "/posts";
    private static final String FETCH_POST = "/{post_id}";
    private static final String CREATE_POST = "/create";
    private static final String EDIT_POST = "/edit/{post_id}";
    private static final String DELETE_POST = "/delete/{post_id}";

    @Transactional(readOnly = true)
    @GetMapping(FETCH_POST)
    public ResponseEntity<PostDTO> fetchPost(@PathVariable("post_id") Optional<Integer> optionalPostId) {
        if (optionalPostId.isPresent()) {
            int id = optionalPostId.get();
            Optional<Post> optionalPost = postService.findById(id);
            if (optionalPost.isPresent()) {
                HttpHeaders headers = new HttpHeaders();

                Post post = optionalPost.get();
                return new ResponseEntity<>(postService.convertToPostDto(post), headers, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional(readOnly = true)
    @GetMapping(FETCH_ALL_POST)
    public List<PostDTO> fetchAllPosts() {
        Stream<Post> postStream = postService.streamAllBy();

        return postStream
                .map(postService::convertToPostDto)
                .toList();
    }

    @PostMapping(CREATE_POST)
    public ResponseEntity<String> createPost(
            @RequestParam(value = "text") Optional<String> optionalText,
            @RequestPart(value = "files", required = false) Optional<List<MultipartFile>> files) throws AccountNotFoundException {
        Account account = postService.getAccount();

        if (account != null && optionalText.isPresent()) {
            String text = optionalText.get();

            HttpHeaders headers = new HttpHeaders();

            if (files.isPresent()) {
                postService.createPost(text, account.getAccountId(), files);
            } else {
                postService.createPost(text, account.getAccountId(), Optional.empty());
            }

            return new ResponseEntity<>("Post was successfully added.", headers, HttpStatus.CREATED);
        }
       
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping(EDIT_POST)
    public ResponseEntity<String> editPost(
            @PathVariable("post_id") Optional<Integer> optionalPostId,
            @RequestParam(value = "text", required = false) Optional<String> optionalText,
            @RequestPart(value = "files", required = false) Optional<List<MultipartFile>> optionalFiles,
            @RequestParam(value = "deletePhotos", required = false) Optional<List<Integer>> optionalDeletePhotoIds) {
        if (optionalPostId.isPresent()) {
            Optional<Post> optionalPost = postService.findById(optionalPostId.get());
            if (optionalPost.isPresent()) {
                Post post = optionalPost.get();
                optionalText.ifPresent(s -> postService.saveText(post, s));
                optionalDeletePhotoIds.ifPresent(integers -> postService.deletePhotosFromPost(post.getPostId(), integers));
                optionalFiles.ifPresent(multipartFiles -> postService.addPhotosToPost(post.getPostId(), multipartFiles));
                return new ResponseEntity<>("Post was successfully edited.", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Post not found.", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(DELETE_POST)
    public ResponseEntity<String> deletePost(@PathVariable("post_id") Optional<Integer> optionalPostId) {
        return optionalPostId.flatMap(postService::findById)
                .map(post -> {
                    postPhotoService.findAllByPost(post).ifPresent(postPhotoService::deleteAll);
                    postService.deleteById(post.getPostId());
                    return ResponseEntity.ok("Post was successfully deleted.");
                })
                .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The post_id should not be empty"));
    }
}

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
import petpal.api.service.PostService;
import petpal.store.model.Account;
import petpal.store.model.Post;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequestMapping("/api")
public class PostController {

    PostService postService;

    private static final String FETCH_ALL_POST = "/posts";
    private static final String FETCH_POST = "/post/{post_id}";
    private static final String CREATE_POST = "/post/create";
    private static final String EDIT_POST = "/post/edit/{post_id}";
    private static final String DELETE_POST = "/post/delete/{post_id}";

    private static final String UPLOAD_PICTURE = "/post/upload-picture";

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
}

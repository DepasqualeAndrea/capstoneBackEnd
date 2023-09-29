package BackEnd.CapstoneProject.Post;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import BackEnd.CapstoneProject.User.User;
import BackEnd.CapstoneProject.User.UserService;

@CrossOrigin
@RestController
@RequestMapping("/user/post")
public class PostController {
	@Autowired
	private PostService postService;
	@Autowired
	private UserService userService;
	@Autowired
	private PostRepository postRepo;

	@GetMapping("/home")
	public Page<Post> getPostsForHomePage() {
		int page = 0;
		int size = 10;

		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "datacreazione"));

		return postRepo.findAll(pageRequest);
	}

	@GetMapping("/{postId}")
	public Post findPostsById(@PathVariable UUID postId) {
		return postService.findById(postId);

	}

	@PostMapping("/save")
	public Post creaPost(@RequestParam("image") MultipartFile image, @ModelAttribute Post body) throws IOException {
		return postService.savePostWithImages(body, image);
	}

	@PostMapping("/{postId}/togglelike")
	public ResponseEntity<String> likeOrUnlikePost(@PathVariable UUID postId) {
		User currentUser = userService.getCurrentUser();
		UUID userId = currentUser.getUserId();

		postService.toggleLike(postId, userId);

		String responseMessage;

		if (postService.isUserLikedPost(postId, userId)) {
			responseMessage = "Hai messo like!";
		} else {
			responseMessage = "Hai rimosso like!";
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
	}

	@PutMapping("/{postId}")
	public Post updatePosts(@PathVariable UUID postId, @RequestBody PostPayload body) {
		return postService.findByIdAndUpdate(postId, body);
	}

	@DeleteMapping("/{postId}")
	// @PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deletePost(@PathVariable UUID postId) {
		postService.deletePost(postId);
		return ResponseEntity.ok("Post eliminato con successo.");
	}
}

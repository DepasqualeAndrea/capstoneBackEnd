package BackEnd.CapstoneProject.Post;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import BackEnd.CapstoneProject.User.UserRepo;
import BackEnd.CapstoneProject.User.UserService;
import BackEnd.CapstoneProject.dbimage.ImageData;
import BackEnd.CapstoneProject.dbimage.StorageRepo;

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
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private StorageRepo imageRepository;

	@GetMapping
	public Page<Post> getAllPosts(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "userId") String sortBy) {
		return postService.find(page, size, sortBy);
	}

	@GetMapping("/{postId}")
	public Post findPostsById(@PathVariable UUID postId) {
		return postService.findById(postId);

	}

	@PostMapping("/save")
	public Post creaPost(@RequestParam("image") MultipartFile image, @ModelAttribute Post body) throws IOException {

		if (image.isEmpty()) {
			throw new IllegalArgumentException("Le immagini non sono state fornite.");
		}

		Post post = new Post();
		post.setUserId(userService.getCurrentUser().getUserId());
		post.setDatacreazione(LocalDateTime.now());
		post.setDescription(body.getDescription());
		post.setImageUrl(body.getImageUrl());
		User user = userService.getCurrentUser();
		user.getPost().add(post);
		post = postRepo.save(post);
		userRepo.save(user);

		byte[] imageBytes = image.getBytes();
		String name = image.getName();
		String type = image.getContentType();
		ImageData imageData = new ImageData();
		imageData.setName(name);
		imageData.setType(type);
		imageData.setImageData(imageBytes);
		imageData.setPost(post);

		imageRepository.save(imageData);
		post.setImagedata(imageData);

		return postService.savePostWithImages(post, imageData);
	}

	@PutMapping("/{postId}")
	// @PreAuthorize("hasAuthority('ADMIN')")
	public Post updatePosts(@PathVariable UUID postId, @RequestBody PostPayload body) {
		return postService.findByIdAndUpdate(postId, body);
	}

	@DeleteMapping("/{postId}")
	// @PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deletePost(@PathVariable UUID postId) {
		postService.findByIdAndDelete(postId);
		return ResponseEntity.ok("Utente eliminato con successo.");
	}
}

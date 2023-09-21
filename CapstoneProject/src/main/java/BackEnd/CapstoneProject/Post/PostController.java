package BackEnd.CapstoneProject.Post;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

import BackEnd.CapstoneProject.User.UserRepo;
import BackEnd.CapstoneProject.User.UserService;
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

	@GetMapping("/home")
	public Page<Post> getPostsForHomePage() {
		// Imposta il numero di pagina (0 per la prima pagina) e la dimensione della
		// pagina desiderata
		int page = 0;
		int size = 10; // Sostituisci con la dimensione desiderata

		// Crea un oggetto PageRequest per specificare l'ordinamento per la data di
		// creazione
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "datacreazione"));

		// Recupera i post ordinati per data di creazione
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

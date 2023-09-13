package BackEnd.CapstoneProject.Post;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import BackEnd.CapstoneProject.Cloudinary.CloudinaryService;

@CrossOrigin
@RestController
@RequestMapping("/user/post")
public class PostController {
	@Autowired
	PostService postService;

	@Autowired
	CloudinaryService cloudService;

	@GetMapping
	public Page<Post> getUtenti(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "userId") String sortBy) {
		return postService.find(page, size, sortBy);
	}

	@GetMapping("/{postId}")
	public Post findUtentiById(@PathVariable UUID postId) {
		return postService.findById(postId);

	}

	@PostMapping("/save")
	public ResponseEntity<Post> createPost(@RequestParam("imageFile") MultipartFile imageFile,
			@ModelAttribute PostPayload body) {
		// Carica l'immagine su Cloudinary e ottieni l'URL dell'immagine
		String imageUrl = cloudService.uploadImageToCloudinary(imageFile);

		if (imageUrl == null) {
			// Gestisci l'errore se il caricamento dell'immagine fallisce
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		Post newPost = new Post();
		newPost.setImageUrl(imageUrl);

		// Salva il post nel tuo servizio (usando il PostService)
		Post savedPost = postService.creaPost(newPost);

		return ResponseEntity.ok(savedPost);
	}

	@PutMapping("/{postId}")
	// @PreAuthorize("hasAuthority('ADMIN')")
	public Post updateUtenti(@PathVariable UUID postId, @RequestBody PostPayload body) {
		return postService.findByIdAndUpdate(postId, body);
	}

	@DeleteMapping("/{postId}")
	// @PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deletePost(@PathVariable UUID postId) {
		postService.findByIdAndDelete(postId);
		return ResponseEntity.ok("Utente eliminato con successo.");
	}
}

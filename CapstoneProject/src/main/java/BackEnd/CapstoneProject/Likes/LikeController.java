package BackEnd.CapstoneProject.Likes;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/post/like")
public class LikeController {

	private final LikeService likeService;

	@Autowired
	public LikeController(LikeService likeService) {
		this.likeService = likeService;
	}

	@PostMapping("/add")
	public ResponseEntity<String> addLike(@RequestBody Like like) {
		Like addedLike = likeService.addLike(like);
		return new ResponseEntity<>("Like added with ID: " + addedLike.getLikeId(), HttpStatus.CREATED);
	}

	@DeleteMapping("/remove/{likeId}")
	public ResponseEntity<String> removeLike(@PathVariable UUID likeId) {
		likeService.removeLike(likeId);
		return new ResponseEntity<>("Like removed successfully", HttpStatus.OK);
	}

	// Esempio di endpoint per ottenere il conteggio di "like" per un post specifico
	@GetMapping("/count/{postId}")
	public ResponseEntity<Integer> getLikeCountForPost(@PathVariable UUID postId) {
		int likeCount = likeService.getLikeCountForPost(postId);
		return new ResponseEntity<>(likeCount, HttpStatus.OK);
	}

	// Esempio di endpoint per verificare se un utente ha messo "like" a un post
	@GetMapping("/hasLiked/{userId}/{postId}")
	public ResponseEntity<Boolean> hasUserLikedPost(@PathVariable UUID userId, @PathVariable UUID postId) {
		boolean hasLiked = likeService.hasUserLikedPost(userId, postId);
		return new ResponseEntity<>(hasLiked, HttpStatus.OK);
	}
}

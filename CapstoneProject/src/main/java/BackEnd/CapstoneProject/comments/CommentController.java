package BackEnd.CapstoneProject.comments;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BackEnd.CapstoneProject.User.User;
import BackEnd.CapstoneProject.User.UserService;

@RestController
@RequestMapping("/user/comment")
public class CommentController {
	@Autowired
	CommentService commentService;
	@Autowired
	CommentRepo commentRepo;
	@Autowired
	private UserService userService;

	@GetMapping("/getAllComments/{postId}")
	public List<CommentDTO> getCommentsByPostId(@PathVariable("postId") UUID postId) {
		@SuppressWarnings("unused")
		Sort sortByCreationDateDesc = Sort.by(Sort.Direction.DESC, "datacreazione");
		List<CommentDTO> comments = commentService.getAllFilteredCommentsByPost(postId);
		comments.sort((c1, c2) -> c2.getDataCreazione().compareTo(c1.getDataCreazione()));
		return comments;
	}

	@GetMapping("/{commentId}")
	public ResponseEntity<Comment> getCommentById(@PathVariable UUID commentId) {
		Comment comment = commentService.getCommentById(commentId);
		return new ResponseEntity<>(comment, HttpStatus.OK);
	}

	@PostMapping("/{postId}/create")
	public ResponseEntity<String> createComment(@PathVariable UUID postId, @RequestBody CommentPayload body) {
		@SuppressWarnings("unused")
		Comment comment = commentService.salvaCommento(postId, body);
		String successMessage = "Commento creato con successo";
		return ResponseEntity.status(HttpStatus.CREATED).body(successMessage);
	}

	@PostMapping("/{commentId}/togglelike")
	public ResponseEntity<String> likeOrUnlikePost(@PathVariable UUID commentId) {
		User currentUser = userService.getCurrentUser();
		UUID userId = currentUser.getUserId();

		commentService.toggleLike(commentId, userId);

		String responseMessage;

		if (commentService.isUserLikedcomment(commentId, userId)) {
			responseMessage = "Hai messo like a questo commento!";
		} else {
			responseMessage = "Hai rimosso like!";
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable UUID commentId) {
		Optional<Comment> commentToDelete = commentRepo.findById(commentId);
		if (!commentToDelete.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		try {
			commentService.deleteCommentById(commentId);
			return ResponseEntity.status(HttpStatus.OK).body("Commento Eliminato con successo");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Errore durante l'eliminazione del commento.");
		}
	}

}

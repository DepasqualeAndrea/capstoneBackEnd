package BackEnd.CapstoneProject.comments;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
@RequestMapping("/user/comment")
public class CommentController {
	@Autowired
	CommentService commentService;
	@Autowired
	CommentRepo commentRepo;

	@PostMapping("/create")
	public ResponseEntity<Comment> createComment(@RequestBody CommentPayload body) {
		Comment comment = commentService.salvaCommento(body);
		return new ResponseEntity<>(comment, HttpStatus.CREATED);
	}

	@PostMapping("/{parentCommentId}/reply")
	public ResponseEntity<Comment> createReply(@PathVariable UUID parentCommentId, @RequestBody CommentPayload body) {
		Comment reply = commentService.createReply(parentCommentId, body);
		return new ResponseEntity<>(reply, HttpStatus.CREATED);
	}

	@GetMapping("/")
	public ResponseEntity<List<Comment>> getAllComments() {
		List<Comment> comments = commentService.getAllComments();
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}

	@GetMapping("/{commentId}")
	public ResponseEntity<Comment> getCommentById(@PathVariable UUID commentId) {
		Comment comment = commentService.getCommentById(commentId);
		return new ResponseEntity<>(comment, HttpStatus.OK);
	}

	@GetMapping("/getAllComments/{postId}")
	public ArrayList<Comment> getCommentsByPostId(@PathVariable("postId") UUID postId) {
		return commentService.getAllComment(postId);
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

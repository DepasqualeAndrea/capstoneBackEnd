package BackEnd.CapstoneProject.reply;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BackEnd.CapstoneProject.comments.CommentRepo;

@RestController
@RequestMapping("/user/reply")
public class ReplyController {
	@Autowired
	ReplyService replyService;
	@Autowired
	CommentRepo commentRepo;

	@PostMapping("/create/{commentId}")
	public ResponseEntity<Reply> createReply(@PathVariable UUID commentId, @RequestBody Reply body) {
		Reply createdReply = replyService.createReply(commentId, body);
		return ResponseEntity.ok(createdReply);
	}

	@GetMapping("/byComment/{commentId}")
	public List<ReplyDTO> getRepliesByCommentId(@PathVariable UUID commentId) {
		return replyService.getRepliesByCommentId(commentId);
	}

//	@DeleteMapping("/{repliesId}")
//	public ResponseEntity<?> deleteComment(@PathVariable UUID repliesId) {
//		Optional<Comment> commentToDelete = commentRepo.findById(repliesId);
//		if (!commentToDelete.isPresent()) {
//			return ResponseEntity.notFound().build();
//		}
//
//		try {
//			replyService.deleteCommentById(repliesId);
//			return ResponseEntity.status(HttpStatus.OK).body("Commento Eliminato con successo");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Errore durante l'eliminazione del commento.");
//		}
//	}
}

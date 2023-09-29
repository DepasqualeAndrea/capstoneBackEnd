package BackEnd.CapstoneProject.reply;

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

import BackEnd.CapstoneProject.User.User;
import BackEnd.CapstoneProject.User.UserService;

@RestController
@RequestMapping("/user/reply")
public class ReplyController {
	@Autowired
	ReplyService replyService;
	@Autowired
	ReplyRepo replyRepo;
	@Autowired
	UserService userService;

	@PostMapping("/create/{commentId}")
	public ResponseEntity<Reply> createReply(@PathVariable UUID commentId, @RequestBody Reply body) {
		Reply createdReply = replyService.createReply(commentId, body);
		return ResponseEntity.ok(createdReply);
	}

	@PostMapping("/{repliesId}/togglelike")
	public ResponseEntity<String> likeOrUnlikePost(@PathVariable UUID repliesId) {
		User currentUser = userService.getCurrentUser();
		UUID userId = currentUser.getUserId();

		replyService.toggleLike(repliesId, userId);

		String responseMessage;

		if (replyService.isUserLikedReply(repliesId, userId)) {
			responseMessage = "Hai messo like a questo commento!";
		} else {
			responseMessage = "Hai rimosso like!";
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
	}

	@GetMapping("/byComment/{commentId}")
	public List<ReplyDTO> getRepliesByCommentId(@PathVariable UUID commentId) {
		List<ReplyDTO> replies = replyService.getRepliesByCommentId(commentId);

		replies.sort((r1, r2) -> r2.getDataCreazione().compareTo(r1.getDataCreazione()));

		return replies;
	}

	@DeleteMapping("/{repliesId}")
	public ResponseEntity<?> deleteComment(@PathVariable UUID repliesId) {
		Optional<Reply> commentToDelete = replyRepo.findById(repliesId);
		if (!commentToDelete.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		try {
			replyService.deleteReplyById(repliesId);
			return ResponseEntity.status(HttpStatus.OK).body("Commento Eliminato con successo (reply)");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Errore durante l'eliminazione del commento.");
		}
	}
}

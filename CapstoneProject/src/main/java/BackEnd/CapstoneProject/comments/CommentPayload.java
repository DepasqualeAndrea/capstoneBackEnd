package BackEnd.CapstoneProject.comments;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CommentPayload {
	private UUID commentId;
	private String content;
	private String username;
	private Date timestamp;
	private UUID postId;
	private UUID usercommentId;

}

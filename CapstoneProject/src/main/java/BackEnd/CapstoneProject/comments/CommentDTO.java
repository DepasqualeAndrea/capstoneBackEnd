package BackEnd.CapstoneProject.comments;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {
	private UUID commentId;
	private String content;
	private LocalDateTime dataCreazione;
	private UUID usercommentId;
	private List<CommentDTO> replies;
	private UUID postId;
}

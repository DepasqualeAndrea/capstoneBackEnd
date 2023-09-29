package BackEnd.CapstoneProject.comments;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import BackEnd.CapstoneProject.reply.ReplyDTO;
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
	private List<ReplyDTO> replies;
	private UUID postId;
	private Set<UUID> likedCommentByUsers = new HashSet<>();
	private Integer likeCount = 0;
}

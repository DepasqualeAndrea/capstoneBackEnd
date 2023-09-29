package BackEnd.CapstoneProject.reply;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReplyDTO {
	private UUID replyId;
	private String content;
	private LocalDateTime dataCreazione;
	private UUID userReplyId;
	private String username;
	private Set<UUID> likedReplyByUsers = new HashSet<>();
	private Integer likeCount = 0;
	private String userReplyImage;
}

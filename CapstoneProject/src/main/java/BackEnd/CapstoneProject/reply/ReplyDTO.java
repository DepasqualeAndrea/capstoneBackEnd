package BackEnd.CapstoneProject.reply;

import java.time.LocalDateTime;
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
}

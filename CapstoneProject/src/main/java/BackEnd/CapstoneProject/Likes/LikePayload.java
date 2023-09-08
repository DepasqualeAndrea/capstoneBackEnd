package BackEnd.CapstoneProject.Likes;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LikePayload {
	private LocalDate timestamp;
	private Integer targetType;
	private UUID postId;
	private UUID userId;
}

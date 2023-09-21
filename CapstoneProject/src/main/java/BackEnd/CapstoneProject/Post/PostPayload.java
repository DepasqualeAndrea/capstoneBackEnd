package BackEnd.CapstoneProject.Post;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PostPayload {
	private LocalDate timestamp;
	private String description;
	private String imageUrl;
	private UUID userId;

}

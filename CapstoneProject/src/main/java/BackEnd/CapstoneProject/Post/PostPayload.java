package BackEnd.CapstoneProject.Post;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostPayload {
	private LocalDate timestamp;
	private String description;
	private String imageUrl;
	private UUID userId;

	public PostPayload(String description, String imageUrl) {
		this.description = description;
		this.imageUrl = imageUrl;
	}

}

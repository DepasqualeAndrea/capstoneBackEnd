package BackEnd.CapstoneProject.comments;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Commenti")
@Data
@NoArgsConstructor
@ToString
public class Comment {
	@Id
	@GeneratedValue
	private UUID commentId;
	private String content;
	private String username;
	private LocalDate timestamp;
	private UUID postId;
	private UUID userId;

	public Comment(LocalDate timestamp, String username, String content, UUID postId, UUID userId) {
		this.content = content;
		this.username = username;
		this.timestamp = timestamp;
		this.postId = postId;
		this.userId = userId;
	}

}

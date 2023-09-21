package BackEnd.CapstoneProject.comments;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrimaryKeyJoinColumn;
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
	@PrimaryKeyJoinColumn
	private UUID commentId;
	private String content;
	private LocalDateTime dataCreazione;
	private UUID postId;
	private UUID userId;

	public Comment(LocalDateTime dataCreazione, String content, UUID postId, UUID userId) {
		this.content = content;
		this.dataCreazione = dataCreazione;
		this.postId = postId;
		this.userId = userId;
	}

}

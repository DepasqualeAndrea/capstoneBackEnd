package BackEnd.CapstoneProject.Likes;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Like")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {
	@Id
	@GeneratedValue
	private UUID likeId;
	private LocalDate timestamp;
	private Integer targetType;
	private UUID postId;
	private UUID userId;

	public Like(LocalDate timestamp, Integer targetType, UUID postId, UUID userId) {
		this.timestamp = timestamp;
		this.targetType = targetType;
		this.postId = postId;
		this.userId = userId;
	}

}

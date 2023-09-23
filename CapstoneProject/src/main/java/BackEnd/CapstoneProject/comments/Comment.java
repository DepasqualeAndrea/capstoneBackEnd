package BackEnd.CapstoneProject.comments;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "commentId")
public class Comment {
	@Id
	@GeneratedValue
	@PrimaryKeyJoinColumn
	private UUID commentId;
	private String content;
	private LocalDateTime dataCreazione;
	private UUID postId;
	private UUID userId;

	@ManyToOne
	@JoinColumn(name = "parent_comment_id")
	private Comment parentComment;

	@OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
	private List<Comment> replies = new ArrayList<>();

	public Comment(LocalDateTime dataCreazione, String content, UUID postId, UUID userId) {
		this.content = content;
		this.dataCreazione = dataCreazione;
		this.postId = postId;
		this.userId = userId;
	}

}

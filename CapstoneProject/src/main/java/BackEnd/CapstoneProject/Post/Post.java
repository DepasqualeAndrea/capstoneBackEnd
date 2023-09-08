package BackEnd.CapstoneProject.Post;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import BackEnd.CapstoneProject.Likes.Like;
import BackEnd.CapstoneProject.comments.Comment;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Posts")
@Data
@NoArgsConstructor
public class Post {
	@Id
	@GeneratedValue
	private UUID postId;
	private LocalDate timestamp;
	private String description;
	private String imageUrl;
	private UUID userId;

	@OneToMany
	private List<Comment> comment = new ArrayList<>();

	@OneToMany
	private List<Like> like = new ArrayList<>();

	public Post(LocalDate timestamp, String description, String imageUrl, UUID userId) {
		this.timestamp = timestamp;
		this.description = description;
		this.imageUrl = imageUrl;
		this.userId = userId;
	}

}

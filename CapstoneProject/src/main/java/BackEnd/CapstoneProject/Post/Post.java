package BackEnd.CapstoneProject.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import BackEnd.CapstoneProject.comments.Comment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "postId")
public class Post {
	@Id
	@GeneratedValue
	@PrimaryKeyJoinColumn
	private UUID postId;
	@Column(name = "datacreazione")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDateTime datacreazione;

	private String description;

	private String postImageUrl;
	@Column(name = "liked_by_users")
	@ElementCollection
	private Set<UUID> likedByUsers = new HashSet<>();

	private Integer likeCount = 0;

	@JoinColumn(name = "userId")
	private UUID userId;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();

	public Post(LocalDateTime datacreazione, String description, String postImageUrl, UUID userId) {
		this.datacreazione = datacreazione;
		this.description = description;
		this.postImageUrl = postImageUrl;
		this.userId = userId;
	}

}

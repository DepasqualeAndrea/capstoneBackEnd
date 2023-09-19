package BackEnd.CapstoneProject.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import BackEnd.CapstoneProject.comments.Comment;
import BackEnd.CapstoneProject.dbimage.ImageData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Posts")
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "postId")
public class Post {
	@Id
	@GeneratedValue
	@PrimaryKeyJoinColumn
	private UUID postId;
	private LocalDateTime datacreazione;
	private String description;
	private String imageUrl;
	private Integer likeCount;
	// @Column(name = "user_image")
	// private ImageData userImage;
	private String username;

	@Column(name = "user_id")
	private UUID userId;

	@OneToMany(fetch = FetchType.EAGER)
	private List<Comment> comment = new ArrayList<>();
	@OneToOne(fetch = FetchType.EAGER)
	private ImageData imagedata;

	public Post(ImageData imagedata, LocalDateTime datacreazione, String description, String imageUrl, UUID userId,
			String username, String userImage) {
		this.datacreazione = datacreazione;
		this.description = description;
		this.imageUrl = imageUrl;
		this.userId = userId;
	}

}

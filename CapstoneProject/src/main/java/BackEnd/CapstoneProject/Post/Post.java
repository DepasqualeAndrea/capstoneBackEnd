package BackEnd.CapstoneProject.Post;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import BackEnd.CapstoneProject.Likes.Like;
import BackEnd.CapstoneProject.comments.Comment;
import BackEnd.CapstoneProject.dbimage.ImageData;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

	@OneToMany(fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Comment> comment = new ArrayList<>();
	@OneToMany(fetch = FetchType.EAGER)
	@JsonIgnore
	private List<ImageData> imagedata = new ArrayList<>();
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER)
	private List<Like> like;

	public Post(List<ImageData> imagedata, LocalDate timestamp, String description, String imageUrl, UUID userId) {
		this.timestamp = timestamp;
		this.description = description;
		this.imageUrl = imageUrl;
		this.userId = userId;
	}

}

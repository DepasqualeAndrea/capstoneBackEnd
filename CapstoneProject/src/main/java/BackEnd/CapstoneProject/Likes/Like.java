package BackEnd.CapstoneProject.Likes;

import java.util.Date;
import java.util.UUID;

import BackEnd.CapstoneProject.Post.Post;
import BackEnd.CapstoneProject.User.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User userId;

	private Date timestamp;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Post postId;

	private Integer target_type;
}

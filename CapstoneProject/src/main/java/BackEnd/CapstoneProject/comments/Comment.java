package BackEnd.CapstoneProject.comments;

import java.util.Date;
import java.util.UUID;

import BackEnd.CapstoneProject.Post.Post;
import BackEnd.CapstoneProject.User.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	private Date timestamp;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "user_id", referencedColumnName = "userId")
	private User userId;
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "post_id", referencedColumnName = "postId")
	private Post postId;

}

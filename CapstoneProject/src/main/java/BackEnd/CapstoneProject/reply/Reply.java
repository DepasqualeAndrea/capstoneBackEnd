package BackEnd.CapstoneProject.reply;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import BackEnd.CapstoneProject.comments.Comment;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Replies")
@NoArgsConstructor
@Getter
@Setter
@Data
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "repliesId")
public class Reply {
	@Id
	@GeneratedValue
	@PrimaryKeyJoinColumn
	private UUID repliesId;
	private String content;
	private LocalDateTime dataCreazione;
	private UUID usercommentId;

	@ManyToOne
	private Comment comment;

	@Column(name = "liked_Reply_by_users")
	@ElementCollection
	private Set<UUID> likedReplyByUsers = new HashSet<>();

	private Integer likeCount = 0;

}
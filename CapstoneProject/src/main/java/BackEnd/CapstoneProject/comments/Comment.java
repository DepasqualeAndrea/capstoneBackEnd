package BackEnd.CapstoneProject.comments;

import java.util.Date;
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
	private UUID commentID;

	private UUID postID;
	private String userID;

	private String userImage;
	private String userName;

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private String comment;
	private Date timestamp;

	public Comment(UUID commentID, UUID postID, String userID, String userImage, String userName, String comment,
			Date timestamp) {
		super();
		this.commentID = commentID;
		this.postID = postID;
		this.userID = userID;
		this.userImage = userImage;
		this.userName = userName;
		this.comment = comment;
		this.timestamp = timestamp;
	}

	public UUID getCommentID() {
		return commentID;
	}

	public void setCommentID(UUID commentID) {
		this.commentID = commentID;
	}

	public UUID getPostID() {
		return postID;
	}

	public void setPostID(UUID postID) {
		this.postID = postID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}

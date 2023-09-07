package BackEnd.CapstoneProject.Post;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
	@Id
	@GeneratedValue
	@PrimaryKeyJoinColumn
	private UUID postID;

	private String userID;
	private String userName;
	private String imageURL;

	private String description;
	private String postImgURL;

	private int likes;
	private Date timeStamp;

	public Post(String userID, String userName, String imageURL, String description, String postImgURL, int likes,
			Date timeStamp) {
		this.userID = userID;
		this.userID = userID;
		this.userName = userName;
		this.imageURL = imageURL;
		this.description = description;
		this.postImgURL = postImgURL;
		this.likes = likes;
		this.timeStamp = timeStamp;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPostImgURL() {
		return postImgURL;
	}

	public void setPostImgURL(String postImgURL) {
		this.postImgURL = postImgURL;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public Date getDateTime() {
		return timeStamp;
	}

}

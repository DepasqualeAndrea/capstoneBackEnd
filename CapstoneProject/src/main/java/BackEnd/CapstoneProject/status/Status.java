package BackEnd.CapstoneProject.status;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@ToString
@Table(name = "Status")
public class Status {
	@Id
	@GeneratedValue
	private UUID statusID;

	private String userID;
	private String statusImageURL;
	private Date uploadTIme;

	public Status(UUID statusID, String userID, String statusImageURL, Date uploadTIme) {
		this.statusID = statusID;
		this.userID = userID;
		this.statusImageURL = statusImageURL;
		this.uploadTIme = uploadTIme;
	}

	public UUID getStatusID() {
		return statusID;
	}

	public void setStatusID(UUID statusID) {
		this.statusID = statusID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getStatusImageURL() {
		return statusImageURL;
	}

	public void setStatusImageURL(String statusImageURL) {
		this.statusImageURL = statusImageURL;
	}

	public Date getUploadTIme() {
		return uploadTIme;
	}

	public void setUploadTIme(Date uploadTIme) {
		this.uploadTIme = uploadTIme;
	}

}

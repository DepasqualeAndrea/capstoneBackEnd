package BackEnd.CapstoneProject.dbimage;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import BackEnd.CapstoneProject.Post.Post;
import BackEnd.CapstoneProject.User.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "imageData")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageData {
	@Id
	@GeneratedValue
	private UUID id;
	private String name;
	private String type;
	@Lob
	@Column(name = "imagedata", length = 1000)
	private byte[] imageData;

	@ManyToOne
	@JsonIgnore
	private User user;

	@ManyToOne
	private Post post;

}

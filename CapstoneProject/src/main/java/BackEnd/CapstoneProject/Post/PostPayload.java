package BackEnd.CapstoneProject.Post;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import BackEnd.CapstoneProject.dbimage.ImageData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PostPayload {
	private LocalDate timestamp;
	private String description;
	private String imageUrl;
	private UUID userId;
	List<ImageData> imagedata;
}

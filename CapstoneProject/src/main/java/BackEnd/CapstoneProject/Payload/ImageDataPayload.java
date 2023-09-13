package BackEnd.CapstoneProject.Payload;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ImageDataPayload {
	private UUID id;
	private String name;
	private String type;
	private byte[] imageData;
}

package BackEnd.CapstoneProject.Payload;

import org.springframework.web.multipart.MultipartFile;

import BackEnd.CapstoneProject.User.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegistrationModel {
	private MultipartFile image;
	private User user;
}

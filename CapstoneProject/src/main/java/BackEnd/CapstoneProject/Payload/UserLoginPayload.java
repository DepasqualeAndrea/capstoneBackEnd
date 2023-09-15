package BackEnd.CapstoneProject.Payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginPayload {
	String email;
	String password;
}

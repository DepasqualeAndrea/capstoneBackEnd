package BackEnd.CapstoneProject.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import BackEnd.CapstoneProject.Exception.UnauthorizedException;
import BackEnd.CapstoneProject.Payload.UserLoginPayload;
import BackEnd.CapstoneProject.User.User;
import BackEnd.CapstoneProject.User.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private UserService utenteService;
	@Autowired
	JWTTools jwtTools;
	@Autowired
	PasswordEncoder bcrypt;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public User registerUser(@ModelAttribute User registrationDTO, MultipartFile image) throws IOException {
		String hashedPassword = bcrypt.encode(registrationDTO.getPassword());
		registrationDTO.setPassword(hashedPassword);
		return utenteService.registerUser(registrationDTO, image);
	}

	@PostMapping("/login")
	public TokenResponse login(@RequestBody UserLoginPayload body) {

		User user = utenteService.findByEmail(body.getEmail());

		if (bcrypt.matches(body.getPassword(), user.getPassword())) {
			String token = jwtTools.createToken(user);
			return new TokenResponse(token);

		} else {
			throw new UnauthorizedException("Credenziali non valide");
		}
	}

}

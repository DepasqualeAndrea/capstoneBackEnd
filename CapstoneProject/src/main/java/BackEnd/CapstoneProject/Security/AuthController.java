package BackEnd.CapstoneProject.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import BackEnd.CapstoneProject.Exception.UnauthorizedException;
import BackEnd.CapstoneProject.Payload.UserLoginPayload;
import BackEnd.CapstoneProject.Payload.UserRequestPayload;
import BackEnd.CapstoneProject.User.User;
import BackEnd.CapstoneProject.User.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	UserService utenteService;

	@Autowired
	JWTTools jwtTools;

	@Autowired
	PasswordEncoder bcrypt;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public User saveUser(@RequestBody UserRequestPayload body) {
		body.setPassword(bcrypt.encode(body.getPassword()));
		User created = utenteService.creaUtente(body);
		return created;
	}

	@PostMapping("/login")

	public ResponseEntity<TokenResponse> login(@RequestBody UserLoginPayload body) {

		User utente = null;

		if (body.getEmail() != null) {
			utente = utenteService.findByEmail(body.getEmail());
		} else {
			utente = utenteService.findByUsername(body.getUsername());
		}

		if (utente != null && bcrypt.matches(body.getPassword(), utente.getPassword())) {
			String token = jwtTools.creaToken(utente);
			return new ResponseEntity<>(new TokenResponse(token), HttpStatus.OK);

		} else {
			throw new UnauthorizedException(
					"Credenziali non valide, verifica che la password o Email ed Username siano corrette");
		}
	}
}

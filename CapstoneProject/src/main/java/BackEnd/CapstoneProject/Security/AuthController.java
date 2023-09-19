package BackEnd.CapstoneProject.Security;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import BackEnd.CapstoneProject.Exception.UnauthorizedException;
import BackEnd.CapstoneProject.Payload.UserLoginPayload;
import BackEnd.CapstoneProject.User.Ruolo;
import BackEnd.CapstoneProject.User.User;
import BackEnd.CapstoneProject.User.UserService;
import BackEnd.CapstoneProject.dbimage.ImageData;
import BackEnd.CapstoneProject.dbimage.StorageRepo;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private UserService utenteService;
	@Autowired
	private StorageRepo imageRepository;
	@Autowired
	JWTTools jwtTools;

	@Autowired
	PasswordEncoder bcrypt;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public User saveUser(@RequestParam("image") MultipartFile image, @ModelAttribute User body) throws IOException {

		if (image.isEmpty()) {
			throw new IllegalArgumentException("L'immagine non è stata fornita.");
		}

		User user = new User();
		user.setNome(body.getNome());
		user.setCognome(body.getCognome());
		user.setUsername(body.getUsername());
		user.setEmail(body.getEmail());
		user.setPassword(bcrypt.encode(body.getPassword()));
		user.setDataDiNascita(body.getDataDiNascita());
		user.setDataRegistrazione(LocalDate.now());

		user.setRole(Ruolo.USER);

		// Salva l'utente nel database
		User utente = utenteService.creaUtente(user);

		byte[] imageBytes = image.getBytes();
		String imageName = image.getOriginalFilename();
		String imageType = image.getContentType();

		ImageData imageData = new ImageData();
		imageData.setName(imageName);
		imageData.setType(imageType);
		imageData.setImageData(imageBytes);
		imageData.setUser(utente);

		// Salva l'immagine nel database
		imageRepository.save(imageData);

		// Associa l'immagine all'utente
		utente.setImagedata(imageData);

		// Salva l'utente con l'immagine associata
		return utenteService.saveUserWithImage(utente, imageData);
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

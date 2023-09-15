package BackEnd.CapstoneProject.Security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	public User saveUser(@RequestParam("image") List<MultipartFile> image, @ModelAttribute User body)
			throws IOException {

		if (image.isEmpty()) {
			throw new IllegalArgumentException("Le immagini non sono state fornite.");
		}

		List<ImageData> imageList = new ArrayList<>();
		User user = new User();
		user.setNome(body.getNome());
		user.setCognome(body.getCognome());
		user.setUsername(body.getUsername());
		user.setEmail(body.getEmail());
		user.setPassword(bcrypt.encode(body.getPassword()));
		user.setRole(Ruolo.USER);
		User utente = utenteService.creaUtente(user);
		System.out.println(utente.getUserId());
		for (MultipartFile file : image) {
			byte[] imageBytes = file.getBytes();
			String name = file.getName();
			String type = file.getContentType();
			ImageData imageData = new ImageData();
			imageData.setName(name);
			imageData.setType(type);
			imageData.setImageData(imageBytes);
			imageData.setUser(utente);

			imageRepository.save(imageData);
			imageList.add(imageData);
			utente.setImagedata(imageList);

		}

		return utenteService.saveUserWithImages(utente, imageList);
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

package BackEnd.CapstoneProject.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public User saveUser(@RequestParam("image") MultipartFile file, @ModelAttribute User body) throws IOException {
		// Controlla se il file è vuoto o non è stato fornito
		if (file.isEmpty()) {
			throw new IllegalArgumentException("L'immagine non è stata fornita.");
		}

		// Esegui il salvataggio dell'immagine
		byte[] imageBytes = file.getBytes(); // Converte MultipartFile in un array di byte
		// Ora puoi utilizzare imageBytes per salvare l'immagine su disco o nel database
		// utilizzando userService.saveImage()

		// Codifica la password prima di salvarla
		body.setPassword(bcrypt.encode(body.getPassword()));

		// Salva l'utente nel database passando l'array di byte dell'immagine
		User created = utenteService.saveUserWithImage(body, imageBytes);
		return created;
	}

//	@PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	@ResponseStatus(HttpStatus.CREATED)
//	public User saveUser(@RequestParam("image") MultipartFile file, @ModelAttribute User body) throws IOException {
//		body.setPassword(bcrypt.encode(body.getPassword()));
//		User created = utenteService.creaUtente(body, file);
//		return created;
//	}
//	@PostMapping("/register")
//	public ResponseEntity<String> registerUser(@ModelAttribute UserRegistrationModel registrationModel) {
//		try {
//			// Controlla se body è nullo
//			if (registrationModel == null) {
//				return ResponseEntity.badRequest().body("Richiesta di registrazione non valida.");
//			}
//
//			// Controlla se l'immagine è vuota o non è stata fornita
//			MultipartFile image = registrationModel.getImage();
//			if (image.isEmpty()) {
//				return ResponseEntity.badRequest().body("L'immagine non è stata fornita.");
//			}
//
//			// Controlla se l'utente è nullo o alcuni dei campi essenziali sono vuoti
//			User user = registrationModel.getUser();
//			if (user == null || StringUtils.isEmpty(user.getNome()) || StringUtils.isEmpty(user.getCognome())) {
//				return ResponseEntity.badRequest().body("Dati utente incompleti.");
//			}
//
//			// Esegui il salvataggio dell'immagine
//			byte[] imageBytes = image.getBytes(); // Converte MultipartFile in un array di byte
//			// Ora puoi utilizzare imageBytes per salvare l'immagine su disco o nel database
//
//			// Esegui il salvataggio dell'utente
//			utenteService.saveUserWithImage(user, imageBytes); // Passa imageBytes invece di image
//
//			return ResponseEntity.ok("Utente registrato con successo.");
//		} catch (IOException e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Errore durante il salvataggio dell'utente e/o dell'immagine.");
//		}
//	}

//	@PostMapping("/register")
//	public ResponseEntity<String> registerUser(@ModelAttribute UserRegistrationModel registrationModel) {
//		try {
//			// Controlla se body è nullo
//			if (registrationModel == null) {
//				return ResponseEntity.badRequest().body("Richiesta di registrazione non valida.");
//			}
//
//			// Controlla se l'immagine è vuota o non è stata fornita
//			MultipartFile image = registrationModel.getImage();
//			if (image.isEmpty()) {
//				return ResponseEntity.badRequest().body("L'immagine non è stata fornita.");
//			}
//
//			// Controlla se l'utente è nullo o alcuni dei campi essenziali sono vuoti
//			User user = registrationModel.getUser();
//			if (user == null || StringUtils.isEmpty(user.getNome()) || StringUtils.isEmpty(user.getCognome())) {
//				return ResponseEntity.badRequest().body("Dati utente incompleti.");
//			}
//
//			// Esegui il salvataggio dell'immagine
//			byte[] imageBytes = image.getBytes();
//			// Puoi implementare la logica per salvare l'immagine su disco o nel database
//			// utilizzando userService.saveImage()
//
//			// Esegui il salvataggio dell'utente
//			utenteService.creaUtente(user, image);
//
//			return ResponseEntity.ok("Utente registrato con successo.");
//		} catch (IOException e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Errore durante il salvataggio dell'utente e/o dell'immagine.");
//		}
//	}

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

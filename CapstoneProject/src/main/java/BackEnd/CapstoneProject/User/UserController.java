package BackEnd.CapstoneProject.User;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BackEnd.CapstoneProject.Payload.UserRequestPayload;

@RestController
@RequestMapping("/user/utente")
public class UserController {
	@Autowired
	private UserService utenteService;
	@Autowired
	private UserRepo userRepo;

	@GetMapping
	public User getCurrentUserWithDetails() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {

			String username = authentication.getName();

			User user = utenteService.findByUsername(username);

			return user;
		} else {

			return null;
		}
	}

	@GetMapping("/{userId}")
	public User findUtentiById(@PathVariable UUID userId) {
		return utenteService.findById(userId);

	}

//	@PostMapping
//	// @PreAuthorize("hasAuthority('ADMIN')")
//	@ResponseStatus(HttpStatus.CREATED)
//
//	public User saveCliente(@RequestBody UserRequestPayload body) {
//		User created = utenteService.creaUtente(body);
//		return created;
//	}

	@PutMapping("/{userId}")
	// @PreAuthorize("hasAuthority('ADMIN')")
	public User updateUtenti(@PathVariable UUID userId, @RequestBody UserRequestPayload body) {
		return utenteService.findByIdAndUpdate(userId, body);
	}

	@DeleteMapping("/{userId}")
	// @PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteUtente(@PathVariable UUID userId) {
		utenteService.findByIdAndDelete(userId);
		return ResponseEntity.ok("Utente eliminato con successo.");
	}
}

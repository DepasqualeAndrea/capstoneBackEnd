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

import BackEnd.CapstoneProject.Exception.UUIDValidator;
import BackEnd.CapstoneProject.Payload.UserRequestPayload;

@RestController
@RequestMapping("/user/utente")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepo userRepo;

	@GetMapping
	public User getCurrentUserWithDetails() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {

			String username = authentication.getName();

			User user = userService.findByUsername(username);

			return user;
		} else {

			return null;
		}
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> findUtentiById(@PathVariable String userId) {
		if (!UUIDValidator.isValidUUID(userId)) {
			return ResponseEntity.badRequest().body("UUID non valido");
		}

		UUID uuid = UUID.fromString(userId);
		User user = userService.findById(uuid);

		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(user);
	}

	@PutMapping("/{userId}")
	// @PreAuthorize("hasAuthority('ADMIN')")
	public User updateUtenti(@PathVariable UUID userId, @RequestBody UserRequestPayload body) {
		return userService.findByIdAndUpdate(userId, body);
	}

	@DeleteMapping("/{userId}")
	// @PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteUtente(@PathVariable UUID userId) {
		userService.findByIdAndDelete(userId);
		return ResponseEntity.ok("Utente eliminato con successo.");
	}
}

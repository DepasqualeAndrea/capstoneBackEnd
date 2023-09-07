package BackEnd.CapstoneProject.User;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import BackEnd.CapstoneProject.Payload.UserRequestPayload;

@RestController
@RequestMapping("/user")
public class UserController {
	private final UserService utenteService;

	@Autowired
	public UserController(UserService utenteService) {
		this.utenteService = utenteService;
	}

	@GetMapping
	public Page<User> getUtenti(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "userId") String sortBy) {
		return utenteService.find(page, size, sortBy);
	}

	@GetMapping("/{userId}")
	public User findUtentiById(@PathVariable UUID userId) {
		return utenteService.findById(userId);

	}

	@PostMapping
	// @PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)

	public User saveCliente(@RequestBody UserRequestPayload body) {
		User created = utenteService.creaUtente(body);
		return created;
	}

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
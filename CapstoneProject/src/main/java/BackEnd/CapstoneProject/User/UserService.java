package BackEnd.CapstoneProject.User;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import BackEnd.CapstoneProject.Exception.BadRequestException;
import BackEnd.CapstoneProject.Exception.NotFoundException;
import BackEnd.CapstoneProject.Payload.UserRequestPayload;

@Service
public class UserService {
	private final UserRepo utenteRepo;

	@Autowired
	public UserService(UserRepo utenteRepo) {
		this.utenteRepo = utenteRepo;
	}

	public User creaUtente(UserRequestPayload body) {
		utenteRepo.findByEmail(body.getEmail()).ifPresent(u -> {
			throw new BadRequestException("L'email è gia presente del database");
		});
		User newUtente = new User(body.getNome(), body.getCognome(), body.getUsername(), body.getEmail(),
				body.getPassword(), Ruolo.USER);
		return utenteRepo.save(newUtente);
	}

	public Page<User> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

		return utenteRepo.findAll(pageable);
	}

	public User findById(UUID id) throws NotFoundException {
		return utenteRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	public User findByIdAndUpdate(UUID id, UserRequestPayload body) throws NotFoundException {
		User found = this.findById(id);
		found.setNome(body.getNome());
		found.setCognome(body.getCognome());
		found.setUsername(body.getUsername());
		found.setEmail(body.getEmail());
		found.setPassword(body.getPassword());
		return utenteRepo.save(found);
	}

	public User getUserById(UUID userId) {
		return utenteRepo.findById(userId)
				.orElseThrow(() -> new NotFoundException("Utente con id " + userId + " non trovato"));
	}

	public void findByIdAndDelete(UUID id) throws NotFoundException {
		User found = this.findById(id);
		utenteRepo.delete(found);
	}

	public User findByEmail(String email) {
		return utenteRepo.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato"));
	}

	public User findByUsername(String username) {
		return utenteRepo.findByUsername(username)
				.orElseThrow(() -> new NotFoundException("Username " + username + " non corrispondente"));
	}

	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();

		if (principal instanceof User) {
			User user = (User) principal;
			String currentUserName = user.getNome();
			Optional<User> userOptional = utenteRepo.findByNome(currentUserName);

			if (userOptional.isPresent()) {
				return userOptional.get();
			}
		}

		throw new NotFoundException("Utente non trovato");
	}

}

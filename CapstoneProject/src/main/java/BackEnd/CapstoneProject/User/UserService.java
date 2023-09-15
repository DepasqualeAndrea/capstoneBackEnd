package BackEnd.CapstoneProject.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import BackEnd.CapstoneProject.Post.PostRepository;
import BackEnd.CapstoneProject.comments.CommentRepo;
import BackEnd.CapstoneProject.dbimage.ImageData;
import BackEnd.CapstoneProject.dbimage.StorageRepo;
import jakarta.transaction.Transactional;

@Service
public class UserService {
	private final UserRepo utenteRepo;
	private final PostRepository postRepo;
	private final CommentRepo commentRepo;
	private final StorageRepo imageRepo;

	public UserService(UserRepo utenteRepo, StorageRepo imageRepo, PostRepository postRepo, CommentRepo commentRepo) {
		this.utenteRepo = utenteRepo;
		this.postRepo = postRepo;
		this.commentRepo = commentRepo;
		this.imageRepo = imageRepo;
	}

	@Transactional
	public User creaUtente(User body) {
		utenteRepo.findByEmail(body.getEmail()).ifPresent(u -> {
			throw new BadRequestException("L'email è gia presente del database");
		});

		User newUtente = new User(body.getImagedata(), body.getNome(), body.getCognome(), body.getUsername(),
				body.getEmail(), body.getPassword(), Ruolo.USER);

		return utenteRepo.save(newUtente);
	}

	@Transactional
	public User saveUserWithImages(User user, List<ImageData> images) {
		try {
			// Salva le immagini nel database o su disco
			for (ImageData imageData : images) {
				imageRepo.save(imageData);
			}

			// Associa le immagini all'utente
			user.setImagedata(images);

			// Salva l'utente nel database
			return utenteRepo.save(user);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nel salvataggio delle immagini e dell'utente.");
		}
	}

	@Transactional
	public Page<User> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

		return utenteRepo.findAll(pageable);
	}

	@Transactional
	public User findById(UUID id) throws NotFoundException {
		return utenteRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	@Transactional
	public User findByIdAndUpdate(UUID id, UserRequestPayload body) throws NotFoundException {
		User found = this.findById(id);
		found.setNome(body.getNome());
		found.setCognome(body.getCognome());
		found.setUsername(body.getUsername());
		found.setEmail(body.getEmail());
		found.setPassword(body.getPassword());
		return utenteRepo.save(found);
	}

	@Transactional
	public User getUserById(UUID userId) {
		return utenteRepo.findById(userId)
				.orElseThrow(() -> new NotFoundException("Utente con id " + userId + " non trovato"));
	}

	@Transactional
	public void findByIdAndDelete(UUID id) throws NotFoundException {
		User found = this.findById(id);
		utenteRepo.delete(found);
	}

	@Transactional
	public User findByEmail(String email) {
		return utenteRepo.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato"));
	}

	@Transactional
	public User findByUsername(String username) {
		return utenteRepo.findByUsername(username)
				.orElseThrow(() -> new NotFoundException("Username " + username + " non corrispondente"));
	}

	@Transactional
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

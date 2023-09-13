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

import BackEnd.CapstoneProject.Exception.NotFoundException;
import BackEnd.CapstoneProject.Payload.UserRequestPayload;
import BackEnd.CapstoneProject.dbimage.ImageData;
import BackEnd.CapstoneProject.dbimage.StorageRepo;

@Service
public class UserService {
	private final UserRepo utenteRepo;
	@Autowired
	private StorageRepo imageRepository;

	@Autowired
	public UserService(UserRepo utenteRepo) {
		this.utenteRepo = utenteRepo;
	}

//	public User creaUtente(User body, byte[] imageBytes) throws IOException {
//		utenteRepo.findByEmail(body.getEmail()).ifPresent(u -> {
//			throw new BadRequestException("L'email è gia presente del database");
//		});
//
//		User newUtente = new User(body.getNome(), body.getCognome(), body.getUsername(), imageBytes, body.getEmail(),
//				body.getPassword(), Ruolo.USER);
//
//		return utenteRepo.save(newUtente);
//	}

//	public void saveUserWithImage(User user, MultipartFile image) {
//		try {
//			// Converti l'immagine in un array di byte
//			byte[] imageBytes = image.getBytes();
//			user.setImage(imageBytes);
//
//			// Salva l'utente nel database
//			utenteRepo.save(user);
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new RuntimeException("Errore nel salvataggio dell'immagine e dell'utente.");
//		}
//	}

	public User saveUserWithImage(User user, byte[] imageBytes) {
		try {
			// Salva l'immagine nel database o su disco
			// Puoi implementare questa parte utilizzando la logica specifica di salvataggio
			// In questo esempio, supponiamo di salvarla in una tabella o su disco

			// Pseudocodice:
			// ImageEntity imageEntity = new ImageEntity();
			// imageEntity.setData(imageBytes);
			// imageRepository.save(imageEntity);
			ImageData imageData = new ImageData();
			imageData.setImageData(imageBytes);
			imageRepository.save(imageData);
			// Assegna l'immagine all'utente (potrebbe essere una relazione uno-a-uno o
			// uno-a-molti)
			// user.setImage(imageEntity); // Assumi che user abbia un metodo setImage
			user.setImage(imageData);
			// Salva l'utente nel database
			return utenteRepo.save(user);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nel salvataggio dell'immagine e dell'utente.");
		}
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

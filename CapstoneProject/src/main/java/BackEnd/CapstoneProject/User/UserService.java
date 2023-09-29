package BackEnd.CapstoneProject.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import BackEnd.CapstoneProject.Cloudinary.CloudinaryService;
import BackEnd.CapstoneProject.Exception.NotFoundException;
import BackEnd.CapstoneProject.Exception.UserNotFoundException;
import BackEnd.CapstoneProject.Payload.UserRequestPayload;
import jakarta.transaction.Transactional;

@Service
@Lazy
public class UserService {
	private final UserRepo userRepository;
	private final CloudinaryService cloudinaryService;

	public UserService(UserRepo userRepository, CloudinaryService cloudinaryService) {
		this.userRepository = userRepository;

		this.cloudinaryService = cloudinaryService;
	}

	@Transactional
	public User registerUser(User registrationDTO, MultipartFile image) throws IOException {
		if (image.isEmpty()) {
			throw new IllegalArgumentException("Le immagini non sono state fornite.");
		}

		String imageUrl = cloudinaryService.uploadImage(image);

		User user = new User();
		user.setNome(registrationDTO.getNome());
		user.setCognome(registrationDTO.getCognome());
		user.setUsername(registrationDTO.getUsername());
		user.setEmail(registrationDTO.getEmail());
		user.setPassword(registrationDTO.getPassword());
		user.setRole(Ruolo.USER);
		user.setDataRegistrazione(LocalDate.now());
		user.setProfileImageUrl(imageUrl);

		return userRepository.save(user);
	}

	@Transactional
	public User findById(UUID id) throws NotFoundException {
		return userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	@Transactional
	public User findByIdAndUpdate(UUID id, UserRequestPayload body) throws NotFoundException {
		User found = this.findById(id);
		found.setNome(body.getNome());
		found.setCognome(body.getCognome());
		found.setUsername(body.getUsername());
		found.setEmail(body.getEmail());
		found.setPassword(body.getPassword());
		found.setBio(body.getBio());
		found.setCitta(body.getCitta());
		found.setDataDiNascita(body.getDataDiNascita());
		return userRepository.save(found);
	}

	public void toggleFollow(UUID followerId, UUID followingId) {
		User follower = userRepository.findById(followerId)
				.orElseThrow(() -> new UserNotFoundException("Follower not found"));
		User following = userRepository.findById(followingId)
				.orElseThrow(() -> new UserNotFoundException("Following user not found"));

		if (following.getFollowing().contains(follower)) {
			following.getFollowing().remove(follower);
		} else {
			following.getFollowing().add(follower);
		}

		userRepository.save(following);
	}

	public boolean isUserFollowing(UUID followerId, UUID followingId) {
		User follower = userRepository.findById(followingId).orElse(null);

		return follower != null
				&& follower.getFollowing().stream().anyMatch(user -> user.getUserId().equals(followerId));
	}

	@Transactional
	public User getUserById(UUID userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("Utente con id " + userId + " non trovato"));
	}

	@Transactional
	public void findByIdAndDelete(UUID id) throws NotFoundException {
		User found = this.findById(id);
		userRepository.delete(found);
	}

	@Transactional
	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato"));
	}

	@Transactional
	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new NotFoundException("Username " + username + " non corrispondente"));
	}

	@Transactional
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();

		if (principal instanceof User) {
			User user = (User) principal;
			String currentUserName = user.getNome();
			Optional<User> userOptional = userRepository.findByNome(currentUserName);

			if (userOptional.isPresent()) {
				return userOptional.get();
			}
		}

		throw new NotFoundException("Utente non trovato");
	}

}

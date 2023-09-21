package BackEnd.CapstoneProject.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import BackEnd.CapstoneProject.Cloudinary.CloudinaryService;
import BackEnd.CapstoneProject.Exception.NotFoundException;
import BackEnd.CapstoneProject.Payload.UserRequestPayload;
import BackEnd.CapstoneProject.Post.Post;
import BackEnd.CapstoneProject.Post.PostRepository;
import BackEnd.CapstoneProject.comments.CommentRepo;
import jakarta.transaction.Transactional;

@Service
@Lazy
public class UserService {
	private final UserRepo utenteRepo;
	private final PostRepository postRepo;
	private final CommentRepo commentRepo;
	private final CloudinaryService cloudinaryService;

	public UserService(UserRepo utenteRepo, PostRepository postRepo, CommentRepo commentRepo,
			CloudinaryService cloudinaryService) {
		this.utenteRepo = utenteRepo;
		this.postRepo = postRepo;
		this.commentRepo = commentRepo;
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

		user.setFirebaseUid(registrationDTO.getFirebaseUid());

		return utenteRepo.save(user);
	}

	@Transactional
	public Page<User> findAllUsersWithPostsOrderedByDataCreazione(Pageable pageable) {
		Page<Post> postsPage = postRepo.findAllByOrderByDatacreazioneDesc(pageable);

		List<User> usersWithOrderedPosts = new ArrayList<>();

		for (Post post : postsPage.getContent()) {
			User user = utenteRepo.findById(post.getUserId()).orElse(null);
			if (user != null) {
				if (!usersWithOrderedPosts.contains(user)) {
					user.setPost(new ArrayList<>());
					usersWithOrderedPosts.add(user);
				}
				user.getPost().add(post);
			}
		}

		return new PageImpl<>(usersWithOrderedPosts, pageable, postsPage.getTotalElements());
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
		found.setBio(body.getBio());
		found.setCitta(body.getCitta());
		found.setDataDiNascita(body.getDataDiNascita());
		return utenteRepo.save(found);
	}

	@Transactional
	public User getUserById(UUID userId) {
		return utenteRepo.findById(userId)
				.orElseThrow(() -> new NotFoundException("Utente con id " + userId + " non trovato"));
	}

	public User findByFirebaseUid(String firebaseUid) {

		return utenteRepo.findByFirebaseUid(firebaseUid);
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

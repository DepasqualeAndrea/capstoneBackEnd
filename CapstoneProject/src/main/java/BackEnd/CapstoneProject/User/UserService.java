package BackEnd.CapstoneProject.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import BackEnd.CapstoneProject.Exception.BadRequestException;
import BackEnd.CapstoneProject.Exception.NotFoundException;
import BackEnd.CapstoneProject.Payload.UserRequestPayload;
import BackEnd.CapstoneProject.Post.Post;
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
				body.getEmail(), body.getPassword(), Ruolo.USER, body.getDataRegistrazione());

		return utenteRepo.save(newUtente);
	}

	@Transactional
	public User saveUserWithImage(User user, ImageData image) {
		try {
			imageRepo.save(image);
			user.setImagedata(image);
			return utenteRepo.save(user);
		} catch (Exception e) {
			e.printStackTrace();

			throw new RuntimeException("Errore nel salvataggio dell'immagine e dell'utente.");
		}
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

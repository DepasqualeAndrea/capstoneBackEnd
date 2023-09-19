package BackEnd.CapstoneProject.Post;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import BackEnd.CapstoneProject.Exception.BadRequestException;
import BackEnd.CapstoneProject.Exception.NotFoundException;
import BackEnd.CapstoneProject.User.User;
import BackEnd.CapstoneProject.User.UserRepo;
import BackEnd.CapstoneProject.User.UserService;
import BackEnd.CapstoneProject.dbimage.ImageData;
import BackEnd.CapstoneProject.dbimage.StorageRepo;
import jakarta.transaction.Transactional;

@Service
public class PostService {
	private final PostRepository postRepo;
	private final UserService userService;
	private final UserRepo userRepo;
	private final StorageRepo imageRepo;

	@Autowired
	public PostService(PostRepository postRepo, StorageRepo imageRepo, UserService userService, UserRepo userRepo) {
		this.postRepo = postRepo;
		this.userService = userService;
		this.userRepo = userRepo;
		this.imageRepo = imageRepo;
	}

	@Transactional
	public Post creaPost(Post body) {
		postRepo.findByDescription(body.getDescription()).ifPresent(u -> {
			throw new BadRequestException("Il post é gia Esistente!");
		});
		Post post = new Post();
		post.setUserId(userService.getCurrentUser().getUserId());
		post.setDatacreazione(LocalDateTime.now());
		post.setDescription(body.getDescription());
		post.setImageUrl(body.getImageUrl());
		// post.setUserImage(userService.getCurrentUser().getImagedata());
		// post.setUsername(userService.getCurrentUser().getUsername());
		User user = userService.getCurrentUser();
		user.getPost().add(post);
		post = postRepo.save(post);
		userRepo.save(user);
		return post;
	}

	@Transactional
	public Post savePostWithImages(Post post, ImageData image) {
		try {

			imageRepo.save(image);

			post.setImagedata(image);

			return postRepo.save(post);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nel salvataggio dell'immagine e dell'utente.");
		}
	}

	@Transactional
	public Page<Post> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

		return postRepo.findAll(pageable);
	}

	@Transactional
	public Post findById(UUID id) throws NotFoundException {
		return postRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	@Transactional
	public Post findByIdAndUpdate(UUID id, PostPayload body) throws NotFoundException {
		Post found = this.findById(id);
		return postRepo.save(found);
	}

	@Transactional
	public void findByIdAndDelete(UUID id) throws NotFoundException {
		Post found = this.findById(id);
		postRepo.delete(found);
	}

}

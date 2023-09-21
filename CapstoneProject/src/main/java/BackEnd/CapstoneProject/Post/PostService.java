package BackEnd.CapstoneProject.Post;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
		User user = userService.getCurrentUser();
		user.getPost().add(post);
		post = postRepo.save(post);
		userRepo.save(user);
		return post;
	}

	@Transactional
	public Post savePostWithImages(Post body, MultipartFile image) throws IOException {
		if (image.isEmpty()) {
			throw new IllegalArgumentException("Le immagini non sono state fornite.");
		}

		Post post = new Post();
		post.setUserId(userService.getCurrentUser().getUserId());
		post.setDatacreazione(LocalDateTime.now());
		post.setDescription(body.getDescription());
		post.setImageUrl(body.getImageUrl());
		User user = userService.getCurrentUser();
		user.getPost().add(post);
		post = postRepo.save(post);
		userRepo.save(user);

		byte[] imageBytes = image.getBytes();
		String name = image.getName();
		String type = image.getContentType();
		ImageData imageData = new ImageData();
		imageData.setName(name);
		imageData.setType(type);
		imageData.setImageData(imageBytes);
		imageData.setPost(post);

		imageRepo.save(imageData);
		post.setImagedata(imageData);
		return post;
	}

	public Page<Post> getAllPostsOrderedByDataCreazione(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return postRepo.findAllByOrderByDatacreazioneDesc(pageable);
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

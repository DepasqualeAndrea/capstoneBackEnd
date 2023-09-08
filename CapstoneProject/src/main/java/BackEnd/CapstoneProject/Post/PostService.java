package BackEnd.CapstoneProject.Post;

import java.time.LocalDate;
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

@Service
public class PostService {
	private final PostRepository postRepo;
	private final UserService userService;
	private final UserRepo userRepo;

	@Autowired
	public PostService(PostRepository postRepo, UserService userService, UserRepo userRepo) {
		this.postRepo = postRepo;
		this.userService = userService;
		this.userRepo = userRepo;
	}

	public Post creaPost(PostPayload body) {
		postRepo.findByDescription(body.getDescription()).ifPresent(u -> {
			throw new BadRequestException("Il post é gia Esistente!");
		});
		Post post = new Post();
		post.setUserId(userService.getCurrentUser().getUserId());
		post.setTimestamp(LocalDate.now());
		post.setDescription(body.getDescription());
		post.setImageUrl(body.getImageUrl());
		User user = userService.getCurrentUser();
		user.getPost().add(post);
		post = postRepo.save(post);
		userRepo.save(user);
		return post;
	}

	public Page<Post> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

		return postRepo.findAll(pageable);
	}

	public Post findById(UUID id) throws NotFoundException {
		return postRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	public Post findByIdAndUpdate(UUID id, PostPayload body) throws NotFoundException {
		Post found = this.findById(id);
		return postRepo.save(found);
	}

	public void findByIdAndDelete(UUID id) throws NotFoundException {
		Post found = this.findById(id);
		postRepo.delete(found);
	}

}

package BackEnd.CapstoneProject.Post;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import BackEnd.CapstoneProject.Cloudinary.CloudinaryService;
import BackEnd.CapstoneProject.Exception.BadRequestException;
import BackEnd.CapstoneProject.Exception.NotFoundException;
import BackEnd.CapstoneProject.Exception.PostNotFoundException;
import BackEnd.CapstoneProject.User.User;
import BackEnd.CapstoneProject.User.UserRepo;
import BackEnd.CapstoneProject.User.UserService;
import BackEnd.CapstoneProject.comments.Comment;
import BackEnd.CapstoneProject.comments.CommentRepo;
import BackEnd.CapstoneProject.comments.CommentService;
import jakarta.transaction.Transactional;

@Service
public class PostService {
	private final PostRepository postRepo;
	private final UserService userService;
	private final UserRepo userRepo;
	private final CloudinaryService cloudinaryService;
	private final CommentService commentService;
	private final CommentRepo commentRepo;

	@Autowired
	public PostService(PostRepository postRepo, CloudinaryService cloudinaryService, CommentService commentService,
			UserService userService, CommentRepo commentRepo, UserRepo userRepo) {
		this.postRepo = postRepo;
		this.userService = userService;
		this.userRepo = userRepo;
		this.cloudinaryService = cloudinaryService;
		this.commentService = commentService;
		this.commentRepo = commentRepo;

	}

	@Transactional
	public Post savePostWithImages(Post body, MultipartFile image) throws IOException {
		if (image.isEmpty()) {
			throw new IllegalArgumentException("Le immagini non sono state fornite.");
		}
		postRepo.findByDescription(body.getDescription()).ifPresent(u -> {
			throw new BadRequestException("Il post é gia Esistente!");
		});
		String imageUrl = cloudinaryService.uploadImage(image);

		Post post = new Post();
		post.setUserId(userService.getCurrentUser().getUserId());
		post.setDatacreazione(LocalDateTime.now());
		post.setDescription(body.getDescription());
		post.setPostImageUrl(imageUrl);
		User user = userService.getCurrentUser();
		if (user == null || user.getUserId() == null) {
			throw new IllegalStateException("L'utente corrente non è valido o manca l'ID dell'utente.");
		}
		user.getPosts().add(post);
		post = postRepo.save(post);
		userRepo.save(user);
		return post;
	}

	@Transactional
	public void toggleLike(UUID postId, UUID userId) {
		Post post = postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found"));

		Set<UUID> likedByUsers = post.getLikedByUsers();
		if (likedByUsers == null) {
			likedByUsers = new HashSet<>();
		}

		if (likedByUsers.contains(userId)) {

			likedByUsers.remove(userId);
			post.setLikeCount(post.getLikeCount() - 1);
		} else {

			likedByUsers.add(userId);
			post.setLikeCount(post.getLikeCount() + 1);
		}

		post.setLikedByUsers(likedByUsers);
		postRepo.save(post);
	}

	public boolean isUserLikedPost(UUID postId, UUID userId) {
		Post post = postRepo.findById(postId).orElse(null);

		return post != null && post.getLikedByUsers() != null && post.getLikedByUsers().contains(userId);
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
	public void deletePostAndRelatedEntities(UUID postId) throws NotFoundException {
		Post post = this.findById(postId);

		List<User> usersAssociatedWithPost = userRepo.findByPostsPostId(postId);
		for (User user : usersAssociatedWithPost) {
			user.getPosts().remove(post);
			userRepo.save(user);
		}

		post.setLikedByUsers(new HashSet<>());
		post.setLikeCount(0);

		deleteCommentsAndReplies(post.getComments());

		postRepo.delete(post);
	}

	private void deleteCommentsAndReplies(List<Comment> comments) {
		List<Comment> commentsToDelete = new ArrayList<>(comments);

		for (Comment comment : commentsToDelete) {
			removeUserCommentAssociation(comment);

			deleteRepliesAndAssociations(comment);
			deletePostCommentRelationships(comment);

			comments.remove(comment);
		}

		commentRepo.deleteAll(commentsToDelete);
	}

	private void removeUserCommentAssociation(Comment comment) {
		User user = comment.getUser();
		if (user != null) {
			UUID userId = user.getUserId();
			user.getComment().remove(comment);
			userRepo.save(user);
		}
	}

	private void deleteRepliesAndAssociations(Comment comment) {
		List<Comment> replies = new ArrayList<>(comment.getReplies());
		for (Comment reply : replies) {
			deleteRepliesAndAssociations(reply);
			commentRepo.delete(reply);
		}
	}

	private void deletePostCommentRelationships(Comment comment) {
		Post post = comment.getPost();
		if (post != null) {
			UUID postId = post.getPostId();
			post.getComments().remove(comment);
			postRepo.save(post);
		}
	}

}

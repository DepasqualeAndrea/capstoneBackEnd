package BackEnd.CapstoneProject.Post;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
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
		user.getPost().add(post);
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

//	@Transactional
//	public void deletePostAndRelatedEntities(UUID postId) throws NotFoundException {
//		Post post = this.findById(postId);
//
//		// Trova tutti gli utenti associati a questo post
//		List<User> usersAssociatedWithPost = userRepo.findByPostsPostId(postId);
//
//		// Rimuovi l'associazione tra gli utenti e il post dalla tabella "utenti_post"
//		for (User user : usersAssociatedWithPost) {
//			user.getPost().remove(post);
//			userRepo.save(user);
//		}
//
//		// Elimina i like associati al post
//		post.setLikedByUsers(new HashSet<>()); // Rimuovi tutti i like
//		post.setLikeCount(0);
//
//		// Elimina i commenti associati al post (e le relative replies)
//		for (Comment comment : post.getComments()) {
//			commentService.deleteCommentAndReplies(comment.getCommentId());
//		}
//
//		// Infine, elimina il post stesso
//		postRepo.delete(post);
//	}

//	@Transactional
//	public void deleteCommentAndReplies(UUID commentId) throws NotFoundException {
//		Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
//
//		// Elimina le risposte associate a questo commento
//		for (Comment reply : comment.getReplies()) {
//			// Chiamata per eliminare le associazioni nella tabella "post_comments"
//			deletePostCommentRelationships(reply);
//			commentRepo.delete(reply);
//		}
//
//		// Infine, elimina il commento stesso
//		// Chiamata per eliminare le associazioni nella tabella "post_comments"
//		deletePostCommentRelationships(comment);
//		commentRepo.delete(comment);
//	}

//	private void deletePostCommentRelationships(Comment comment) {
//	    // Supponiamo che tu abbia una tabella "post_comments" con due colonne: "comment_id" e "post_id".
//	    // Qui stiamo cercando di eliminare tutte le righe in cui "comment_id" corrisponde all'ID del commento fornito.
//
//	    // Prima creiamo una query per eliminare le righe da "post_comments" con il commento fornito.
//	    Query deleteQuery = entityManager.createQuery("DELETE FROM PostComment pc WHERE pc.commentId = :commentId");
//	    deleteQuery.setParameter("commentId", comment.getCommentId());
//
//	    // Esegui la query per eliminare le righe.
//	    int rowsAffected = deleteQuery.executeUpdate();
//	    
//	    // Puoi controllare il numero di righe eliminate, se necessario.
//	    System.out.println("Numero di righe eliminate da post_comments: " + rowsAffected);
//	}

}

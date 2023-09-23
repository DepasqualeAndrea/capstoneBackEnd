package BackEnd.CapstoneProject.comments;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import BackEnd.CapstoneProject.Exception.NotFoundException;
import BackEnd.CapstoneProject.Post.Post;
import BackEnd.CapstoneProject.Post.PostPayload;
import BackEnd.CapstoneProject.Post.PostRepository;
import BackEnd.CapstoneProject.Post.PostService;
import BackEnd.CapstoneProject.User.User;
import BackEnd.CapstoneProject.User.UserRepo;
import BackEnd.CapstoneProject.User.UserService;
import jakarta.transaction.Transactional;

@Service
@Lazy
public class CommentService {
	private final PostRepository postRepo;
	private final UserService userService;
	private final UserRepo userRepo;
	private final CommentRepo commentRepo;
	private final PostService ps;

	@Autowired
	public CommentService(PostRepository postRepo, UserService userService, UserRepo userRepo, @Lazy PostService ps,
			CommentRepo commentRepo) {
		this.postRepo = postRepo;
		this.userService = userService;
		this.userRepo = userRepo;
		this.commentRepo = commentRepo;
		this.ps = ps;
	}

	@Transactional
	public Comment salvaCommento(CommentPayload body) {
		Post post = ps.findById(body.getPostId());
		Comment comment = new Comment();
		comment.setUserId(userService.getCurrentUser().getUserId());
		comment.setPostId(body.getPostId());
		comment.setDataCreazione(LocalDateTime.now());
		comment.setContent(body.getContent());
		User user = userService.getCurrentUser();
		user.getComment().add(comment);
		post.getComments().add(comment);
		comment = commentRepo.save(comment);
		userRepo.save(user);
		return comment;
	}

	public Comment createReply(UUID parentCommentId, CommentPayload body) {
		Comment parentComment = commentRepo.findById(parentCommentId)
				.orElseThrow(() -> new NotFoundException("Parent comment not found"));

		User userId = userService.getCurrentUser();
		UUID userIds = userId.getUserId();

		Comment reply = new Comment(LocalDateTime.now(), body.getContent(), body.getPostId(), userIds);
		reply.setParentComment(parentComment);
		parentComment.getReplies().add(reply);
		commentRepo.save(parentComment);

		return commentRepo.save(reply);
	}

	public List<Comment> getAllComments() {
		return commentRepo.findAll();
	}

	public Comment getCommentById(UUID commentId) {
		return commentRepo.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
	}

	public Page<Comment> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

		return commentRepo.findAll(pageable);
	}

	public Comment findById(UUID id) throws NotFoundException {
		return commentRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	public Comment findByIdAndUpdate(UUID id, PostPayload body) throws NotFoundException {
		Comment found = this.findById(id);
		return commentRepo.save(found);
	}

	public void findByIdAndDelete(UUID id) throws NotFoundException {
		Comment found = this.findById(id);
		commentRepo.delete(found);
	}

	public ArrayList<Comment> getAllComment(UUID postId) {
		ArrayList<Comment> result = new ArrayList<Comment>();
		result = commentRepo.findAllByPostId(postId);
		return result;
	}

	@Transactional
	public void deleteCommentAndReplies(UUID commentId) throws NotFoundException {
		Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));

		// Elimina prima tutte le associazioni in utenti_comment
		userRepo.deleteByComment(comment);

		// Elimina prima tutte le risposte associate a questo commento
		List<Comment> replies = comment.getReplies();
		for (Comment reply : replies) {
			// Rimuovi il riferimento dal commento principale
			comment.getReplies().remove(reply);
			// Elimina la risposta
			commentRepo.delete(reply);
		}

		// Infine, elimina il commento principale
		commentRepo.delete(comment);
	}

}

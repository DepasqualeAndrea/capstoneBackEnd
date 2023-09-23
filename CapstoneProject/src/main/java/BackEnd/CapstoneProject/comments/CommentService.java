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

	
		User user = userService.getCurrentUser();

	
		Comment comment = new Comment();
		comment.setUser(user);
		comment.setPost(post);
		comment.setDataCreazione(LocalDateTime.now());
		comment.setContent(body.getContent());

	
		user.getComment().add(comment);
		post.getComments().add(comment);

	
		comment = commentRepo.save(comment);

	
		userRepo.save(user);

		// 7. Restituisci il commento salvato
		return comment;
	}

	public Comment createReply(UUID parentCommentId, CommentPayload body) {
		Comment parentComment = commentRepo.findById(parentCommentId)
				.orElseThrow(() -> new NotFoundException("Parent comment not found"));
		
		Post post = ps.findById(body.getPostId());

		User currentUser = userService.getCurrentUser();
		UUID userId = currentUser.getUserId();

		Comment reply = new Comment();
		reply.setPost(post);
		reply.setContent(body.getContent());
		reply.setDataCreazione(LocalDateTime.now());
		reply.setUser(currentUser);
		reply.setParentComment(parentComment);
		parentComment.getReplies().add(reply);
		commentRepo.save(reply);

		commentRepo.save(parentComment);

		return reply;
	}

	public List<Comment> getAllComments() {
		return commentRepo.findAll();
	}

	public ArrayList<Comment> getAllComment(UUID postId) {
		ArrayList<Comment> result = new ArrayList<Comment>();
		result = (ArrayList<Comment>) commentRepo.findAllByPostId(postId);
		return result;
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

	public void deleteCommentById(UUID commentId) {
		// Verifica se il commento esiste
		if (commentRepo.existsById(commentId)) {
			// Se esiste, elimina il commento utilizzando l'ID
			commentRepo.deleteById(commentId);
		} else {
			// Gestisci il caso in cui il commento non esista
			throw new NotFoundException("Comment with ID " + commentId + " not found");
		}
	}

}

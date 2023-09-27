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
import BackEnd.CapstoneProject.reply.Reply;
import BackEnd.CapstoneProject.reply.ReplyDTO;
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
	public Comment salvaCommento(UUID postId, CommentPayload body) {

		Post post = ps.findById(postId);

		User user = userService.getCurrentUser();

		Comment comment = new Comment();
		comment.setUsercommentId(user.getUserId());
		comment.setPost(post);
		comment.setDataCreazione(LocalDateTime.now());
		comment.setContent(body.getContent());

		user.getComment().add(comment);
		post.getComments().add(comment);

		comment = commentRepo.save(comment);

		userRepo.save(user);

		return comment;
	}

	@Transactional
	public List<CommentDTO> getAllFilteredCommentsByPost(UUID postId) {
		List<Comment> comments = commentRepo.findAllByPostId(postId);
		List<CommentDTO> filteredComments = new ArrayList<>();

		for (Comment comment : comments) {
			// Forza il caricamento delle risposte
			comment.getReplies().size();

			CommentDTO commentDTO = new CommentDTO();
			commentDTO.setCommentId(comment.getCommentId());
			commentDTO.setContent(comment.getContent());
			commentDTO.setDataCreazione(comment.getDataCreazione());
			commentDTO.setUsercommentId(comment.getUsercommentId());
			commentDTO.setPostId(comment.getPost().getPostId());

			// Aggiungi le risposte al commento DTO
			List<ReplyDTO> replyDTOs = new ArrayList<>();
			for (Reply reply : comment.getReplies()) {
				ReplyDTO replyDTO = new ReplyDTO();
				replyDTO.setContent(reply.getContent());
				replyDTO.setDataCreazione(reply.getDataCreazione());
				replyDTO.setReplyId(reply.getRepliesId());
				replyDTO.setUserReplyId(reply.getUsercommentId());
				replyDTOs.add(replyDTO);
			}
			commentDTO.setReplies(replyDTOs);

			filteredComments.add(commentDTO);
		}

		return filteredComments;
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

		if (commentRepo.existsById(commentId)) {

			commentRepo.deleteById(commentId);
		} else {

			throw new NotFoundException("Comment with ID " + commentId + " not found");
		}
	}

}

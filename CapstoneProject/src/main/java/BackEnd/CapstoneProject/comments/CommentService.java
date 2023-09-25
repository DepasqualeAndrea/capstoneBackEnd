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

	public Comment createReply(UUID parentCommentId, CommentPayload body) {
		Comment parentComment = commentRepo.findById(parentCommentId)
				.orElseThrow(() -> new NotFoundException("Parent comment not found"));

		Post post = ps.findById(body.getPostId());
		User user = userService.getCurrentUser();
		UUID userId = user.getUserId();

		Comment reply = new Comment();
		reply.setPost(post);
		reply.setContent(body.getContent());
		reply.setDataCreazione(LocalDateTime.now());
		reply.setUsercommentId(userId);
		reply.setParentComment(parentComment);
		parentComment.getReplies().add(reply);
		commentRepo.save(reply);

		commentRepo.save(parentComment);

		return reply;
	}

	public List<CommentDTO> getAllFilteredCommentsAndByPost(UUID postId) {
		List<Comment> comments = commentRepo.findAllByPostId(postId);
		List<CommentDTO> filteredComments = new ArrayList<>();

		for (Comment comment : comments) {
			if (comment.getCommentId() != null || comment.getParentComment() == null) {
				CommentDTO commentDTO = new CommentDTO();
				commentDTO.setCommentId(comment.getCommentId());
				commentDTO.setContent(comment.getContent());
				commentDTO.setDataCreazione(comment.getDataCreazione());
				commentDTO.setUsercommentId(comment.getUsercommentId());
				commentDTO.setPostId(comment.getPost().getPostId());

				// Aggiungi solo le informazioni necessarie delle risposte
				List<Comment> replies = comment.getReplies();
				List<CommentDTO> replyDTOs = new ArrayList<>();
				for (Comment reply : replies) {
					CommentDTO replyDTO = new CommentDTO();
					replyDTO.setCommentId(reply.getCommentId());
					replyDTO.setContent(reply.getContent());
					replyDTO.setDataCreazione(reply.getDataCreazione());
					replyDTO.setUsercommentId(reply.getUsercommentId());
					replyDTO.setPostId(reply.getPost().getPostId());

					// Aggiungi le risposte alle risposte se presenti
					List<Comment> replyReplies = reply.getReplies();
					List<CommentDTO> replyReplyDTOs = new ArrayList<>();
					for (Comment replyReply : replyReplies) {
						CommentDTO replyReplyDTO = new CommentDTO();
						replyReplyDTO.setCommentId(replyReply.getCommentId());
						replyReplyDTO.setContent(replyReply.getContent());
						replyReplyDTO.setDataCreazione(replyReply.getDataCreazione());
						replyReplyDTO.setUsercommentId(replyReply.getUsercommentId());
						replyReplyDTO.setPostId(replyReply.getPost().getPostId());

						replyReplyDTOs.add(replyReplyDTO);
					}
					replyDTO.setReplies(replyReplyDTOs);

					replyDTOs.add(replyDTO);
				}
				commentDTO.setReplies(replyDTOs);

				filteredComments.add(commentDTO);
			}
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

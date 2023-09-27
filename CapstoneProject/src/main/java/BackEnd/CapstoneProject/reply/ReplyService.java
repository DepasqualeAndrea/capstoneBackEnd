package BackEnd.CapstoneProject.reply;

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
import BackEnd.CapstoneProject.Post.PostRepository;
import BackEnd.CapstoneProject.Post.PostService;
import BackEnd.CapstoneProject.User.User;
import BackEnd.CapstoneProject.User.UserRepo;
import BackEnd.CapstoneProject.User.UserService;
import BackEnd.CapstoneProject.comments.Comment;
import BackEnd.CapstoneProject.comments.CommentRepo;

@Service
public class ReplyService {
	private final PostRepository postRepo;
	private final UserService userService;
	private final UserRepo userRepo;
	private final CommentRepo commentRepo;
	private final PostService ps;
	private final ReplyRepo replyRepo;

	@Autowired
	public ReplyService(ReplyRepo replyRepo, PostRepository postRepo, UserService userService, UserRepo userRepo,
			@Lazy PostService ps, CommentRepo commentRepo) {
		this.postRepo = postRepo;
		this.userService = userService;
		this.userRepo = userRepo;
		this.commentRepo = commentRepo;
		this.ps = ps;
		this.replyRepo = replyRepo;
	}

	public Reply createReply(UUID commentId, Reply body) {
		User user = userService.getCurrentUser();
		UUID userId = user.getUserId();

		Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found")); // specifico

		Reply reply = new Reply();
		reply.setContent(body.getContent());
		reply.setDataCreazione(LocalDateTime.now());
		reply.setUsercommentId(userId);
		reply.setComment(comment);
		return replyRepo.save(reply);
	}

	public List<ReplyDTO> getRepliesByCommentId(UUID commentId) {
		List<Reply> replies = replyRepo.findAllByCommentCommentId(commentId);

		List<ReplyDTO> replyDTOs = new ArrayList<>();
		for (Reply reply : replies) {
			ReplyDTO replyDTO = new ReplyDTO();
			replyDTO.setReplyId(reply.getRepliesId());
			replyDTO.setContent(reply.getContent());
			replyDTO.setDataCreazione(reply.getDataCreazione());
			replyDTO.setUserReplyId(userService.getCurrentUser().getUserId());
			replyDTO.setUsername(userService.getCurrentUser().getUsername());
			replyDTOs.add(replyDTO);
		}

		return replyDTOs;
	}

	public Reply getReplyById(UUID RepliesId) {
		return replyRepo.findById(RepliesId).orElseThrow(() -> new NotFoundException("Comment not found"));
	}

	public Page<Reply> find(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

		return replyRepo.findAll(pageable);
	}

	public Reply findById(UUID RepliesId) throws NotFoundException {
		return replyRepo.findById(RepliesId).orElseThrow(() -> new NotFoundException(RepliesId));
	}

	public Reply findByIdAndUpdate(UUID RepliesId, Reply body) throws NotFoundException {
		Reply found = this.findById(RepliesId);
		return replyRepo.save(found);
	}

//	public void deleteCommentById(UUID commentId) {
//
//		if (replyRepo.existsById(commentId)) {
//
//			replyRepo.deleteById(commentId);
//		} else {
//
//			throw new NotFoundException("Comment with ID " + commentId + " not found");
//		}
//	}

}
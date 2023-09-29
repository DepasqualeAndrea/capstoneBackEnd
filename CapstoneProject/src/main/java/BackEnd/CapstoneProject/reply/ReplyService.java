package BackEnd.CapstoneProject.reply;

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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import BackEnd.CapstoneProject.Exception.NotFoundException;
import BackEnd.CapstoneProject.Exception.PostNotFoundException;
import BackEnd.CapstoneProject.User.User;
import BackEnd.CapstoneProject.User.UserService;
import BackEnd.CapstoneProject.comments.Comment;
import BackEnd.CapstoneProject.comments.CommentRepo;
import jakarta.transaction.Transactional;

@Service
public class ReplyService {
	private final UserService userService;
	private final CommentRepo commentRepo;
	private final ReplyRepo replyRepo;

	@Autowired
	public ReplyService(ReplyRepo replyRepo, UserService userService, CommentRepo commentRepo) {
		this.userService = userService;
		this.commentRepo = commentRepo;
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
			replyDTO.setLikeCount(reply.getLikeCount());
			replyDTO.setLikedReplyByUsers(reply.getLikedReplyByUsers());
			replyDTO.setUserReplyId(userService.getCurrentUser().getUserId());
			replyDTO.setUsername(userService.getCurrentUser().getUsername());
			replyDTO.setUserReplyImage(userService.getCurrentUser().getProfileImageUrl());
			replyDTOs.add(replyDTO);
		}

		return replyDTOs;
	}

	@Transactional
	public void toggleLike(UUID repliesId, UUID userId) {
		Reply reply = replyRepo.findById(repliesId).orElseThrow(() -> new PostNotFoundException("Post not found"));

		Set<UUID> likedReplyByUsers = reply.getLikedReplyByUsers();
		if (likedReplyByUsers == null) {
			likedReplyByUsers = new HashSet<>();
		}

		if (likedReplyByUsers.contains(userId)) {

			likedReplyByUsers.remove(userId);
			reply.setLikeCount(reply.getLikeCount() - 1);
		} else {

			likedReplyByUsers.add(userId);
			reply.setLikeCount(reply.getLikeCount() + 1);
		}

		reply.setLikedReplyByUsers(likedReplyByUsers);
		replyRepo.save(reply);
	}

	public boolean isUserLikedReply(UUID repliesId, UUID userId) {
		Reply reply = replyRepo.findById(repliesId).orElse(null);

		return reply != null && reply.getLikedReplyByUsers() != null && reply.getLikedReplyByUsers().contains(userId);
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

	public void deleteReplyById(UUID commentId) {

		if (replyRepo.existsById(commentId)) {

			replyRepo.deleteById(commentId);
		} else {

			throw new NotFoundException("Comment with ID " + commentId + " not found");
		}
	}

}

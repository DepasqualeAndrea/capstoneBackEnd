package BackEnd.CapstoneProject.comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
	@Autowired
	CommentRepo commentRepo;
//
//	@Autowired
//	UserService userService;
//
//	public Comment saveComment(Comment comment) {
//		Date date = new Date();
//		long time = date.getTime();
//		Timestamp dateTime = new Timestamp(time);
//
//		comment.setCommentID(UUID.randomUUID());
//		comment.setTimestamp(dateTime);
//		return commentRepo.save(comment);
//	}
//
//	public ArrayList<Comment> getAllComment(UUID postID) {
//		ArrayList<Comment> result = new ArrayList<Comment>();
//		result = commentRepo.findAllByPostID(postID);
//		return result;
//	}
}

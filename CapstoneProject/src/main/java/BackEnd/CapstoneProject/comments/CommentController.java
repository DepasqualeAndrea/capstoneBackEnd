package BackEnd.CapstoneProject.comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/commentService")
public class CommentController {
	@Autowired
	CommentService commentService;
//
//	@PostMapping("/save")
//	public Comment saveComment(@RequestBody Comment comment) {
//		return commentService.saveComment(comment);
//	}
//
//	@GetMapping("/getAllComments/{postID}")
//	public ArrayList<Comment> getAllComments(@PathVariable("postID") UUID postID) {
//		return commentService.getAllComment(postID);
//
//	}

}

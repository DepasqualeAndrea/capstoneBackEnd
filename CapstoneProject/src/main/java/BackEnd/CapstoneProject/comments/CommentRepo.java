package BackEnd.CapstoneProject.comments;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, UUID> {
	Comment save(Comment comment);

	ArrayList<Comment> findAllByPostID(UUID postID);
}

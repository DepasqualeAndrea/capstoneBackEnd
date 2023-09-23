package BackEnd.CapstoneProject.comments;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, UUID> {

	ArrayList<Comment> findAllByPostId(UUID postId);

	List<Comment> findByUserId(UUID userId);

	List<Comment> findAllByParentComment_CommentId(UUID parentCommentId);

}

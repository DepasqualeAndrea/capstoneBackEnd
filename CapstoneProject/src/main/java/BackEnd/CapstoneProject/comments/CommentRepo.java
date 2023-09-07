package BackEnd.CapstoneProject.comments;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, UUID> {

//	ArrayList<Comment> findAllByPostID(UUID postId);
}

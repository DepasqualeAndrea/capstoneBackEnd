package BackEnd.CapstoneProject.comments;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, UUID> {

	ArrayList<Comment> findAllByPostId(UUID postId);

	Optional<Comment> findByUsername(String username);

	List<Comment> findByUserId(UUID userId);

}

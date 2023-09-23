package BackEnd.CapstoneProject.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import BackEnd.CapstoneProject.comments.Comment;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
	Optional<Post> findById(UUID postId);

	Optional<Post> findByDescription(String description);

	List<Post> findByUserId(UUID userId);

	Page<Post> findAllByOrderByDatacreazioneDesc(Pageable pageable);

	void deleteByComments(Comment comments);

	@Query("SELECT COUNT(c) FROM Comment c WHERE c.commentId = :commentId")
	Long countCommentsByCommentId(UUID commentId);
}

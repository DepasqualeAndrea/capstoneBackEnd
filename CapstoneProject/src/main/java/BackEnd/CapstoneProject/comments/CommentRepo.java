package BackEnd.CapstoneProject.comments;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import BackEnd.CapstoneProject.Post.Post;
import BackEnd.CapstoneProject.User.User;

@Repository
public interface CommentRepo extends JpaRepository<Comment, UUID> {

	@Query("SELECT c FROM Comment c WHERE c.commentId = :commentId")
	Comment findByCommentId(@Param("commentId") UUID commentId);

	@Query("SELECT COUNT(p) FROM Post p WHERE :commentId MEMBER OF p.comments")
	long countReferencedComments(@Param("commentId") UUID commentId);

	@Query("SELECT c FROM Comment c WHERE c.post.postId = :postId")
	List<Comment> findAllByPostId(@Param("postId") UUID postId);

	@Query("SELECT u FROM User u WHERE u.userId = :userId")
	User findByUserId(@Param("userId") UUID userId);

	@Query("SELECT c FROM Comment c WHERE c.user.id = :userId")
	List<Comment> findCommentsByUserId(@Param("userId") UUID userId);

	List<Comment> findByPost(Post post);

	Comment deleteCommentByCommentId(UUID commentId);

}

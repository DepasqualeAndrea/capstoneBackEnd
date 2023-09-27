package BackEnd.CapstoneProject.reply;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepo extends JpaRepository<Reply, UUID> {
	@Query("SELECT r FROM Reply r WHERE r.comment.commentId = :commentId")
	List<Reply> findAllByCommentCommentId(@Param("commentId") UUID commentId);
}

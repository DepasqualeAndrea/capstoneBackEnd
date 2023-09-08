package BackEnd.CapstoneProject.Likes;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, UUID> {

	int countByPostId(UUID postId);

	boolean existsByUserIdAndPostId(UUID userId, UUID postId);

}

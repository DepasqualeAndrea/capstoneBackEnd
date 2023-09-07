package BackEnd.CapstoneProject.Post;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
	Optional<Post> findById(UUID postid);

	Optional<Post> findByDescription(String description);

}

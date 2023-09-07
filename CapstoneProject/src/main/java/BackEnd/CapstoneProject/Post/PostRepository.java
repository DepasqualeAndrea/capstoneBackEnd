package BackEnd.CapstoneProject.Post;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
	ArrayList<Post> findAll();

	@SuppressWarnings("unchecked")
	Post save(Post post);

	void deleteById(UUID postID);
}

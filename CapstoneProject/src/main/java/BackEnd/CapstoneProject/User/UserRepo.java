package BackEnd.CapstoneProject.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import BackEnd.CapstoneProject.comments.Comment;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
	Optional<User> findByEmail(String email);

	Optional<User> findByNome(String nome);

	Optional<User> findByUsername(String username);

	@Query("SELECT u FROM User u JOIN u.posts p WHERE p.postId = :postId")
	List<User> findByPostsPostId(@Param("postId") UUID postId);

	@Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.followers LEFT JOIN FETCH u.following")
	List<User> findAllUsersWithDetails();

	void deleteByComment(Comment comment);

}

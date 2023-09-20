package BackEnd.CapstoneProject.User;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
	Optional<User> findByEmail(String email);

	Optional<User> findByNome(String nome);

	Optional<User> findByUsername(String username);

	// Query personalizzata per ottenere utenti con post ordinati per dataCreazione
	@Query("SELECT DISTINCT u FROM User u LEFT JOIN u.post p ORDER BY p.datacreazione DESC")
	Page<User> findAllWithPostsOrderedByDataCreazione(Pageable pageable);
}

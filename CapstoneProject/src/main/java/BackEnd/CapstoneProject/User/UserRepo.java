package BackEnd.CapstoneProject.User;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepo  extends JpaRepository<User, UUID> {
	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);
}

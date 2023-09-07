package BackEnd.CapstoneProject.status;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepo extends JpaRepository<Status, UUID> {
	@SuppressWarnings("unchecked")
	Status save(Status status);

	ArrayList<Status> findAll();
}

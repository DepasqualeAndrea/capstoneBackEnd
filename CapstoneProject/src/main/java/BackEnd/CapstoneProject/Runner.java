package BackEnd.CapstoneProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import BackEnd.CapstoneProject.Post.PostRepository;
import BackEnd.CapstoneProject.User.UserRepo;
import BackEnd.CapstoneProject.User.UserService;

@Component
public class Runner implements CommandLineRunner {
	@Autowired
	private UserRepo us;
	@Autowired
	private PostRepository ps;
	@Autowired
	private UserService uS;

	@Override
	public void run(String... args) throws Exception {
//		User user = new User("Emanuele", "Murru", "BavariCoddai", "Tigoddiri@live.it", "zeosudendidesopracciglioso",
//				Ruolo.ADMIN);
//		// us.save(user);
//		User userId = uS.findById(user.getUserId());
//		Post post = new Post(LocalDate.now(), "io amo la sardegna", "www.formaggioDiCapra.com", userId);
//		// ps.save(post);
	}

}

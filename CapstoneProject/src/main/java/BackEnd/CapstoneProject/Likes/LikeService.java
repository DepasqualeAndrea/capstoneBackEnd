package BackEnd.CapstoneProject.Likes;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import BackEnd.CapstoneProject.Post.Post;
import BackEnd.CapstoneProject.Post.PostRepository;
import BackEnd.CapstoneProject.User.User;
import BackEnd.CapstoneProject.User.UserRepo;
import BackEnd.CapstoneProject.User.UserService;

@Service
public class LikeService {
	private final PostRepository postRepo;
	private final UserService userService;
	private final UserRepo userRepo;
	private final LikeRepository likeRepo;

	@Autowired
	public LikeService(PostRepository postRepo, UserService userService, UserRepo userRepo, LikeRepository likeRepo) {
		this.postRepo = postRepo;
		this.userService = userService;
		this.userRepo = userRepo;
		this.likeRepo = likeRepo;
	}

	@SuppressWarnings("unchecked")
	public Like addLike(Like body) {
		Like like = new Like();
		like.setUserId(userService.getCurrentUser().getUserId());
		like.setTimestamp(LocalDate.now());
		like.setTargetType(body.getTargetType());
		User user = userService.getCurrentUser();
		user.getPost().addAll((Collection<? extends Post>) like);
		like = likeRepo.save(like);
		userRepo.save(user);
		return like;
	}

	// Metodo per rimuovere un "like" da un post
	public void removeLike(UUID likeId) {
		likeRepo.deleteById(likeId);
	}

	// Metodo per recuperare il conteggio di "like" per un post specifico
	public int getLikeCountForPost(UUID postId) {
		return likeRepo.countByPostId(postId);
	}

	// Metodo per verificare se un utente ha messo "like" a un post
	public boolean hasUserLikedPost(UUID userId, UUID postId) {
		return likeRepo.existsByUserIdAndPostId(userId, postId);
	}
}

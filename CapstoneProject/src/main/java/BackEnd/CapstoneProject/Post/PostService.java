package BackEnd.CapstoneProject.Post;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
	@Autowired
	PostRepository postRepo;

	public ArrayList<Post> submitPostToDB(Post postData) {

		Date date = new Date();
		long time = date.getTime();
		Timestamp dateTime = new Timestamp(time);

		postData.setPostID(UUID.randomUUID());
		postData.setLikes(0);
		postData.setTimeStamp(dateTime);

		postRepo.save(postData);
		ArrayList<Post> result = retrivePostFromDB();
		return result;
	}

	public ArrayList<Post> retrivePostFromDB() {
		ArrayList<Post> result = postRepo.findAll();
		return result;
	}

	public ArrayList<Post> deletePostFromDB(UUID postID) {
		postRepo.deleteById(postID);

		ArrayList<Post> result = retrivePostFromDB();
		return result;
	}
}

package BackEnd.CapstoneProject.Cloudinary;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CloudinaryImageService {
	public Map upload(MultipartFile file);
}

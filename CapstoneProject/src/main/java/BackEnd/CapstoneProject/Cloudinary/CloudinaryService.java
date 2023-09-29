package BackEnd.CapstoneProject.Cloudinary;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService implements CloudinaryImageService {

	@Autowired
	private Cloudinary cloudinary;

	@SuppressWarnings("rawtypes")
	@Override
	public Map upload(MultipartFile file) {

		try {
			Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
			return data;
		} catch (IOException e) {
			throw new RuntimeException("image uploading failed");

		}

	}

	public String uploadImageToCloudinary(MultipartFile imageFile) {
		try {
			Map<?, ?> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
			return uploadResult.get("url").toString();
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
	}

	public String uploadImage(MultipartFile imageFile) throws IOException {
		@SuppressWarnings("unchecked")
		Map<String, Object> params = ObjectUtils.asMap("folder", "your-folder-name");

		Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), params);
		return (String) uploadResult.get("secure_url");
	}
}

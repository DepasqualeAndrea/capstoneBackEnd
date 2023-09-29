package BackEnd.CapstoneProject.Cloudinary;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user/cloudinary/upload")
public class CloudinaryController {
	@Autowired
	CloudinaryService cloudinaryService;

	@PostMapping
	public ResponseEntity<Map> uploadImage(@RequestParam("image") MultipartFile file) {
		Map data = this.cloudinaryService.upload(file);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

}

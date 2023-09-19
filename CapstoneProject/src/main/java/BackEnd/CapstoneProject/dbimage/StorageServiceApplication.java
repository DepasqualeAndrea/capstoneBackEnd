package BackEnd.CapstoneProject.dbimage;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import BackEnd.CapstoneProject.User.UserService;

@RestController
@RequestMapping("/user/image")
public class StorageServiceApplication {
	@Autowired
	private StorageService service;
	@Autowired
	private UserService utenteService;

	@PostMapping
	public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
		String uploadImage = service.uploadImage(file);
		return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
	}

	@GetMapping("/{fileName}")
	public ResponseEntity<?> downloadImage(@PathVariable String fileName) {
		byte[] imageData = service.downloadImage(fileName);
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);

	}

//	@GetMapping("/current")
//	public ResponseEntity<byte[]> getCurrentUserImage() {
//		// Ottieni l'utente corrente (ad esempio, da un'informazione di autenticazione).
//		User currentUser = utenteService.getCurrentUser();
//
//		if (currentUser != null && currentUser.getImagedata() != null) {
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.IMAGE_JPEG); // Imposta il tipo di contenuto dell'intestazione sulla giusta
//															// immagine.
//
//			return new ResponseEntity<>(currentUser.getImagedata(), headers, HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Gestisci l'utente o l'immagine non trovati.
//		}
//	}

//	@PostMapping("/fileSystem")
//	public ResponseEntity<?> uploadImageToFIleSystem(@RequestParam("image") MultipartFile file) throws IOException {
//		String uploadImage = service.uploadImageToFileSystem(file);
//		return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
//	}
//
//	@GetMapping("/fileSystem/{fileName}")
//	public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
//		byte[] imageData = service.downloadImageFromFileSystem(fileName);
//		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);
//
//	}

}

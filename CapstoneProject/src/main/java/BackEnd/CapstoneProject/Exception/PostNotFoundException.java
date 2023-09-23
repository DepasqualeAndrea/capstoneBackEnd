package BackEnd.CapstoneProject.Exception;

import java.util.UUID;

@SuppressWarnings("serial")
public class PostNotFoundException extends RuntimeException {
	public PostNotFoundException(String message) {
		super(message);
	}

	public PostNotFoundException(UUID id) {
		super(id + " non trovato!");
	}
}

package BackEnd.CapstoneProject.Exception;

import java.util.UUID;

@SuppressWarnings("serial")
public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(UUID id) {
		super(id + " non trovato!");
	}
}

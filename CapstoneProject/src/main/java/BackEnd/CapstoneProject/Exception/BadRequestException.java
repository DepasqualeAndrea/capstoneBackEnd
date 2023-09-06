package BackEnd.CapstoneProject.Exception;

@SuppressWarnings("serial")
public class BadRequestException extends RuntimeException {
	public BadRequestException(String message) {
		super(message);
	}
}

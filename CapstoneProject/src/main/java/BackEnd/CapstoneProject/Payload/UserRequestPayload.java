package BackEnd.CapstoneProject.Payload;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import BackEnd.CapstoneProject.User.Ruolo;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserRequestPayload {
	@NotNull(message = "Il campo nome è obbligatorio")
	@Size(min = 4, max = 20, message = "Nome deve avere minimo 3 caratteri, massimo 30")
	private String nome;
	@NotNull(message = "Il campo cognome è obbligatorio")
	private String cognome;
	@NotNull(message = "Il campo username è obbligatorio")
	private String username;
	@NotNull(message = "Inserisci una email valida, quest campo non può rimanere vuoto")
	@Email(message = "L'email inserita non è un indirizzo valido")
	private String email;
	@NotNull(message = "La password è obbligatoria")
	private String password;
	private String Bio;
	@Column(length = 16)
	private String genere;
	@Column(length = 128)
	private String Citta;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDate dataDiNascita;
	private Ruolo role;
}

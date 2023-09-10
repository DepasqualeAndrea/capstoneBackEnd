package BackEnd.CapstoneProject.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import BackEnd.CapstoneProject.Likes.Like;
import BackEnd.CapstoneProject.Post.Post;
import BackEnd.CapstoneProject.comments.Comment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Utenti")
@Data
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({ "password", "accountNonExpired", "authorities", "credentialsNonExpired", "accountNonLocked" })
public class User implements UserDetails {
	@Id
	@GeneratedValue
	private UUID userId;
	private String nome;
	private String cognome;
	private String username;
	@Column(nullable = true, unique = true)
	private String email;
	private String password;
	private String Imageprofile;
	@Enumerated(EnumType.STRING)
	private Ruolo role;

	@OneToMany
	private List<Post> post = new ArrayList<>();
	@OneToMany
	private List<Comment> comment = new ArrayList<>();
	@OneToMany
	private List<Like> like = new ArrayList<>();

	public User(String nome, String cognome, String username, String Imageprofile, String email, String password,
			Ruolo role) {
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.Imageprofile = Imageprofile;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}

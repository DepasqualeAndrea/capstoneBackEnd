package BackEnd.CapstoneProject.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import BackEnd.CapstoneProject.Likes.Like;
import BackEnd.CapstoneProject.Post.Post;
import BackEnd.CapstoneProject.comments.Comment;
import BackEnd.CapstoneProject.dbimage.ImageData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Utenti")
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
	@JsonIgnore
	private String password;

	@Enumerated(EnumType.STRING)
	private Ruolo role;

	@OneToMany(fetch = FetchType.EAGER)
	@JsonIgnore
	private List<ImageData> imagedata = new ArrayList<>();
	@OneToMany(fetch = FetchType.EAGER)
	private List<Post> post = new ArrayList<>();
	@OneToMany(fetch = FetchType.EAGER)
	private List<Comment> comment = new ArrayList<>();
	@OneToMany(fetch = FetchType.EAGER)
	private List<Like> like = new ArrayList<>();

	public User(List<ImageData> list, String nome, String cognome, String username, String email, String password,
			Ruolo role) {
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = Ruolo.USER;
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

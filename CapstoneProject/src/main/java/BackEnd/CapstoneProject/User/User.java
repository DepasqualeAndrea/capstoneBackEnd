package BackEnd.CapstoneProject.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import BackEnd.CapstoneProject.Post.Post;
import BackEnd.CapstoneProject.comments.Comment;
import BackEnd.CapstoneProject.dbimage.ImageData;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
@JsonIgnoreProperties({ "password", "accountNonExpired", "authorities", "credentialsNonExpired", "accountNonLocked" })
public class User implements UserDetails {
	@Id
	@GeneratedValue
	private UUID userId;
	@Column(nullable = true, unique = false, length = 64)
	private String nome;
	@Column(nullable = true, unique = false, length = 64)
	private String cognome;
	@Column(nullable = true, unique = true, length = 64)
	private String username;
	@Column(nullable = true, unique = true, length = 64)
	private String email;
	@Column(length = 256, nullable = false)
	@JsonIgnore
	private String password;
	@Column(length = 200)
	private String bio;
	@Column(length = 16)
	private String genere;
	@Column(length = 128)
	private String Citta;
	@Enumerated(EnumType.STRING)
	private Ruolo role;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDate dataDiNascita;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDate dataRegistrazione;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDate dataUltimeModifiche;

	@OneToOne(fetch = FetchType.EAGER)
	private ImageData imagedata;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Post> post = new ArrayList<>();
	@OneToMany(fetch = FetchType.EAGER)
	private List<Comment> comment = new ArrayList<>();
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "follow_users", joinColumns = @JoinColumn(name = "followed_id"), inverseJoinColumns = @JoinColumn(name = "follower_id"))
	private List<User> followerUsers = new ArrayList<>();
	@JsonIgnore
	@ManyToMany(mappedBy = "followerUsers")
	private List<User> followingUsers = new ArrayList<>();

	public User(ImageData img, String nome, String cognome, String username, String email, String password, Ruolo role,
			LocalDate dataRegistrazione) {
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

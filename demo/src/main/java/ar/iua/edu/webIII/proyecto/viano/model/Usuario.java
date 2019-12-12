package ar.iua.edu.webIII.proyecto.viano.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.iua.edu.webIII.proyecto.viano.model.dto.UsuarioDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@NamedNativeQueries({
	@NamedNativeQuery(
			name="listadoUsuariosDTO",
			query="SELECT username, email FROM usuarios WHERE enabled=1 ORDER BY email",
			resultSetMapping = "usuarioSinteticoMapper"
	)	
})

@SqlResultSetMappings({
	@SqlResultSetMapping(
			name="usuarioSinteticoMapper",
			classes= {
					@ConstructorResult(
							targetClass = UsuarioDTO.class,
							columns= {
									@ColumnResult(name="username", type=String.class),
									@ColumnResult(name="email", type=String.class)
									
							}
					)
			}
	)
})

@Cacheable
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 6554265456180416900L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 300, nullable = false)
	private String password;

	@Column(length = 200, unique = true, nullable = false)
	private String username;
	@Column(length = 250, unique = true, nullable = false)
	private String email;

	private String firstName;
	private String lastName;
	private int version;

	@Column(columnDefinition = "tinyint default 0")
	private boolean accountNonExpired;
	@Column(columnDefinition = "tinyint default 0")
	private boolean accountNonLocked;
	@Column(columnDefinition = "tinyint default 0")
	private boolean credentialsNonExpired;
	@Column(columnDefinition = "tinyint default 0")
	private boolean enabled;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuarios_roles", joinColumns = {
			@JoinColumn(name = "idUsuario", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "idRol", referencedColumnName = "id") })
	private Set<Rol> roles=new HashSet<Rol>();

	public Set<Rol> getRoles() {
		return roles;
	}

	private void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> r = getRoles().stream().map(rol -> new SimpleGrantedAuthority(rol.getRol()))
				.collect(Collectors.toList());

		// List<GrantedAuthority> r=new ArrayList<GrantedAuthority>();
		// for(Rol rol:getRoles())
		// r.add(new SimpleGrantedAuthority(rol.getRol()));
		return r;
	}
	/**En caso de cambiar algun tipo de dato sensible, debo pasar todos los datos sensibles y incrementar la version de la cuenta en 1**/
	public void changeSensitiveData(String username, String email, boolean accountNotExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled,
									Set<Rol> roles, String password, int version){
		setUsername(username);
		setEmail(email);
		setAccountNonExpired(accountNotExpired);
		setAccountNonLocked(accountNonLocked);
		setCredentialsNonExpired(credentialsNonExpired);
		setEnabled(enabled);
		setRoles(roles);
		setPassword(password);
		setVersion(version+1);

	}
	public int getVersion() {
		return version;
	}

	private void setVersion(int version) {
		this.version = version;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	private void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	private void setPassword(String password) {
		this.password = password;
	}

	private void setUsername(String username) {
		this.username = username;
	}

	private void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	private void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	private void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	private void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	@Transient
	private String sessionToken;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

}

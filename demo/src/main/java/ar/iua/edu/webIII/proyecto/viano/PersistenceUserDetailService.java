package ar.iua.edu.webIII.proyecto.viano;

import ar.iua.edu.webIII.proyecto.viano.business.exceptions.BusinessException;
import ar.iua.edu.webIII.proyecto.viano.business.IUsuarioBusiness;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.NotFoundException;
import ar.iua.edu.webIII.proyecto.viano.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersistenceUserDetailService implements UserDetailsService {

@Autowired
private PasswordEncoder pe;

@Autowired
private IUsuarioBusiness usuarioService;
private Logger log = LoggerFactory.getLogger(this.getClass());
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*
		Usuario u=new Usuario();
		u.setUsername(username);
		u.setPassword(pe.encode("123"));
		u.setEnabled(true);
		u.setAccountNonExpired(true);
		u.setAccountNonLocked(true);
		u.setCredentialsNonExpired(true);
		*/
		
		Usuario u;
		try {
			u = usuarioService.load(username);
		} catch (BusinessException e) {
			log.error(e.getMessage(),e);
			throw new RuntimeException();
		} catch (NotFoundException e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
		
		return u;
	}

}

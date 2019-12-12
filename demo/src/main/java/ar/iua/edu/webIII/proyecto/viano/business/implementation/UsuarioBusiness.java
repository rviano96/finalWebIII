package ar.iua.edu.webIII.proyecto.viano.business.implementation;

import ar.iua.edu.webIII.proyecto.viano.business.IUsuarioBusiness;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.BusinessException;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.InvalidArgumentException;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.NotFoundException;
import ar.iua.edu.webIII.proyecto.viano.model.Rol;
import ar.iua.edu.webIII.proyecto.viano.model.Usuario;
import ar.iua.edu.webIII.proyecto.viano.persistence.RolRepository;
import ar.iua.edu.webIII.proyecto.viano.persistence.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class UsuarioBusiness implements IUsuarioBusiness {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UsuarioRepository usuarioDAO;

    @Autowired
    private PasswordEncoder pswEncoder;
    @Autowired
    private RolRepository rolDAO;

    @Override
    public Usuario load(String username) throws BusinessException, NotFoundException {
        List<Usuario> ou;
        try {
            ou = usuarioDAO.findByUsernameOrEmail(username, username);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        if (ou.size() == 0) {
            log.error("No se encuentra el usuario con nombre o email " + username);
            throw new NotFoundException("No se encuentra el usuario con nombre o email " + username);

        }
        return ou.get(0);
    }

    @Override
    public Usuario save(Usuario usuario) throws BusinessException {
        try {
            return usuarioDAO.save(usuario);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public Usuario update(String field, String password, Object newValue, String name) throws BusinessException, NotFoundException, InvalidArgumentException {
        List<Usuario> ou = null;
        try {
            ou = usuarioDAO.findByUsernameOrEmail(name, name);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        if (ou.isEmpty()) {
            log.error("No se encuentra ningun usuario con nombre o email " + name);
            throw new NotFoundException("No se encuentra ningun usuario con nombre o email " + name);
        }
        if (!pswEncoder.matches(password, ou.get(0).getPassword())) {
            log.error("Ingrese una contrasena correcta");
            throw new InvalidArgumentException("Ingrese una contrasena correcta");
        }
        Usuario u = ou.get(0);
        // los campos que se pueden modificar son password, email, estados de la cuenta, roles
        //String username, String email, boolean accountNotExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled,
        //									Set<Rol> roles, String password, int version
        switch (field) {
            case "email":
                if (newValue instanceof String) {
                    u.changeSensitiveData(u.getUsername(), newValue.toString().toLowerCase(), u.isAccountNonExpired(), u.isAccountNonLocked(), u.isCredentialsNonExpired(), u.isEnabled(),
                            u.getRoles(), u.getPassword(), u.getVersion());
                } else {
                    log.error("El parametro " + newValue + " debe ser string");
                    throw new InvalidArgumentException("El parametro" + newValue + " debe ser string");
                }
                break;
            case "password":
                if (newValue instanceof String) {
                    u.changeSensitiveData(u.getUsername(), u.getEmail(), u.isAccountNonExpired(), u.isAccountNonLocked(), u.isCredentialsNonExpired(), u.isEnabled(),
                            u.getRoles(), pswEncoder.encode(newValue.toString()), u.getVersion());
                } else {
                    log.error("El parametro " + newValue + " debe ser string");
                    throw new InvalidArgumentException("El parametro" + newValue + " debe ser string");
                }
                break;
            case "enabled":
                if (newValue instanceof Boolean) {
                    u.changeSensitiveData(u.getUsername(), u.getEmail(), u.isAccountNonExpired(), u.isAccountNonLocked(), u.isCredentialsNonExpired(), ((Boolean) newValue).booleanValue(),
                            u.getRoles(), u.getPassword(), u.getVersion());
                } else {
                    log.error("El parametro " + newValue + " debe ser boolean");
                    throw new InvalidArgumentException("El parametro" + newValue + " debe ser boolean");
                }
                break;
            case "accountLocked":
                if (newValue instanceof Boolean) {
                    u.changeSensitiveData(u.getUsername(), u.getEmail(), u.isAccountNonExpired(), ((Boolean) newValue).booleanValue(), u.isCredentialsNonExpired(), u.isEnabled(),
                            u.getRoles(), u.getPassword(), u.getVersion());
                } else {
                    log.error("El parametro " + newValue + " debe ser boolean");
                    throw new InvalidArgumentException("El parametro" + newValue + " debe ser boolean");
                }
                break;
            case "credentialsExpired":
                if (newValue instanceof Boolean) {
                    u.changeSensitiveData(u.getUsername(), u.getEmail(), u.isAccountNonExpired(), u.isAccountNonLocked(), ((Boolean) newValue).booleanValue(), u.isEnabled(),
                            u.getRoles(), u.getPassword(), u.getVersion());
                } else {
                    log.error("El parametro " + newValue + " debe ser boolean");
                    throw new InvalidArgumentException("El parametro" + newValue + " debe ser boolean");
                }
                break;
            case "roles":

                if (newValue instanceof List) {
                    Set<Rol> list = new HashSet<Rol>();
                    for (int i = 0; i < ((List) newValue).size(); i++) {
                        List<Rol> l = rolDAO.findByRol(((List) newValue).get(i).toString());
                        if (l.size() == 0) {
                            log.error("El rol " + ((List) newValue).get(i).toString() + " no existe");
                            throw new InvalidArgumentException("El rol " + ((List) newValue).get(i).toString() + " no existe");
                        }
                        list.add(l.get(0));
                    }
                    u.changeSensitiveData(u.getUsername(), u.getEmail(), u.isAccountNonExpired(), u.isAccountNonLocked(), u.isCredentialsNonExpired(), u.isEnabled(),
                            list, u.getPassword(), u.getVersion());
                } else {
                    log.error("El parametro " + newValue + " debe ser una lista");
                    throw new InvalidArgumentException("El parametro " + newValue + " debe ser una lista");
                }
                break;
            default:
                log.error("Solo se pueden modificar: password, email, estados de la cuenta, roles ");
                throw new InvalidArgumentException("Solo se pueden modificar: password, email, estados de la cuenta, roles ");
        }
        try {
            return usuarioDAO.save(u);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(e);
        }
    }


}

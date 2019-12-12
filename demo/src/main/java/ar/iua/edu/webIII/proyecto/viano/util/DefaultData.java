package ar.iua.edu.webIII.proyecto.viano.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.iua.edu.webIII.proyecto.viano.model.Rol;
import ar.iua.edu.webIII.proyecto.viano.persistence.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ar.iua.edu.webIII.proyecto.viano.model.Usuario;
import ar.iua.edu.webIII.proyecto.viano.persistence.RolRepository;

@Service
public class DefaultData {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioRepository usuarioDAO;

    @Autowired
    private RolRepository rolDAO;

    public Usuario ensureUserAdmin() {
        Usuario u;
        List<Usuario> l = usuarioDAO.findByUsername("rodrigoAdmin");
        List<Rol> roles = new ArrayList<>();
        if (l.size() == 0) {
            roles.add(ensureRoleAdmin());
            saveUser("rodrigoAdmin", roles, "rodrigo_admin@mail.com", "password", "Viano", "Rodrigo");

        }
        roles = new ArrayList<>();
         l = usuarioDAO.findByUsername("rodrigoUser");
        if (l.size() == 0) {
            roles.add(ensureRoleUser());
            saveUser("rodrigoUser", roles, "rodrigo_user@mail.com", "password", "Viano", "Rodrigo User");

        }
        roles = new ArrayList<>();
         l = usuarioDAO.findByUsername("rodrigoUserAdmin");
        if (l.size() == 0) {
            roles.add(ensureRoleUser());
            roles.add(ensureRoleAdmin());
            u = saveUser("rodrigoUserAdmin", roles, "rodrigo_user_admin@mail.com", "password", "Viano", "Rodrigo User Admin");
        } else {
            u = l.get(0);
        }
        return u;
    }

    private Usuario saveUser(String username, List<Rol> rol, String email, String password, String lastName, String name) {
        Usuario r = new Usuario();
        //r.setUsername(username);
        r.setFirstName(name);
        r.setLastName(lastName);
        //r.setEmail(email);
        //r.setPassword(passwordEncoder.encode(password));
        //r.setEnabled(true);
        //r.setAccountNonExpired(true);
        //r.setAccountNonLocked(true);
        //r.setCredentialsNonExpired(true);

        Set<Rol> roles = new HashSet<Rol>();
        for (Rol rr: rol) {
            roles.add(rr);
        }

        //roles.add(ensureRoleUser());
        //r.setRoles(roles);
        r.changeSensitiveData(username, email, true, true, true, true, roles, passwordEncoder.encode(password), 0);
        return usuarioDAO.save(r);
    }

    public Rol ensureRoleAdmin() {
        return ensureRol("ROLE_ADMIN");
    }

    public Rol ensureRoleUser() {
        return ensureRol("ROLE_USER");
    }

    private Rol ensureRol(String role) {
        List<Rol> l = rolDAO.findByRol(role);
        if (l.size() == 0) {
            Rol r = new Rol();
            r.setRol(role);
            return rolDAO.save(r);
        } else {
            return l.get(0);

        }
    }

}

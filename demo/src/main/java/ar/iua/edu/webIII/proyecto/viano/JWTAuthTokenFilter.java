package ar.iua.edu.webIII.proyecto.viano;

import ar.iua.edu.webIII.proyecto.viano.business.exceptions.BusinessException;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.NotFoundException;
import ar.iua.edu.webIII.proyecto.viano.model.JWTAuth;
import ar.iua.edu.webIII.proyecto.viano.persistence.UsuarioRepository;
import ar.iua.edu.webIII.proyecto.viano.business.IJWTAuthService;
import ar.iua.edu.webIII.proyecto.viano.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class JWTAuthTokenFilter extends OncePerRequestFilter {
    private IJWTAuthService jwtAuthService;
    private UsuarioRepository usuariosDAO;
    private Logger log = LoggerFactory.getLogger(this.getClass());
    public static String AUTH_HEADER = "Authorization";
    public static String TOKEN_PREFIX = "Bearer ";
    public static String AUTH_HEADER_CUSTOM = "X-AUTH-TOKEN";
    public static String AUTH_HEADER1_CUSTOM = "XAUTHTOKEN";
    public static String AUTH_PARAMETER = "xauthtoken";
    public static String AUTH_PARAMETER1 = "token";

    public JWTAuthTokenFilter(IJWTAuthService jwtAuthService, UsuarioRepository usuariosDAO) {
        super();
        this.jwtAuthService = jwtAuthService;
        this.usuariosDAO = usuariosDAO;
    }

    // el formato de jwt son 3 campos separados por puntos --> xxxxx.yyyyyy.zzzz
    private boolean esValido(String valor) {
        return valor != null && valor.split("\\.").length == 3;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(AUTH_HEADER);

        //Si el header Authorization es null o si hay algun header del custom-token, hago el doFilter
        if (header == null || request.getHeader(AUTH_HEADER_CUSTOM) != null || request.getHeader(AUTH_HEADER1_CUSTOM) != null
                || request.getParameter(AUTH_PARAMETER) != null || request.getHeader(AUTH_PARAMETER1) != null) {
            chain.doFilter(request, response);
            return;
        }
        // Si el header Authorization no comienza con el prefijo o si no es valido, hago el doFilter
        if ( !header.startsWith(TOKEN_PREFIX) || !esValido(header.replace(TOKEN_PREFIX, ""))) {
            chain.doFilter(request, response);
            return;
        }

        // A partir de aquí, se considera que se envió el el token y es propritario, por
        // ende si no está ok, login inválido
        String token  = header.replace(TOKEN_PREFIX, "");
        log.trace("Token recibido por header=" + token);
        String username = null;
        JWTAuth jwtAuth = null;
        try {
            jwtAuth = jwtAuthService.load(token);
        } catch (NotFoundException e) {
            SecurityContextHolder.clearContext();
            //throw new ServletException("No existe el token=" + token);
            log.debug("No existe el token=" + token);
            chain.doFilter(request, response);
            return;
        } catch (BusinessException e) {
            SecurityContextHolder.clearContext();
            log.error(e.getMessage(), e);
            chain.doFilter(request, response);
            return;
        }

        username = jwtAuth.getUsernameFromToken(jwtAuth.getToken());
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<Usuario> lu = usuariosDAO.findByUsername(username);          // if token is valid configure Spring Security to manually set authentication
            if (lu.size() == 1) {
                if (jwtAuth.validateToken(token, lu.get(0))) {
                    log.trace("Token para usuario {} ({}) [{}]", lu.get(0).getUsername(), token, request.getRequestURI());
                    lu.get(0).setSessionToken(token);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(lu.get(0), null, lu.get(0).getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    try {
                        jwtAuthService.delete(jwtAuth);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        chain.doFilter(request, response);
                    }
                    SecurityContextHolder.clearContext();
                    log.debug("El Token " + token + " ha expirado");
                    //throw new ServletException("El Token ha expirado. Token=" + token);

                    chain.doFilter(request, response);
                    return;
                }
            } else {
                log.debug("No se encontró el usuario {} por token", username);
            }

            chain.doFilter(request, response);
        }

    }

}

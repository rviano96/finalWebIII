package ar.iua.edu.webIII.proyecto.viano;

import java.io.IOException;
import java.util.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.iua.edu.webIII.proyecto.viano.business.IAuthTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import ar.iua.edu.webIII.proyecto.viano.business.exceptions.BusinessException;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.NotFoundException;
import ar.iua.edu.webIII.proyecto.viano.model.AuthToken;
import ar.iua.edu.webIII.proyecto.viano.model.Usuario;
import ar.iua.edu.webIII.proyecto.viano.persistence.UsuarioRepository;


public class CustomTokenAuthenticationFilter extends OncePerRequestFilter {
    private Logger log = LoggerFactory.getLogger(this.getClass());


    private IAuthTokenService authTokenService;
    private UsuarioRepository usuariosDAO;
    public static String AUTH_HEADER = "X-AUTH-TOKEN";
    public static String AUTH_HEADER1 = "XAUTHTOKEN";
    public static String AUTH_PARAMETER = "xauthtoken";
    public static String AUTH_PARAMETER1 = "token";

    public CustomTokenAuthenticationFilter(IAuthTokenService authTokenService, UsuarioRepository usuariosDAO) {
        super();
        this.authTokenService = authTokenService;
        this.usuariosDAO = usuariosDAO;

    }


    //public static String ATTR_SESSION_NOT_CREATION = "ATTR_SESSION_NOT_CREATION";

    private boolean esValido(String valor) {
        return valor != null && valor.trim().length() > 10;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {



        String parameter = request.getParameter(AUTH_PARAMETER);
        if (!esValido(parameter)) {
            parameter = request.getParameter(AUTH_PARAMETER1);
        }
        String header = request.getHeader(AUTH_HEADER);
        if (!esValido(header)) {
            header = request.getHeader(AUTH_HEADER1);
        }
        if (!esValido(parameter) && !esValido(header) &&
                (request.getHeader("Authorization") == null && request.getHeader("authorization") == null)) {
            chain.doFilter(request, response);
            return;
        }
        if (!esValido(parameter) && !esValido(header)) {
            chain.doFilter(request, response);
            return;
        }
        String token = "";
        if (esValido(parameter)) {
            token = parameter;
            log.trace("Token recibido por query param=" + token);
        } else {
            token = header;
            log.trace("Token recibido por header=" + token);
        }
        String[] tokens = null;
        /*if (request.getHeader("Authorization") != null || request.getHeader("authorization") != null) {
            request.putHeader("Authorization", "");
            request.putHeader("authorization", "");

        }*/
        try {
            tokens = AuthToken.decode(token);
            if (tokens.length != 2) {

                chain.doFilter(request, response);
                return;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            chain.doFilter(request, response);
            return;
        }

        // A partir de aquí, se considera que se envió el el token y es propritario, por
        // ende si no está ok, login inválido
        AuthToken authToken = null;
        try {
            authToken = authTokenService.load(tokens[0]);
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
            //throw new ServletException(e);
        }

        if (!authToken.valid()) {
            try {
                if (authToken.getType().equals(AuthToken.TYPE_DEFAULT)
                        || authToken.getType().equals(AuthToken.TYPE_TO_DATE)
                        || authToken.getType().equals(AuthToken.TYPE_REQUEST_LIMIT)) {
                    authTokenService.delete(authToken);
                }
                if (authToken.getType().equals(AuthToken.TYPE_FROM_TO_DATE)) {
                    if (authToken.getTo().getTime() < System.currentTimeMillis()) {
                        authTokenService.delete(authToken);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            SecurityContextHolder.clearContext();
            log.debug("El Token " + token + " ha expirado");
            //throw new ServletException("El Token ha expirado. Token=" + token);
            chain.doFilter(request, response);
            return;
        }
        try {
            authToken.setLast_used(new Date());
            authToken.addRequest();
            authTokenService.save(authToken);
            String username = authToken.getUsername();
            List<Usuario> lu = usuariosDAO.findByUsername(username);
            if (lu.size() == 1) {
                log.trace("Token para usuario {} ({}) [{}]", lu.get(0).getUsername(), token, request.getRequestURI());
                lu.get(0).setSessionToken(token);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(lu.get(0), null, lu.get(0).getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                //request.setAttribute(ATTR_SESSION_NOT_CREATION, "true");

            } else {
                log.debug("No se encontró el usuario {} por token", username);
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            chain.doFilter(request, response);
        }

    }


}
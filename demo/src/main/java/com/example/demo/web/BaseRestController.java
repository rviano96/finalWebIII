package com.example.demo.web;

import java.util.Calendar;
import java.util.Date;

import com.example.demo.business.IJWTAuthService;
import com.example.demo.model.JWTAuth;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.business.BusinessException;
import com.example.demo.business.IAuthTokenService;
import com.example.demo.model.AuthToken;
import com.example.demo.model.Usuario;

public class BaseRestController {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IAuthTokenService authTokenService;
    @Autowired
    private IJWTAuthService jwtAuthService;

    protected UserDetails getUserPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = null;
        if (auth.getPrincipal() instanceof UserDetails) {
            user = (UserDetails) auth.getPrincipal();
        }

        return user;
    }

    protected Usuario getUserLogged() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = null;
        if (auth.getPrincipal() instanceof Usuario) {
            usuario = (Usuario) auth.getPrincipal();
        }
        return usuario;
    }

    @Value("${app.session.token.timeout:86400}")
    private int sessionTimeout;

    protected JSONObject userToJson(Usuario u) {
        boolean isAdmin = false, isUser = false;
        AuthToken token = null;
        String tokenValue = null;
        JWTAuth jwtToken = null;
        for (GrantedAuthority g : u.getAuthorities()) {
            if (g.getAuthority().equals("ROLE_ADMIN"))
                isAdmin = true;
            if (g.getAuthority().equals("ROLE_USER"))
                isUser = true;
        }
        if (isAdmin) {
            token = new AuthToken(u.getUsername(),new Date(System.currentTimeMillis() + sessionTimeout) );
            tokenValue = token.encodeCookieValue();
            try {
                authTokenService.save(token);
            } catch (BusinessException e) {
                log.error(e.getMessage(), e);
            }
        }
        if (isUser) {
            jwtToken = new JWTAuth(sessionTimeout,u.getUsername());
            try {
                jwtAuthService.save(jwtToken);
            }catch (BusinessException e){
                log.error(e.getMessage(), e);
            }
        }

        JSONObject o = JSONBuilder(u);
        o.put("authtoken", tokenValue);
        if(jwtToken != null)
            o.put("jwtToken", jwtToken.getToken());
        return o;
    }

    protected JSONObject userToJsonJWT(Usuario u) {
        JWTAuth token = new JWTAuth(sessionTimeout, u.getUsername());
        try {
            jwtAuthService.save(token);
        } catch (BusinessException e) {
            log.error(e.getMessage(), e);
        }
        JSONObject o = JSONBuilder(u);
        o.put("authtoken", token.getToken());
        return o;
    }

    protected JSONObject userToJson(UserDetails u1) {
        Usuario u = (Usuario) u1;
        JSONObject o = JSONBuilder(u);
        return o;
    }

    protected JSONObject userToJsonMasToken(UserDetails u, String token) {
        JSONObject r = userToJson(u);
        r.put("token", token);
        return r;
    }

    protected ResponseEntity<Object> genToken(String username, int diasvalido) {
        try {
            AuthToken token = buildToken(username, diasvalido);
            return new ResponseEntity<Object>("{\"token\":\"" + token.encodeCookieValue() + "\"}", HttpStatus.OK);
        } catch (BusinessException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    protected AuthToken buildToken(String username, int diasvalido) throws BusinessException {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, diasvalido);
        AuthToken token = new AuthToken(username, c.getTime());
        authTokenService.save(token);
        return token;

    }

    private JSONObject JSONBuilder(Usuario u) {
        JSONObject o = new JSONObject();
        o.put("username", u.getUsername());
        o.put("fullname", u.getFirstName() + "," + u.getLastName());
        o.put("email", u.getEmail());
        o.put("code", 0);
        JSONArray r = new JSONArray();
        for (GrantedAuthority g : u.getAuthorities()) {
            r.put(g.getAuthority());
        }
        o.put("roles", r);
        return o;
    }
}

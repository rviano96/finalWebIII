package com.example.demo.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Entity
@Table(name = "jwt_auth")
public class JWTAuth implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    private int validitySeconds;

    @Ignore
    private static final String SECRET = "rodrigo";


    @Column(name = "hasta", columnDefinition = "datetime DEFAULT NULL")
    private Date to;

    @Ignore
    // @Column(columnDefinition = "datetime DEFAULT NULL")
    private Date last_used;

    private String token;

    public JWTAuth() {
    }

    public int getValiditySeconds() {
        return validitySeconds;
    }

    public void setValiditySeconds(int validitySeconds) {
        this.validitySeconds = validitySeconds;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*public Date getFrom() {
        return from;
    }*/

    /*public void setFrom(Date from) {
        this.from = from;
    }*/

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Date getLast_used() {
        return last_used;
    }

    public void setLast_used(Date last_used) {
        this.last_used = last_used;
    }

    public JWTAuth(int validitySeconds, String username) {
        setValiditySeconds(validitySeconds);
        setUsername(username);
        setTo(new Date(System.currentTimeMillis() + validitySeconds * 1000));
        setToken(generateToken(username));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }    //retrieve expiration date from jwt token

    public String getRoleFromToken(String token) {
         Claims claims = getAllClaimsFromToken(token);
        return claims.get("role").toString();
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }    //check if the token has expired

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }    //generate token for user

    private String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        // return doGenerateToken(claims, userDetails.getUsername());
        return doGenerateToken(claims, username);
    }    //while creating the token -

    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        claims.put("role", "user");
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validitySeconds * 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }    //validate token

    public Boolean validateToken(String token, Usuario u) {
        final String username = getUsernameFromToken(token);
        return (username.equals(u.getUsername()) && !isTokenExpired(token));
    }
}

package com.example.demo.model;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;

@Entity(name="requests_audit")
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @Column(name="moment", columnDefinition = "datetime DEFAULT NULL")
    private Date time;

    @Column(name="uri")
    private String uri;

    @Column(name="params", columnDefinition = "BLOB")
    @Lob
    private String parametros;

    @Column(name="headers", columnDefinition = "BLOB")
    @Lob
    private String cabeceras;

    @Column(name="type_of_token")
    private String typeOfToken;

    @Column(name="authorized")
    private String autorizado;

    private String rol;

    @Column(name="username")
    private String usuario;

    @Column(name="body", columnDefinition= "LONGBLOB  DEFAULT NULL")
    @Lob
    private byte[] body;

    @Column(name="method")
    private String metodo;

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public Audit() {
        this.body = null;
        this.cabeceras = "NONE";
        this.parametros = "NONE";
        this.uri = "NONE";
        this.typeOfToken = "NONE";
        this.autorizado = "NONE";
        this.usuario = "NONE";
        this.metodo = "NONE";
        this.rol = "NONE";

    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    public String getCabeceras() {
        return cabeceras;
    }

    public void setCabeceras(String cabeceras) {
        this.cabeceras = cabeceras;
    }

    public String getTypeOfToken() {
        return typeOfToken;
    }

    public void setTypeOfToken(String typeOfToken) {
        this.typeOfToken = typeOfToken;
    }

    public String getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(String autorizado) {
        this.autorizado = autorizado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }


}

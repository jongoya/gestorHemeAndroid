package com.example.gestorheme.Models.Login;

import java.io.Serializable;

public class LoginModel implements Serializable {
    private long comercioId = 0;
    private String usuario = "";
    private String password = "";
    private String token = "";
    private String nombre = "";
    private String nombre_dispositivo = "";

    public LoginModel(String usuario, String password, String nombre, String nombreDispositivo) {
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.nombre_dispositivo = nombreDispositivo;
    }

    public LoginModel() {

    }

    public long getComercioId() {
        return comercioId;
    }

    public void setComercioId(long comercioId) {
        this.comercioId = comercioId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreDispositivo() {
        return nombre_dispositivo;
    }

    public void setNombreDispositivo(String nombreDispositivo) {
        this.nombre_dispositivo = nombreDispositivo;
    }
}

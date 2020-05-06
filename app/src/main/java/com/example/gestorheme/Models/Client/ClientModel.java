package com.example.gestorheme.Models.Client;

public class ClientModel {
    private long clientId = 0;
    private String nombre = "";
    private String apellidos = "";
    private long fecha = 0;
    private String telefono = "";
    private String email = "";
    private String direccion = "";
    private String cadenciaVisita = "";
    private String observaciones = "";
    private long notificacionPersonalizada = 0;
    private String imagen = "";

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCadenciaVisita() {
        return cadenciaVisita;
    }

    public void setCadenciaVisita(String cadenciaVisita) {
        this.cadenciaVisita = cadenciaVisita;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public long getNotificacionPersonalizada() {
        return notificacionPersonalizada;
    }

    public void setNotificacionPersonalizada(long notificacionPersonalizada) {
        this.notificacionPersonalizada = notificacionPersonalizada;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
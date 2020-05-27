package com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Models;

public class BirthdayModel {
    private long userId = 0;
    private String nombre = "";
    private String apellidos = "";

    public BirthdayModel(long userId, String nombre, String apellidos) {
        this.userId = userId;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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
}

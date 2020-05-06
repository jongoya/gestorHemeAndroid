package com.example.gestorheme.Models.Service;

import java.util.ArrayList;

public class ServiceModel {
    private long clientId = 0;
    private long serviceId = 0;
    private String nombre = "";
    private String apellidos = "";
    private long fecha = 0;
    private long profesional = 0;
    private ArrayList servicios = new ArrayList();
    private String observaciones = "";
    private double precio = 0.0;

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
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

    public long getProfesional() {
        return profesional;
    }

    public void setProfesional(long profesional) {
        this.profesional = profesional;
    }

    public ArrayList getServicios() {
        return servicios;
    }

    public void setServicios(ArrayList servicios) {
        this.servicios = servicios;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}

package com.example.gestorheme.Models.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ServiceModel implements Serializable {
    private long clientId = 0;
    private long serviceId = 0;
    private long fecha = 0;
    private long empleadoId = 0;
    private ArrayList<Long> servicios = new ArrayList<>();
    private String observaciones = "";
    private double precio = 0.0;
    private long comercioId = 0;
    private String unique_deviceId;

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

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public long getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(long empleadoId) {
        this.empleadoId = empleadoId;
    }

    public ArrayList<Long> getServicios() {
        return servicios;
    }

    public void setServicios(ArrayList<Long> servicios) {
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

    public long getComercioId() {
        return comercioId;
    }

    public void setComercioId(long comercioId) {
        this.comercioId = comercioId;
    }

    public String getUnique_deviceId() {
        return unique_deviceId;
    }

    public void setUnique_deviceId(String unique_deviceId) {
        this.unique_deviceId = unique_deviceId;
    }
}

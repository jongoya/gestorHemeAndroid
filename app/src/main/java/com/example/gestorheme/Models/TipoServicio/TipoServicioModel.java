package com.example.gestorheme.Models.TipoServicio;

import java.io.Serializable;

public class TipoServicioModel implements Serializable {
    private long serviceId;
    private String nombre;

    public TipoServicioModel(long serviceId, String nombre) {
        this.serviceId = serviceId;
        this.nombre = nombre;
    }

    public TipoServicioModel() {

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
}

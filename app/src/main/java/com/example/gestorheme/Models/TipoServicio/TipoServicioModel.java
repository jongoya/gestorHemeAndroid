package com.example.gestorheme.Models.TipoServicio;

import java.io.Serializable;

public class TipoServicioModel implements Serializable {
    private long servicioId;
    private String nombre;
    private long comercioId;

    public TipoServicioModel(long servicioId, String nombre) {
        this.servicioId = servicioId;
        this.nombre = nombre;
    }

    public TipoServicioModel() {

    }

    public long getServicioId() {
        return servicioId;
    }

    public void setServicioId(long servicioId) {
        this.servicioId = servicioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getComercioId() {
        return comercioId;
    }

    public void setComercioId(long comercioId) {
        this.comercioId = comercioId;
    }
}

package com.example.gestorheme.Models.TipoServicio;

import android.content.Context;

import com.example.gestorheme.Common.CommonFunctions;

import java.io.Serializable;

public class TipoServicioModel implements Serializable {
    private long servicioId;
    private String nombre;
    private long comercioId;
    private String unique_deviceId;

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

    public String getUnique_deviceId() {
        return unique_deviceId;
    }

    public void setUnique_deviceId(String unique_deviceId) {
        this.unique_deviceId = unique_deviceId;
    }
}

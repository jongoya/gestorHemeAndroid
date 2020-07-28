package com.example.gestorheme.Models.Cesta;

import java.io.Serializable;

public class CestaModel implements Serializable {
    private long cestaId = 0;
    private long clientId = 0;
    private long fecha = 0;
    private boolean efectivo = false;
    private long comercioId = 0;

    public long getCestaId() {
        return cestaId;
    }

    public void setCestaId(long cestaId) {
        this.cestaId = cestaId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public boolean isEfectivo() {
        return efectivo;
    }

    public void setEfectivo(boolean efectivo) {
        this.efectivo = efectivo;
    }

    public long getComercioId() {
        return comercioId;
    }

    public void setComercioId(long comercioId) {
        this.comercioId = comercioId;
    }
}

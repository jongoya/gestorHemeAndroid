package com.example.gestorheme.Models.Dispositivo;

import java.io.Serializable;

public class DispositivoModel implements Serializable {

    private long dispositivoId = 0;
    private long comercioId = 0;
    private long fecha = 0;
    private String nombre_dispositivo = "";
    private String unique_deviceId = "";

    public DispositivoModel() {

    }

    public Long getDispositivoId() {
        return dispositivoId;
    }

    public void setDispositivoId(Long dispositivoId) {
        this.dispositivoId = dispositivoId;
    }

    public Long getComercioId() {
        return comercioId;
    }

    public void setComercioId(Long comercioId) {
        this.comercioId = comercioId;
    }

    public Long getFecha() {
        return fecha;
    }

    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    public String getNombre_dispositivo() {
        return nombre_dispositivo;
    }

    public void setNombre_dispositivo(String nombre_dispositivo) {
        this.nombre_dispositivo = nombre_dispositivo;
    }

    public String getUniqueDeviceId() {
        return unique_deviceId;
    }

    public void setUniqueDeviceId(String uniqueDeviceId) {
        this.unique_deviceId = uniqueDeviceId;
    }
}

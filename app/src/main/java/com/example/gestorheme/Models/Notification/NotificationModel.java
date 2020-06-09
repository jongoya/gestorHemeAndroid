package com.example.gestorheme.Models.Notification;

import java.io.Serializable;
import java.util.ArrayList;

public class NotificationModel implements Serializable {
    private long clientId = 0;
    private long notificationId = 0;
    private String descripcion = "";
    private long fecha = 0;
    private boolean leido = false;
    private String type = "";
    private long comercioId = 0;
    private String unique_deviceId;

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

package com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Models;

import com.example.gestorheme.Models.Notification.NotificationModel;

import java.io.Serializable;
import java.util.ArrayList;

public class NotificationDayModel implements Serializable {

    private long fecha;
    private ArrayList<NotificationModel> notificaciones;
    private String dayType;
    private String tituloHeader;

    public NotificationDayModel(long fecha, ArrayList<NotificationModel> notificaciones, String type, String tituloHeader) {
        this.fecha = fecha;
        this.notificaciones = notificaciones;
        this.dayType = type;
        this.tituloHeader =  tituloHeader;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public ArrayList<NotificationModel> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(ArrayList<NotificationModel> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public String getDayType() {
        return dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public String getTituloHeader() {
        return tituloHeader;
    }

    public void setTituloHeader(String tituloHeader) {
        this.tituloHeader = tituloHeader;
    }
}

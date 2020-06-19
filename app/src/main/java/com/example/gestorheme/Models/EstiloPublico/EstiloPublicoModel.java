package com.example.gestorheme.Models.EstiloPublico;

import java.io.Serializable;

public class EstiloPublicoModel implements Serializable {

    private long estiloId = 0;
    private String androidBundleId = "";
    private String iosBundleId = "";
    private String fondoLogin = "";
    private String primaryTextColor = "";
    private String secondaryTextColor = "";
    private String primaryColor = "";
    private String nombreApp = "";
    private String iconoApp = "";

    public long getEstiloId() {
        return estiloId;
    }

    public void setEstiloId(long estiloId) {
        this.estiloId = estiloId;
    }

    public String getAndroidBundleId() {
        return androidBundleId;
    }

    public void setAndroidBundleId(String androidBundleId) {
        this.androidBundleId = androidBundleId;
    }

    public String getIosBundleId() {
        return iosBundleId;
    }

    public void setIosBundleId(String iosBundleId) {
        this.iosBundleId = iosBundleId;
    }

    public String getFondoLogin() {
        return fondoLogin;
    }

    public void setFondoLogin(String fondoLogin) {
        this.fondoLogin = fondoLogin;
    }

    public String getPrimaryTextColor() {
        return primaryTextColor;
    }

    public void setPrimaryTextColor(String primaryTextColor) {
        this.primaryTextColor = primaryTextColor;
    }

    public String getSecondaryTextColor() {
        return secondaryTextColor;
    }

    public void setSecondaryTextColor(String secondaryTextColor) {
        this.secondaryTextColor = secondaryTextColor;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getNombreApp() {
        return nombreApp;
    }

    public void setNombreApp(String nombreApp) {
        this.nombreApp = nombreApp;
    }

    public String getIconoApp() {
        return iconoApp;
    }

    public void setIconoApp(String iconoApp) {
        this.iconoApp = iconoApp;
    }
}

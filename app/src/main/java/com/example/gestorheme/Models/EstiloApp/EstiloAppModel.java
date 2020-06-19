package com.example.gestorheme.Models.EstiloApp;

import java.io.Serializable;

public class EstiloAppModel implements Serializable {

    private long estiloId = 0;
    private long comercioId = 0;
    private String primaryTextColor = "";
    private String secondaryTextColor = "";
    private String primaryColor = "";
    private String secondaryColor = "";
    private String backgroundColor = "";
    private String navigationColor = "";
    private String appSmallIcon = "";
    private String appName = "";

    public long getEstiloId() {
        return estiloId;
    }

    public void setEstiloId(long estiloId) {
        this.estiloId = estiloId;
    }

    public long getComercioId() {
        return comercioId;
    }

    public void setComercioId(long comercioId) {
        this.comercioId = comercioId;
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

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getNavigationColor() {
        return navigationColor;
    }

    public void setNavigationColor(String navigationColor) {
        this.navigationColor = navigationColor;
    }

    public String getAppSmallIcon() {
        return appSmallIcon;
    }

    public void setAppSmallIcon(String appSmallIcon) {
        this.appSmallIcon = appSmallIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}

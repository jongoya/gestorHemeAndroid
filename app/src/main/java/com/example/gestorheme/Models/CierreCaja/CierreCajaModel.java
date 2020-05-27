package com.example.gestorheme.Models.CierreCaja;

public class CierreCajaModel {
    private long cajaId = 0;
    private long fecha = 0;
    private int numeroServicios = 0;
    private double totalCaja = 0.0;
    private double totalProductos = 0.0;
    private double efectivo = 0.0;
    private double tarjeta = 0.0;
    private long comercioId = 0;

    public long getCajaId() {
        return cajaId;
    }

    public void setCajaId(long cajaId) {
        this.cajaId = cajaId;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public int getNumeroServicios() {
        return numeroServicios;
    }

    public void setNumeroServicios(int numeroServicios) {
        this.numeroServicios = numeroServicios;
    }

    public double getTotalCaja() {
        return totalCaja;
    }

    public void setTotalCaja(double totalCaja) {
        this.totalCaja = totalCaja;
    }

    public double getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(double totalProductos) {
        this.totalProductos = totalProductos;
    }

    public double getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(double efectivo) {
        this.efectivo = efectivo;
    }

    public double getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(double tarjeta) {
        this.tarjeta = tarjeta;
    }

    public long getComercioId() {
        return comercioId;
    }

    public void setComercioId(long comercioId) {
        this.comercioId = comercioId;
    }
}

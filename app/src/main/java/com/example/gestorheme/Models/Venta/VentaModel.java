package com.example.gestorheme.Models.Venta;

import java.io.Serializable;

public class VentaModel implements Serializable {
    private long cestaId = 0;
    private long ventaId = 0;
    private long productoId = 0;
    private int cantidad = 1;
    private long comercioId = 0;

    public VentaModel(long productoId) {
        this.productoId = productoId;
    }

    public VentaModel() {

    }

    public long getCestaId() {
        return cestaId;
    }

    public void setCestaId(long cestaId) {
        this.cestaId = cestaId;
    }

    public long getVentaId() {
        return ventaId;
    }

    public void setVentaId(long ventaId) {
        this.ventaId = ventaId;
    }

    public long getProductoId() {
        return productoId;
    }

    public void setProductoId(long productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public long getComercioId() {
        return comercioId;
    }

    public void setComercioId(long comercioId) {
        this.comercioId = comercioId;
    }
}

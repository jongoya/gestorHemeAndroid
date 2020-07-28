package com.example.gestorheme.Models.Producto;

import java.io.Serializable;

public class ProductoModel implements Serializable {
    private long productoId = 0;
    private String nombre = "";
    private String codigoBarras = "";
    private String imagen = "";
    private int numProductos = 0;
    private long comercioId = 0;
    private double precio = 0.0;

    public ProductoModel(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public ProductoModel() {

    }

    public long getProductoId() {
        return productoId;
    }

    public void setProductoId(long productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getNumProductos() {
        return numProductos;
    }

    public void setNumProductos(int numProductos) {
        this.numProductos = numProductos;
    }

    public long getComercioId() {
        return comercioId;
    }

    public void setComercioId(long comercioId) {
        this.comercioId = comercioId;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}

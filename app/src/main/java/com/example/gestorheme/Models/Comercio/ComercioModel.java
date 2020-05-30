package com.example.gestorheme.Models.Comercio;

public class ComercioModel {
    int refImagen = 0;
    String titulo = "";
    String descripcion = "";

    public ComercioModel(int refImagen, String titulo, String descripcion) {
        this.refImagen = refImagen;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public int getRefImagen() {
        return refImagen;
    }

    public void setRefImagen(int refImagen) {
        this.refImagen = refImagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

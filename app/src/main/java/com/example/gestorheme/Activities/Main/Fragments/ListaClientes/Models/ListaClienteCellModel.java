package com.example.gestorheme.Activities.Main.Fragments.ListaClientes.Models;

import com.example.gestorheme.Models.Client.ClientModel;

import java.io.Serializable;

public class ListaClienteCellModel implements Serializable {
    private int cellType;
    private String letra;
    private ClientModel cliente;

    public ListaClienteCellModel(int cellType, ClientModel cliente) {
        this.cellType = cellType;
        this.cliente = cliente;
    }

    public ListaClienteCellModel(int cellType, String letra) {
        this.cellType = cellType;
        this.letra = letra;
    }

    public int getCellType() {
        return cellType;
    }

    public void setCellType(int cellType) {
        this.cellType = cellType;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public ClientModel getCliente() {
        return cliente;
    }

    public void setCliente(ClientModel cliente) {
        this.cliente = cliente;
    }
}

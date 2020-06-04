package com.example.gestorheme.Models.WebServicesModels;

import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.Preferencias;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Service.ServiceModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientesMasServiciosModel implements Serializable {

    private ClientModel cliente;
    private ArrayList<ServiceModel> servicios;

    public ClientesMasServiciosModel() {

    }

    public ClientesMasServiciosModel(ClientModel cliente, ArrayList<ServiceModel> servicios, long comercioId) {
        this.cliente = cliente;

        for (int i = 0; i < servicios.size(); i++) {
            servicios.get(i).setComercioId(comercioId);
        }
        this.servicios = servicios;
    }

    public ArrayList<ServiceModel> getServicios() {
        return servicios;
    }

    public void setServicios(ArrayList<ServiceModel> servicios) {
        this.servicios = servicios;
    }

    public ClientModel getCliente() {
        return cliente;
    }

    public void setCliente(ClientModel cliente) {
        this.cliente = cliente;
    }
}

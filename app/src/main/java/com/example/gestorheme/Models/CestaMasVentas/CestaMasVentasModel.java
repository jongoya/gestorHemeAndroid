package com.example.gestorheme.Models.CestaMasVentas;

import com.example.gestorheme.Models.Cesta.CestaModel;
import com.example.gestorheme.Models.Venta.VentaModel;

import java.io.Serializable;
import java.util.ArrayList;

public class CestaMasVentasModel implements Serializable {
    private CestaModel cesta;
    private ArrayList<VentaModel> ventas;

    public CestaMasVentasModel(CestaModel cesta, ArrayList<VentaModel> ventas) {
        this.cesta = cesta;
        this.ventas = ventas;
    }

    public CestaModel getCesta() {
        return cesta;
    }

    public void setCesta(CestaModel cesta) {
        this.cesta = cesta;
    }

    public ArrayList<VentaModel> getVentas() {
        return ventas;
    }

    public void setVentas(ArrayList<VentaModel> ventas) {
        this.ventas = ventas;
    }
}

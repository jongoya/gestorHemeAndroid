package com.example.gestorheme.Activities.Stadisticas.Common;

import com.example.gestorheme.Models.CierreCaja.CierreCajaModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StadisticasFunctions {

    public static ArrayList<CierreCajaModel> groupCierreCajasAnual(ArrayList<CierreCajaModel> cierreCajas) {
        ArrayList<CierreCajaModel> cierreCajasAgrupadas = new ArrayList<>();
        for (int i = 0; i <= 11; i++) {
            ArrayList<CierreCajaModel> cierreCajasPorMes = new ArrayList<>();
            for (int j = 0; j < cierreCajas.size(); j++) {
                CierreCajaModel cierreCaja = cierreCajas.get(j);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(cierreCaja.getFecha()));
                int mes = calendar.get(Calendar.MONTH);
                if (mes == i) {
                    cierreCajasPorMes.add(cierreCaja);
                }
            }

            if (cierreCajasPorMes.size() > 0) {
                cierreCajasAgrupadas.add(groupCierreCajas(cierreCajasPorMes));
            }
        }

        return cierreCajasAgrupadas;
    }

    public static ArrayList<CierreCajaModel> groupCierreCajasTotal(ArrayList<CierreCajaModel> cierreCajas) {
        ArrayList<CierreCajaModel> cierreCajasAgrupadas = new ArrayList<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = (year - 50); i <= (year + 50); i++) {
            ArrayList<CierreCajaModel> cierreCajasPorAño = new ArrayList<>();
            for (int j = 0; j < cierreCajas.size(); j++) {
                CierreCajaModel cierreCaja = cierreCajas.get(j);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(cierreCaja.getFecha()));
                int año = calendar.get(Calendar.YEAR);
                if (año == i) {
                    cierreCajasPorAño.add(cierreCaja);
                }
            }
            if (cierreCajasPorAño.size() > 0) {
                cierreCajasAgrupadas.add(groupCierreCajas(cierreCajasPorAño));
            }
        }

        return  cierreCajasAgrupadas;
    }

    private static CierreCajaModel groupCierreCajas(ArrayList<CierreCajaModel> cierreCajas) {
        CierreCajaModel cierreCajaModel = new CierreCajaModel();
        for (int i = 0; i < cierreCajas.size(); i++) {
            cierreCajaModel.setNumeroServicios(cierreCajaModel.getNumeroServicios() + cierreCajas.get(i).getNumeroServicios());
            cierreCajaModel.setTotalCaja(cierreCajaModel.getTotalCaja() + cierreCajas.get(i).getTotalCaja());
            cierreCajaModel.setTotalProductos(cierreCajaModel.getTotalProductos() + cierreCajas.get(i).getTotalProductos());
            cierreCajaModel.setEfectivo(cierreCajaModel.getEfectivo() + cierreCajas.get(i).getEfectivo());
            cierreCajaModel.setTarjeta(cierreCajaModel.getTarjeta() + cierreCajas.get(i).getTarjeta());
        }
        cierreCajaModel.setFecha(cierreCajas.get(0).getFecha());

        return cierreCajaModel;
    }
}

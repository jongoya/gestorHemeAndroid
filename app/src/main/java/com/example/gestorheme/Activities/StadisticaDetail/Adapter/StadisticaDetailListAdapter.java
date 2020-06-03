package com.example.gestorheme.Activities.StadisticaDetail.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gestorheme.Models.CierreCaja.CierreCajaModel;
import com.example.gestorheme.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StadisticaDetailListAdapter extends BaseAdapter {
    private Context contexto;
    private ArrayList<CierreCajaModel> cierreCajas;
    private boolean isMensual;
    private boolean isAnual;
    private String titulo;

    public StadisticaDetailListAdapter(Context contexto, ArrayList<CierreCajaModel> cierreCajas, boolean isMensual, boolean isAnual, String titulo) {
        this.contexto = contexto;
        this.cierreCajas = cierreCajas;
        this.titulo = titulo;
        this.isMensual = isMensual;
        this.isAnual = isAnual;
    }

    @Override
    public int getCount() {
        return cierreCajas.size();
    }

    @Override
    public Object getItem(int i) {
        return cierreCajas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();

        LayoutInflater mInflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.stadistica_detail_item_layout, null);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        holder.fecha = view.findViewById(R.id.fechaLabel);
        holder.valor = view.findViewById(R.id.valorLabel);
        CierreCajaModel cierreCaja = cierreCajas.get(i);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(cierreCaja.getFecha()));
        holder.fecha.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        if (isMensual) {
            holder.fecha.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " de " + new SimpleDateFormat("MMMM").format(calendar.getTime()) + " de " + String.valueOf(calendar.get(Calendar.YEAR)));
        }

        if (isAnual) {
            holder.fecha.setText(new SimpleDateFormat("MMMM").format(calendar.getTime()) + " de " + String.valueOf(calendar.get(Calendar.YEAR)));
        }

        holder.valor.setText(getValue(cierreCaja));
        return view;
    }

    private String getValue(CierreCajaModel cierreCaja) {
        switch (titulo) {
            case "Numero Servicios":
                return String.valueOf(cierreCaja.getNumeroServicios());
            case  "Total Caja":
                return new DecimalFormat("#0.00").format(cierreCaja.getTotalCaja()) + " €";
            case "Total Productos":
                return new DecimalFormat("#0.00").format(cierreCaja.getTotalProductos()) + " €";
            case "Efectivo":
                return new DecimalFormat("#0.00").format(cierreCaja.getEfectivo()) + " €";
            case "Tarjeta":
                return new DecimalFormat("#0.00").format(cierreCaja.getTarjeta()) + " €";
            default:
                return "";
        }
    }

    private static class ViewHolder {
        private TextView fecha;
        private TextView valor;
    }
}

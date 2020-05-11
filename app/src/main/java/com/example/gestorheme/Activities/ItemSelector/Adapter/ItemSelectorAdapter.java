package com.example.gestorheme.Activities.ItemSelector.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gestorheme.Models.Cadencia.CadenciaModel;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.Models.TipoServicio.TipoServicioModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

public class ItemSelectorAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Object> elements;

    public ItemSelectorAdapter(Context context, ArrayList<Object> elements) {
        this.elements = elements;
        this.context = context;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int i) {
        return elements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = (LayoutInflater.from(context)).inflate(R.layout.item_selector_cell, null);
        TextView country = view.findViewById(R.id.text);
        country.setText(getTextForItem(elements.get(i)));
        return view;
    }

    private String getTextForItem(Object item) {
        if (item instanceof CadenciaModel) {
            return ((CadenciaModel) item).getCadencia();
        } else if (item instanceof EmpleadoModel) {
            return ((EmpleadoModel) item).getNombre();
        } else if (item instanceof TipoServicioModel) {
            return ((TipoServicioModel) item).getNombre();
        }

        return "";
    }
}

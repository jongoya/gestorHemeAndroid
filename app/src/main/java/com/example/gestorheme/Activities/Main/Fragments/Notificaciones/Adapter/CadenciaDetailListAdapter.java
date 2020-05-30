package com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.R;
import java.util.ArrayList;

public class CadenciaDetailListAdapter extends BaseAdapter {
    private Context contexto;
    private ArrayList<ClientModel> clientes;

    public CadenciaDetailListAdapter(Context contexto, ArrayList<ClientModel> clientes) {
        this.contexto = contexto;
        this.clientes = clientes;
    }

    @Override
    public int getCount() {
        return clientes.size();
    }

    @Override
    public Object getItem(int i) {
        return clientes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater mInflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.cadencia_detail_list_cell, null);
        TextView nombre= view.findViewById(R.id.nombre);
        nombre.setText(clientes.get(i).getNombre() + " " + clientes.get(i).getApellidos());

        return view;
    }
}

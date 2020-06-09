package com.example.gestorheme.Activities.Login.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gestorheme.Models.Dispositivo.DispositivoModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

public class DeviceListAdapter extends BaseAdapter {
    private Context contexto;
    private ArrayList<DispositivoModel> dispositivos;

    public DeviceListAdapter(Context contexto, ArrayList<DispositivoModel> dispositivos) {
        this.contexto = contexto;
        this.dispositivos = dispositivos;
    }

    @Override
    public int getCount() {
        return dispositivos.size();
    }

    @Override
    public Object getItem(int i) {
        return dispositivos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();

        LayoutInflater mInflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.device_item_layout, null);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        holder.nombre = view.findViewById(R.id.nombreLabel);

        holder.nombre.setText(dispositivos.get(i).getNombre_dispositivo());
        return view;
    }
    private static class ViewHolder {
        private TextView nombre;
    }
}

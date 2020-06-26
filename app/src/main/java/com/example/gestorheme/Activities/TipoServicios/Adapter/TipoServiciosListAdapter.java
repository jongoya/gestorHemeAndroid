package com.example.gestorheme.Activities.TipoServicios.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Models.TipoServicio.TipoServicioModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

public class TipoServiciosListAdapter extends BaseAdapter {
    private Context contexto;
    private ArrayList<TipoServicioModel> servicios;

    public TipoServiciosListAdapter(Context contexto, ArrayList<TipoServicioModel> servicios) {
        this.contexto = contexto;
        this.servicios = servicios;
    }

    @Override
    public int getCount() {
        return servicios.size();
    }

    @Override
    public Object getItem(int i) {
        return servicios.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();

        LayoutInflater mInflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.tipo_servicio_item_layout, null);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        holder.nombre = view.findViewById(R.id.nombre);
        holder.img = view.findViewById(R.id.img);
        holder.rootLayout = view.findViewById(R.id.rootLayout);

        holder.nombre.setText(servicios.get(i).getNombre());

        holder.nombre.setTextColor(AppStyle.getPrimaryTextColor());
        holder.img.setColorFilter(AppStyle.getPrimaryTextColor());
        CommonFunctions.customizeView(contexto, holder.rootLayout, AppStyle.getSecondaryColor());
        return view;
    }


    private static class ViewHolder {
        private TextView nombre;
        private ImageView img;
        private RelativeLayout rootLayout;
    }
}

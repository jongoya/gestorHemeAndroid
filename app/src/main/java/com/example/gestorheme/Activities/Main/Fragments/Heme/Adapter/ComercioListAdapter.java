package com.example.gestorheme.Activities.Main.Fragments.Heme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Adapter.NotificationListAdapter;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Models.Comercio.ComercioModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

public class ComercioListAdapter extends BaseAdapter {
    private Context contexto;
    private ArrayList<ComercioModel> lista;

    public ComercioListAdapter(Context contexto, ArrayList<ComercioModel> lista) {
        this.contexto = contexto;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();

        LayoutInflater mInflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = mInflater.inflate(R.layout.comercio_cell_layout, null);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        holder.titulo = view.findViewById(R.id.titulo);
        holder.descripcion = view.findViewById(R.id.descripcion);
        holder.image = view.findViewById(R.id.image);

        ComercioModel model = lista.get(i);

        holder.titulo.setText(model.getTitulo());
        holder.descripcion.setText(model.getDescripcion());
        holder.image.setImageResource(model.getRefImagen());

        holder.image.setColorFilter(AppStyle.getPrimaryTextColor());
        holder.titulo.setTextColor(AppStyle.getPrimaryTextColor());
        holder.descripcion.setTextColor(AppStyle.getPrimaryTextColor());

        return view;
    }

    private static class ViewHolder {
        private TextView titulo;
        private TextView descripcion;
        private ImageView image;
    }
}

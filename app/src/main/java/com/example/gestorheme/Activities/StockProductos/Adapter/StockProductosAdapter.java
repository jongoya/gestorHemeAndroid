package com.example.gestorheme.Activities.StockProductos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Models.Producto.ProductoModel;
import com.example.gestorheme.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class StockProductosAdapter extends BaseAdapter {
    private Context contexto;
    private ArrayList<ProductoModel> productos;

    public StockProductosAdapter(Context contexto, ArrayList<ProductoModel> productos) {
        this.contexto = contexto;
        this.productos = productos;
    }

    @Override
    public int getCount() {
        return productos.size();
    }

    @Override
    public Object getItem(int i) {
        return productos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        LayoutInflater mInflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.stock_producto_cell, null);
        holder.nombre = view.findViewById(R.id.nombreLabel);
        holder.cantidad = view.findViewById(R.id.cantidad);
        holder.imagen = view.findViewById(R.id.img);

        if (productos.get(i).getImagen().length() == 0) {
            holder.imagen.setImageResource(R.drawable.user_image);
            holder.imagen.setCornerRadius(0);
            holder.imagen.setColorFilter(AppStyle.getPrimaryTextColor());
        } else {
            holder.imagen.setImageBitmap(CommonFunctions.convertBase64StringToBitmap(productos.get(i).getImagen()));
            holder.imagen.setCornerRadius(CommonFunctions.convertToPx(30, contexto));
        }
        holder.nombre.setTextColor(AppStyle.getPrimaryTextColor());
        holder.cantidad.setTextColor(AppStyle.getSecondaryTextColor());
        holder.nombre.setText(productos.get(i).getNombre());
        holder.cantidad.setText("Cantidad en stock: " + String.valueOf(productos.get(i).getNumProductos()));

        return view;
    }

    private static class ViewHolder {
        private TextView cantidad;
        private TextView nombre;
        private RoundedImageView imagen;
    }
}

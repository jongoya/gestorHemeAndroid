package com.example.gestorheme.Activities.Main.Fragments.ListaClientes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.gestorheme.Activities.Main.Fragments.ListaClientes.Models.ListaClienteCellModel;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ClientListAdapter extends BaseAdapter {

    private static final int TYPE_CLIENT = 0;
    private static final int TYPE_HEADER = 1;
    public ArrayList<ListaClienteCellModel> clientes = new ArrayList<>();

    private Context context ;

    public ClientListAdapter(Context context) {
        this.context = context;
    }

    public void addItem(ListaClienteCellModel cliente) {
        clientes.add(cliente);
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        int rowType = clientes.get(i).getCellType();

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (rowType) {
            case TYPE_CLIENT:
                view = mInflater.inflate(R.layout.clientes_cell_layout, null);
                view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, CommonFunctions.convertToPx(80, context)));
                holder.nombre = view.findViewById(R.id.nombre);
                holder.telefono = view.findViewById(R.id.telefono);
                holder.imagen = view.findViewById(R.id.imagen);
                holder.nombre.setText(clientes.get(i).getCliente().getApellidos() + ", " + clientes.get(i).getCliente().getNombre());
                holder.telefono.setText("Telefono: " + clientes.get(i).getCliente().getTelefono());
                if (clientes.get(i).getCliente().getImagen().length() == 0) {
                    holder.imagen.setImageResource(R.drawable.user_image);
                    holder.imagen.setCornerRadius(0);
                    holder.imagen.setColorFilter(AppStyle.getPrimaryTextColor());
                } else {
                    holder.imagen.setImageBitmap(CommonFunctions.convertBase64StringToBitmap(clientes.get(i).getCliente().getImagen()));
                    holder.imagen.setCornerRadius(CommonFunctions.convertToPx(75, context));
                }
                holder.nombre.setTextColor(AppStyle.getPrimaryTextColor());
                holder.telefono.setTextColor(AppStyle.getSecondaryTextColor());

                break;
            case TYPE_HEADER:
                view = mInflater.inflate(R.layout.clientes_header_layout, null);
                view.setBackgroundColor(AppStyle.getBackgroundColor());
                view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, CommonFunctions.convertToPx(40, context)));
                holder.letra = view.findViewById(R.id.client_letter);
                holder.letra.setText(clientes.get(i).getLetra());
                holder.letra.setTextColor(AppStyle.getPrimaryTextColor());
                break;
        }

        return view;
    }

    private static class ViewHolder {
        private TextView letra;
        private TextView nombre;
        private TextView telefono;
        private RoundedImageView imagen;
    }
}

package com.example.gestorheme.Activities.Cesta.Views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gestorheme.Activities.Cesta.Interface.VentaViewInterface;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Producto.ProductoModel;
import com.example.gestorheme.Models.Venta.VentaModel;
import com.makeramen.roundedimageview.RoundedImageView;

public class VentaView extends RelativeLayout {
    private VentaModel venta;
    private ProductoModel producto;
    private RoundedImageView imageView;
    private TextView nombre;
    private TextView precio;
    private TextView cantidad;
    private int position;
    private VentaViewInterface delegate;

    public VentaView(Context context, VentaModel venta, int position, boolean isLastVenta, VentaViewInterface delegate, boolean addClickEvent) {
        super(context);
        setLayoutParams();
        setBackgroundColor(Color.WHITE);
        this.venta = venta;
        this.producto = Constants.databaseManager.productoManager.getProductoWithProductId(venta.getProductoId());
        this.position = position;
        this.delegate = delegate;
        addImageView();
        addNombre();
        addPrecio();
        addCantidad();

        if (!isLastVenta) {
            addDivisory();
        }

        if (addClickEvent) {
            addClickEvent();
        }
    }

    private void setLayoutParams() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonFunctions.convertToPx(80, getContext()));
        setLayoutParams(params);
    }

    private void addImageView() {
        imageView = new RoundedImageView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(CommonFunctions.convertToPx(60, getContext()), CommonFunctions.convertToPx(60, getContext()));
        params.setMargins(CommonFunctions.convertToPx(10, getContext()), CommonFunctions.convertToPx(10, getContext()), 0,0);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(CommonFunctions.convertBase64StringToBitmap(producto.getImagen()));
        imageView.setCornerRadius(CommonFunctions.convertToPx(75, getContext()));
        addView(imageView);
    }

    private void addNombre() {
        nombre = new TextView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(CommonFunctions.convertToPx(80, getContext()), CommonFunctions.convertToPx(10, getContext()), 0, 0);
        nombre.setLayoutParams(params);
        nombre.setTextColor(AppStyle.getPrimaryTextColor());
        nombre.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        nombre.setTypeface(nombre.getTypeface(), Typeface.BOLD);
        nombre.setText(producto.getNombre());
        nombre.setLines(1);
        addView(nombre);
    }

    private void addPrecio() {
        precio = new TextView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(CommonFunctions.convertToPx(80, getContext()), CommonFunctions.convertToPx(30, getContext()), 0, 0);
        precio.setLayoutParams(params);
        precio.setTextColor(AppStyle.getSecondaryTextColor());
        precio.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        precio.setText("Precio: " + String.valueOf(producto.getPrecio()) + " €");
        precio.setLines(1);
        addView(precio);
    }

    private void addCantidad() {
        cantidad = new TextView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(CommonFunctions.convertToPx(80, getContext()), CommonFunctions.convertToPx(50, getContext()), 0, 0);
        cantidad.setLayoutParams(params);
        cantidad.setTextColor(AppStyle.getPrimaryTextColor());
        cantidad.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        cantidad.setText("Cantidad: " + String.valueOf(venta.getCantidad()));
        cantidad.setLines(1);
        addView(cantidad);
    }

    private void addDivisory() {
        RelativeLayout divisory = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonFunctions.convertToPx(1, getContext()));
        params.setMargins(CommonFunctions.convertToPx(10, getContext()), CommonFunctions.convertToPx(79, getContext()), 0, 0);
        divisory.setLayoutParams(params);
        divisory.setBackgroundColor(AppStyle.getSecondaryColor());
        addView(divisory);
    }

    private void addClickEvent() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.ventaClicked(position);
            }
        });
    }
}

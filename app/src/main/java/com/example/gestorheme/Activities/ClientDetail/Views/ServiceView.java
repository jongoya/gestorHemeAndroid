package com.example.gestorheme.Activities.ClientDetail.Views;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Models.Cesta.CestaModel;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Producto.ProductoModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.Models.Venta.VentaModel;
import com.example.gestorheme.R;

import java.util.ArrayList;


public class ServiceView extends ConstraintLayout {
    private TextView nombreLabel;
    private TextView fechaLabel;
    private TextView profesionalLabel;
    private TextView serviciosLabel;
    private TextView precioLabel;
    private TextView observacionesField;

    private TextView nombreField;
    private TextView fechaField;
    private TextView profesionalField;
    private TextView serviciosField;
    private TextView precioField;
    private TextView titulo;

    private RelativeLayout nombreDivider;
    private RelativeLayout fechaDivider;
    private RelativeLayout profesionalDivider;
    private RelativeLayout serviciosDivider;
    private RelativeLayout precioDivider;
    private RelativeLayout layoutContentView;
    private ConstraintLayout rootLayout;

    public ServiceModel service;
    public CestaModel cesta;

    public ServiceView(Context context, ServiceModel service, CestaModel cesta, String nombreCliente, String apellidosCliente) {
        super(context);
        View.inflate(context, R.layout.service_view, this);
        this.service = service;
        this.cesta = cesta;
        getFields();
        cutomizeLabels();
        customizeFields();
        customizeDividers();
        customizeBackground();

        if (service != null) {
            setServiceDetails(nombreCliente, apellidosCliente);
        } else {
            setCestaDetails();
        }
    }

    private void getFields() {
        nombreLabel = findViewById(R.id.nombreLabel);
        fechaLabel = findViewById(R.id.fechaLabel);
        profesionalLabel = findViewById(R.id.profesionalLabel);
        serviciosLabel = findViewById(R.id.serviciosLabel);
        precioLabel = findViewById(R.id.precioLabel);
        observacionesField = findViewById(R.id.observacionesField);
        nombreField = findViewById(R.id.nombreField);
        fechaField = findViewById(R.id.fechaField);
        profesionalField = findViewById(R.id.profesionalField);
        serviciosField = findViewById(R.id.serviciosField);
        precioField = findViewById(R.id.precioField);
        titulo = findViewById(R.id.titulo);
        nombreDivider = findViewById(R.id.nombreDivider);
        fechaDivider = findViewById(R.id.fechaDivider);
        profesionalDivider = findViewById(R.id.profesionalDivider);
        serviciosDivider = findViewById(R.id.serviciosDivider);
        precioDivider = findViewById(R.id.precioDivider);
        layoutContentView = findViewById(R.id.layoutContentView);
        rootLayout = findViewById(R.id.root);
    }

    private void cutomizeLabels() {
        nombreLabel.setTextColor(AppStyle.getSecondaryTextColor());
        fechaLabel.setTextColor(AppStyle.getSecondaryTextColor());
        profesionalLabel.setTextColor(AppStyle.getSecondaryTextColor());
        serviciosLabel.setTextColor(AppStyle.getSecondaryTextColor());
        precioLabel.setTextColor(AppStyle.getSecondaryTextColor());
    }

    private void customizeFields() {
        nombreField.setTextColor(AppStyle.getPrimaryTextColor());
        fechaField.setTextColor(AppStyle.getPrimaryTextColor());
        profesionalField.setTextColor(AppStyle.getPrimaryTextColor());
        serviciosField.setTextColor(AppStyle.getPrimaryTextColor());
        precioField.setTextColor(AppStyle.getPrimaryTextColor());
        observacionesField.setTextColor(AppStyle.getPrimaryTextColor());
    }

    private void customizeDividers() {
        nombreDivider.setBackgroundColor(AppStyle.getSecondaryColor());
        fechaDivider.setBackgroundColor(AppStyle.getSecondaryColor());
        profesionalDivider.setBackgroundColor(AppStyle.getSecondaryColor());
        serviciosDivider.setBackgroundColor(AppStyle.getSecondaryColor());
        precioDivider.setBackgroundColor(AppStyle.getSecondaryColor());
    }

    private void customizeBackground() {
        CommonFunctions.customizeView(getContext(), layoutContentView, AppStyle.getSecondaryColor());
    }

    private void setServiceDetails(String nombreCliente, String apellidosCliente) {
        ClientModel cliente = Constants.databaseManager.clientsManager.getClientForClientId(service.getClientId());
        if (cliente == null) {
            nombreLabel.setText(nombreCliente + " " + apellidosCliente);
        } else {
            nombreLabel.setText(cliente.getNombre() + " " + cliente.getApellidos());
        }
        fechaLabel.setText(DateFunctions.convertTimestampToServiceDateString(service.getFecha()));
        profesionalLabel.setText(Constants.databaseManager.empleadosManager.getEmpleadoForEmpleadoId(service.getEmpleadoId()).getNombre());
        precioLabel.setText(Double.toString(service.getPrecio()) + " €");
        serviciosLabel.setText(CommonFunctions.getServiciosForServicio(service.getServicios()));
        observacionesField.setText(service.getObservaciones().length() > 0 ? service.getObservaciones() : "Añade una observación");
    }

    private void setCestaDetails() {
        titulo.setText("VENTA");
        ClientModel cliente = Constants.databaseManager.clientsManager.getClientForClientId(cesta.getClientId());
        nombreLabel.setText(cliente.getNombre() + " " + cliente.getApellidos());
        fechaLabel.setText(DateFunctions.convertTimestampToServiceDateString(cesta.getFecha()));
        profesionalField.setText("Precio venta");
        profesionalLabel.setText(String.valueOf(calculateVenta() + " €"));
        ConstraintLayout.LayoutParams params = (LayoutParams) rootLayout.getLayoutParams();
        params.height =CommonFunctions.convertToPx(193, getContext());
        rootLayout.setLayoutParams(params);
    }

    private double calculateVenta() {
        double precio = 0.0;
        ArrayList<VentaModel> ventas = Constants.databaseManager.ventaManager.getVentas(cesta.getCestaId());
        for (int i = 0; i < ventas.size(); i++) {
            ProductoModel producto = Constants.databaseManager.productoManager.getProductoWithProductId(ventas.get(i).getProductoId());
            precio += producto.getPrecio() * ventas.get(i).getCantidad();
        }

        return precio;
    }
}

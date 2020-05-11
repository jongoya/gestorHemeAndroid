package com.example.gestorheme.Activities.ClientDetail.Views;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.Models.TipoServicio.TipoServicioModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

public class ServiceView extends ConstraintLayout {
    private TextView nombreLabel;
    private TextView fechaLabel;
    private TextView profesionalLabel;
    private TextView serviciosLabel;
    private TextView precioLabel;
    private TextView observacionesField;

    public ServiceModel service;

    public ServiceView(Context context, ServiceModel service) {
        super(context);
        View.inflate(context, R.layout.service_view, this);
        this.service = service;
        getFields();
        setServiceDetails();
    }

    private void getFields() {
        nombreLabel = findViewById(R.id.nombreLabel);
        fechaLabel = findViewById(R.id.fechaLabel);
        profesionalLabel = findViewById(R.id.profesionalLabel);
        serviciosLabel = findViewById(R.id.serviciosLabel);
        precioLabel = findViewById(R.id.precioLabel);
        observacionesField = findViewById(R.id.observacionesField);
    }

    private void setServiceDetails() {
        nombreLabel.setText(service.getNombre() + " " + service.getApellidos());
        fechaLabel.setText(DateFunctions.convertTimestampToServiceDateString(service.getFecha()));
        profesionalLabel.setText(Constants.databaseManager.empleadosManager.getEmpleadoForEmpleadoId(service.getProfesional()).getNombre());
        precioLabel.setText(Double.toString(service.getPrecio()) + " €");
        serviciosLabel.setText(CommonFunctions.getServiciosForServicio(service.getServicios()));
        observacionesField.setText(service.getObservaciones().length() > 0 ? service.getObservaciones() : "Añade una observación");
    }
}

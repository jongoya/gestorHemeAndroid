package com.example.gestorheme.Activities.Main.Fragments.Agenda.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gestorheme.Activities.CierreCaja.CierreCajaActivity;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CerrarCajaButton extends RelativeLayout {
    private TextView texto;
    private RelativeLayout rootLayout;

    public CerrarCajaButton(Context context, Date fecha, Activity activity) {
        super(context);
        View.inflate(context, R.layout.cerrar_caja_layout, this);
        getFields();
        customizeButton();
        setOnclickListener(context, fecha, activity);
    }

    private void getFields() {
        texto = findViewById(R.id.titulo);
        rootLayout = findViewById(R.id.cierreButton);
    }

    private void customizeButton() {
        texto.setTextColor(AppStyle.getPrimaryColor());
        CommonFunctions.customizeView(getContext(), rootLayout, AppStyle.getPrimaryColor());
    }

    private void setOnclickListener(final Context context, final Date fecha, final Activity activity) {
        rootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ServiceModel> services = Constants.databaseManager.servicesManager.getServicesForDate(fecha);
                boolean preciosIncluidos = true;
                for (int i = 0; i < services.size(); i++) {
                    if (services.get(i).getPrecio() < 1) {
                        preciosIncluidos = false;
                    }
                }

                if (preciosIncluidos) {
                    Intent intent = new Intent(context, CierreCajaActivity.class);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(fecha);
                    intent.putExtra("fecha", calendar.getTimeInMillis() / 1000);
                    context.startActivity(intent);
                } else {
                    CommonFunctions.showGenericAlertMessage(activity,"Debe inlcuir el precio a todos los servicios del día");
                }
            }
        });
    }
}

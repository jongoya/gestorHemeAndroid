package com.example.gestorheme.Activities.Main.Fragments.Agenda.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.gestorheme.Activities.CierreCaja.CierreCajaActivity;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.R;

import java.util.ArrayList;
import java.util.Date;

public class CerrarCajaButton extends RelativeLayout {
    public CerrarCajaButton(Context context, Date fecha, Activity activity) {
        super(context);
        View.inflate(context, R.layout.cerrar_caja_layout, this);
        setOnclickListener(context, fecha, activity);
    }

    private void setOnclickListener(final Context context, final Date fecha, final Activity activity) {
        findViewById(R.id.cierreButton).setOnClickListener(new OnClickListener() {
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
                    intent.putExtra("fecha", fecha.getTime());
                    context.startActivity(intent);
                } else {
                    CommonFunctions.showGenericAlertMessage(activity,"Debe inlcuir el precio a todos los servicios del día");
                }


            }
        });
    }
}

package com.example.gestorheme.Activities.Main.Fragments.Agenda.Views;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.gestorheme.Activities.CierreCaja.CierreCajaActivity;
import com.example.gestorheme.R;

import java.util.Date;

public class CerrarCajaButton extends RelativeLayout {
    public CerrarCajaButton(Context context, Date fecha) {
        super(context);
        View.inflate(context, R.layout.cerrar_caja_layout, this);
        setOnclickListener(context, fecha);
    }

    private void setOnclickListener(final Context context, final Date fecha) {
        findViewById(R.id.cierreButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CierreCajaActivity.class);
                intent.putExtra("fecha", fecha.getTime());
                context.startActivity(intent);
            }
        });
    }
}

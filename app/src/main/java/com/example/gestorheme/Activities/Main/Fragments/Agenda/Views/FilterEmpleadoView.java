package com.example.gestorheme.Activities.Main.Fragments.Agenda.Views;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.R;

public class FilterEmpleadoView extends RelativeLayout {

    public FilterEmpleadoView(Context context, String nombreEmpleado) {
        super(context);
        View.inflate(context, R.layout.filter_empleado_view, this);
        ((TextView)findViewById(R.id.nombreLabel)).setText(nombreEmpleado);
        ((TextView)findViewById(R.id.nombreLabel)).setTextColor(AppStyle.getPrimaryColor());
    }
}

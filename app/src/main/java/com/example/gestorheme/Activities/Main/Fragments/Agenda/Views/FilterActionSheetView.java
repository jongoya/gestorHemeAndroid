package com.example.gestorheme.Activities.Main.Fragments.Agenda.Views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gestorheme.Activities.Main.Fragments.Agenda.Interfaces.FilterActionSheetInterface;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

public class FilterActionSheetView extends LinearLayout {
    private LinearLayout empleadosLayout;
    private FilterActionSheetInterface delegate;
    private RelativeLayout cancelarButton;
    private TextView cancelarText;

    public FilterActionSheetView(Context context, FilterActionSheetInterface delegate) {
        super(context);
        this.delegate = delegate;
        View.inflate(context, R.layout.filter_actionsheet_layout, this);
        getFields();
        customizeFields();
        setOnClickListeners();
        addEmpleadosViews(context);
    }

    private void getFields() {
        empleadosLayout = findViewById(R.id.empleados_layout);
        cancelarButton = findViewById(R.id.mensualButton);
        cancelarText = findViewById(R.id.cancelarText);
    }

    private void customizeFields() {
        CommonFunctions.customizeView(getContext(), empleadosLayout, AppStyle.getSecondaryColor());
        CommonFunctions.customizeView(getContext(), cancelarButton, AppStyle.getPrimaryColor());
        cancelarText.setTextColor(AppStyle.getPrimaryColor());
    }

    private void setOnClickListeners() {
        findViewById(R.id.mensualButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.cancelarClicked();
            }
        });
    }

    private void addEmpleadosViews(Context contexto) {
        final ArrayList <EmpleadoModel> empleados = Constants.databaseManager.empleadosManager.getEmpleadosFromDatabase();
        addTodosButton(contexto);
        addDivisory(contexto);

        for (int i = 0; i < empleados.size(); i++) {
            final EmpleadoModel empleado = empleados.get(i);
            addButtonWithEmpleado(contexto, empleado);

            if (i < empleados.size() - 1) {
                addDivisory(contexto);
            }
        }
    }

    private void addTodosButton(Context contexto) {
        FilterEmpleadoView todosView = new FilterEmpleadoView(contexto, "Todos");
        todosView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.todosButtonClicked();
            }
        });

        empleadosLayout.addView(todosView);
    }

    private void addButtonWithEmpleado(Context contexto, final EmpleadoModel empleado) {
        FilterEmpleadoView view = new FilterEmpleadoView(contexto, empleado.getNombre());
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.empleadoSelected(empleado);
            }
        });

        empleadosLayout.addView(view);
    }

    private void addDivisory(Context contexto) {
        View divisory = new View(contexto);
        divisory.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        divisory.setBackgroundColor(AppStyle.getSecondaryColor());
        empleadosLayout.addView(divisory);
    }
}

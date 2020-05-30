package com.example.gestorheme.Activities.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorheme.Activities.Empleados.EmpleadosActivity;
import com.example.gestorheme.Activities.TipoServicios.TipoServiciosActivity;
import com.example.gestorheme.R;

public class SettingsActivity extends AppCompatActivity {
    private RelativeLayout empleadosLayout;
    private RelativeLayout serviciosLayout;
    private RelativeLayout agendaLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        getFields();
        setOnClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getFields() {
        empleadosLayout = findViewById(R.id.empleadosLayout);
        serviciosLayout = findViewById(R.id.serviciosLayout);
        agendaLayout = findViewById(R.id.agendaLayout);
    }

    private void setOnClickListeners() {
        empleadosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, EmpleadosActivity.class);
                startActivity(intent);
            }
        });

        serviciosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, TipoServiciosActivity.class);
                startActivity(intent);
            }
        });

        agendaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}

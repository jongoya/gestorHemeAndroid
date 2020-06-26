package com.example.gestorheme.Activities.Settings;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorheme.Activities.Empleados.EmpleadosActivity;
import com.example.gestorheme.Activities.TipoServicios.TipoServiciosActivity;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.R;

public class SettingsActivity extends AppCompatActivity {
    private RelativeLayout empleadosLayout;
    private RelativeLayout serviciosLayout;
    private RelativeLayout agendaLayout;

    private TextView empleadosText;
    private TextView serviciosText;
    private TextView agendaText;

    private ImageView empleadosImage;
    private ImageView serviciosImage;
    private ImageView agendaImage;

    private RelativeLayout rootLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        AppStyle.setStatusBarColor(this);
        getFields();
        setOnClickListeners();
        customizeLabels();
        customizeImages();
        customizeBackground();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getFields() {
        empleadosLayout = findViewById(R.id.empleadosLayout);
        serviciosLayout = findViewById(R.id.serviciosLayout);
        agendaLayout = findViewById(R.id.agendaLayout);
        empleadosText = findViewById(R.id.empleadosLabel);
        serviciosText = findViewById(R.id.serviciosLabel);
        agendaText = findViewById(R.id.agendaLabel);
        empleadosImage = findViewById(R.id.img);
        serviciosImage = findViewById(R.id.img2);
        agendaImage = findViewById(R.id.img3);
        rootLayout = findViewById(R.id.root);
    }

    private void customizeLabels() {
        empleadosText.setTextColor(AppStyle.getPrimaryTextColor());
        serviciosText.setTextColor(AppStyle.getPrimaryTextColor());
        agendaText.setTextColor(AppStyle.getPrimaryTextColor());
    }

    private void customizeImages() {
        empleadosImage.setColorFilter(AppStyle.getPrimaryTextColor());
        serviciosImage.setColorFilter(AppStyle.getPrimaryTextColor());
        agendaImage.setColorFilter(AppStyle.getPrimaryTextColor());
    }

    private void customizeBackground() {
        rootLayout.setBackgroundColor(AppStyle.getBackgroundColor());
    }

    private void setOnClickListeners() {
        empleadosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, EmpleadosActivity.class);
                intent.putExtra("showColorLayout", false);
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
                Intent intent = new Intent(SettingsActivity.this, AgendaSettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}

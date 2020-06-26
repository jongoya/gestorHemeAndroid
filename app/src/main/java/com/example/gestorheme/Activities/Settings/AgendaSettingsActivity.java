package com.example.gestorheme.Activities.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.gestorheme.Activities.Empleados.EmpleadosActivity;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.R;

public class AgendaSettingsActivity extends AppCompatActivity {
    private ConstraintLayout rootLayout;
    private ImageView img;
    private TextView titulo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_settings_layout);
        AppStyle.setStatusBarColor(this);
        getFields();
        customizeLayout();
        setClickListeners();
    }

    private void getFields() {
        rootLayout = findViewById(R.id.root);
        img = findViewById(R.id.img);
        titulo = findViewById(R.id.titulo);
    }

    private void customizeLayout() {
        rootLayout.setBackgroundColor(AppStyle.getBackgroundColor());
        img.setColorFilter(AppStyle.getPrimaryTextColor());
        titulo.setTextColor(AppStyle.getPrimaryTextColor());
    }

    private void setClickListeners() {
        findViewById(R.id.coloresButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgendaSettingsActivity.this, EmpleadosActivity.class);
                intent.putExtra("showColorLayout", true);
                startActivity(intent);
            }
        });
    }
}

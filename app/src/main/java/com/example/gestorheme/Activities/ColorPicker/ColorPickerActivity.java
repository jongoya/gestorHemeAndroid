package com.example.gestorheme.Activities.ColorPicker;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.R;

public class ColorPickerActivity extends AppCompatActivity {
    private EmpleadoModel empleado;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_picker_layout);
        getEmpleadoIntent();
    }

    private void getEmpleadoIntent() {
        empleado = (EmpleadoModel) getIntent().getSerializableExtra("empleado");
    }
}

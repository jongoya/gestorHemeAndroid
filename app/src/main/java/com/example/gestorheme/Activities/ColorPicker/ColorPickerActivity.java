package com.example.gestorheme.Activities.ColorPicker;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.R;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ColorPickerActivity extends AppCompatActivity {
    private EmpleadoModel empleado;
    private ColorPickerView colorPickerView;
    private RelativeLayout saveButton;
    private ImageView saveImage;
    private ConstraintLayout rootLayout;
    private RelativeLayout loadingState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_picker_layout);
        getEmpleadoIntent();
        getFields();
        customizeButton();
        setListeners();
    }

    private void getEmpleadoIntent() {
        empleado = (EmpleadoModel) getIntent().getSerializableExtra("empleado");
    }

    private void getFields() {
        colorPickerView = findViewById(R.id.colorPickerView);
        saveButton = findViewById(R.id.saveButton);
        saveImage = findViewById(R.id.saveImage);
        rootLayout = findViewById(R.id.root);
    }

    private void customizeButton() {
        CommonFunctions.selectLayout(getApplicationContext(),saveButton, saveImage);
    }

    private void setListeners() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveColor();
            }
        });

        colorPickerView.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {
                empleado.setRedColorValue(colorEnvelope.getColorRGB()[0]);
                empleado.setGreenColorValue(colorEnvelope.getColorRGB()[1]);
                empleado.setBlueColorValue(colorEnvelope.getColorRGB()[2]);
            }
        });
    }

    private void saveColor() {
        loadingState = CommonFunctions.createLoadingStateView(getApplicationContext(), "Actualizando empleado");
        rootLayout.addView(loadingState);
        Call<EmpleadoModel> call = Constants.webServices.updateEmpleado(empleado);
        call.enqueue(new Callback<EmpleadoModel>() {
            @Override
            public void onResponse(Call<EmpleadoModel> call, Response<EmpleadoModel> response) {
                rootLayout.removeView(loadingState);
                if (response.code() == 200) {
                    Constants.databaseManager.empleadosManager.updateEmpleadoInDatabase(response.body());
                    ColorPickerActivity.super.onBackPressed();
                } else if (response.code() == Constants.logoutResponseValue) {
                    CommonFunctions.logout(ColorPickerActivity.this);
                } else {
                    CommonFunctions.showGenericAlertMessage(ColorPickerActivity.this, "Error actualizando el empleado");
                }
            }

            @Override
            public void onFailure(Call<EmpleadoModel> call, Throwable t) {
                rootLayout.removeView(loadingState);
                CommonFunctions.showGenericAlertMessage(ColorPickerActivity.this, "Error actualizando el empleado");
            }
        });
    }
}

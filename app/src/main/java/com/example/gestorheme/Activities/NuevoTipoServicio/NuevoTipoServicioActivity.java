package com.example.gestorheme.Activities.NuevoTipoServicio;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.gestorheme.Activities.TextInputField.TextInputFieldActivity;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.TipoServicio.TipoServicioModel;
import com.example.gestorheme.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoTipoServicioActivity extends AppCompatActivity {
    private static final int NOMBRE_FIELD_REF = 0;

    private RelativeLayout nombreLayout;
    private RelativeLayout saveButton;
    private TextView nombreLabel;
    private ImageView saveImage;
    private ConstraintLayout rootLayout;
    private RelativeLayout loadingState;

    private TipoServicioModel servicio = new TipoServicioModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_tipo_servicio_layout);
        getFields();
        setOnClickListener();
        customizeButton();
    }

    private void getFields() {
        nombreLayout = findViewById(R.id.nombreLayout);
        saveButton = findViewById(R.id.saveButton);
        nombreLabel = findViewById(R.id.nombreLabel);
        saveImage = findViewById(R.id.saveImage);
        rootLayout = findViewById(R.id.root);
    }

    private void setOnClickListener() {
        nombreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", nombreLabel.getText().toString());
                intent.putExtra("keyboard", InputType.TYPE_CLASS_TEXT);
                startActivityForResult(intent, NOMBRE_FIELD_REF);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFields();
            }
        });
    }

    private void customizeButton() {
        CommonFunctions.selectLayout(getApplicationContext(), saveButton, saveImage);
    }

    private void checkFields() {
        if (servicio.getNombre().length() == 0) {
            CommonFunctions.showGenericAlertMessage(this, "Debe incluir un nombre");
            return;
        }

        saveServicio();
    }

    private void saveServicio() {
        loadingState = CommonFunctions.createLoadingStateView(getApplicationContext(), "Guardando servicio");
        rootLayout.addView(loadingState);
        servicio.setComercioId(Constants.developmentComercioId);
        Call<TipoServicioModel> call = Constants.webServices.saveTipoServicio(servicio);
        call.enqueue(new Callback<TipoServicioModel>() {
            @Override
            public void onResponse(Call<TipoServicioModel> call, Response<TipoServicioModel> response) {
                rootLayout.removeView(loadingState);
                if (response.code() == 201) {
                    Constants.databaseManager.tipoServiciosManager.addTipoServicioToDatabase(response.body());
                    NuevoTipoServicioActivity.super.onBackPressed();
                } else {
                    CommonFunctions.showGenericAlertMessage(NuevoTipoServicioActivity.this, "Error guardando el servicio");
                }
            }

            @Override
            public void onFailure(Call<TipoServicioModel> call, Throwable t) {
                rootLayout.removeView(loadingState);
                CommonFunctions.showGenericAlertMessage(NuevoTipoServicioActivity.this, "Error guardando el servicio");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case NOMBRE_FIELD_REF:
                nombreLabel.setText(data.getExtras().getString("TEXTO"));
                servicio.setNombre(data.getExtras().getString("TEXTO"));
                break;
            default:
                break;
        }
    }
}

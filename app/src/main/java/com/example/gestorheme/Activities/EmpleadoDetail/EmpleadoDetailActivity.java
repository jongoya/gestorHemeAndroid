package com.example.gestorheme.Activities.EmpleadoDetail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.example.gestorheme.Activities.DatePicker.DatePickerActivity;
import com.example.gestorheme.Activities.TextInputField.TextInputFieldActivity;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Common.Preferencias;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmpleadoDetailActivity extends AppCompatActivity {
    private static final int NOMBRE_FIELD_REF = 0;
    private static final int APELLIDOS_FIELD_REF = 1;
    private static final int TELEFONO_FIELD_REF = 2;
    private static final int FECHA_FIELD_REF = 3;
    private static final int EMAIL_FIELD_REF = 4;

    private RelativeLayout nombreLayout;
    private RelativeLayout apellidosLayout;
    private RelativeLayout fechaLayout;
    private RelativeLayout telefonoLayout;
    private RelativeLayout emailLayout;
    private RelativeLayout saveButton;
    private RelativeLayout callButton;
    private ConstraintLayout rootLayout;
    private RelativeLayout loadingState;

    private TextView nombreLabel;
    private TextView apellidosLabel;
    private TextView fechaLabel;
    private TextView telefonoLabel;
    private TextView emailLabel;

    private ImageView saveImage;
    private ImageView callImage;

    private EmpleadoModel empleado;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empleado_detail_layout);
        getFields();
        getEmpleadoIntent();
        setOnClickListeners();

        if (empleado != null) {
            setContent();
        } else {
            empleado = new EmpleadoModel();
            callButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getEmpleadoIntent() {
        empleado = (EmpleadoModel) getIntent().getSerializableExtra("empleado");
    }

    private void getFields() {
        nombreLayout = findViewById(R.id.nombreLayout);
        apellidosLayout = findViewById(R.id.apellidoLayout);
        fechaLayout = findViewById(R.id.fechaLayout);
        telefonoLayout = findViewById(R.id.telefonoLayout);
        emailLayout = findViewById(R.id.emailLayout);
        saveButton = findViewById(R.id.saveButton);
        callButton = findViewById(R.id.callButton);
        nombreLabel = findViewById(R.id.nombreLabel);
        apellidosLabel = findViewById(R.id.apellidosLabel);
        fechaLabel = findViewById(R.id.fechaLabel);
        telefonoLabel = findViewById(R.id.telefonoLabel);
        emailLabel = findViewById(R.id.emailLabel);
        saveImage = findViewById(R.id.saveImage);
        callImage = findViewById(R.id.callImage);
        rootLayout = findViewById(R.id.root);
    }

    private void setOnClickListeners() {
        nombreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", nombreLabel.getText().toString());
                intent.putExtra("keyboard", InputType.TYPE_CLASS_TEXT);
                startActivityForResult(intent, NOMBRE_FIELD_REF);
            }
        });

        apellidosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", apellidosLabel.getText().toString());
                intent.putExtra("keyboard", InputType.TYPE_CLASS_TEXT);
                startActivityForResult(intent, APELLIDOS_FIELD_REF);
            }
        });

        fechaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DatePickerActivity.class);
                intent.putExtra("timestamp", empleado.getFecha());
                startActivityForResult(intent, FECHA_FIELD_REF);
            }
        });

        telefonoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", telefonoLabel.getText().toString());
                intent.putExtra("keyboard", InputType.TYPE_CLASS_PHONE);
                startActivityForResult(intent, TELEFONO_FIELD_REF);
            }
        });

        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", emailLabel.getText().toString());
                intent.putExtra("keyboard", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                startActivityForResult(intent, EMAIL_FIELD_REF);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFields();
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:" + empleado.getTelefono()));
                if (ActivityCompat.checkSelfPermission(EmpleadoDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    CommonFunctions.showGenericAlertMessage(EmpleadoDetailActivity.this, "No dispone de permisos para realizar llamadas");
                    return;
                }

                startActivity(phoneIntent);
            }
        });
    }

    private void setContent() {
        nombreLabel.setText(empleado.getNombre());
        apellidosLabel.setText(empleado.getApellidos());
        fechaLabel.setText(DateFunctions.convertTimestampToBirthdayString(empleado.getFecha()));
        telefonoLabel.setText(empleado.getTelefono());
        emailLabel.setText(empleado.getEmail());
    }

    private void checkFields() {
        if (empleado.getNombre().length() == 0) {
            CommonFunctions.showGenericAlertMessage(EmpleadoDetailActivity.this, "Debe incluir un nombre");
            return;
        }

        if (empleado.getApellidos().length() == 0) {
            CommonFunctions.showGenericAlertMessage(EmpleadoDetailActivity.this, "Debe incluir los apellidos");
            return;
        }

        if (empleado.getFecha() == 0) {
            CommonFunctions.showGenericAlertMessage(EmpleadoDetailActivity.this, "Debe incluir una fecha de nacimiento");
            return;
        }

        if (empleado.getTelefono().length() < 9) {
            CommonFunctions.showGenericAlertMessage(EmpleadoDetailActivity.this, "Debe incluir un teléfono válido");
            return;
        }

        if (empleado.getEmail().length() == 0) {
            CommonFunctions.showGenericAlertMessage(EmpleadoDetailActivity.this, "Debe incluir un email");
            return;
        }

        if (empleado.getEmpleadoId() == 0) {
            saveEmpleado(empleado);
        } else {
            updateEmpleado(empleado);
        }
    }

    private void saveEmpleado(EmpleadoModel empleado) {
        loadingState = CommonFunctions.createLoadingStateView(getApplicationContext(), "Guardando empleado");
        rootLayout.addView(loadingState);
        empleado.setEmpleadoJefe(false);
        empleado.setComercioId(Preferencias.getComercioIdFromSharedPreferences(getApplicationContext()));
        Call<EmpleadoModel> call = Constants.webServices.saveEmpleado(empleado);
        call.enqueue(new Callback<EmpleadoModel>() {
            @Override
            public void onResponse(Call<EmpleadoModel> call, Response<EmpleadoModel> response) {
                rootLayout.removeView(loadingState);
                if (response.code() == 201) {
                    Constants.databaseManager.empleadosManager.addEmpleadoToDatabase(response.body());
                    EmpleadoDetailActivity.super.onBackPressed();
                }  else if (response.code() == Constants.logoutResponseValue) {
                    CommonFunctions.logout(EmpleadoDetailActivity.this);
                } else {
                    CommonFunctions.showGenericAlertMessage(EmpleadoDetailActivity.this, "Error guardando el empleado");
                }
            }

            @Override
            public void onFailure(Call<EmpleadoModel> call, Throwable t) {
                rootLayout.removeView(loadingState);
                CommonFunctions.showGenericAlertMessage(EmpleadoDetailActivity.this, "Error guardando el empleado");
            }
        });
    }

    private void updateEmpleado(EmpleadoModel empleado) {
        loadingState = CommonFunctions.createLoadingStateView(getApplicationContext(), "Actualizando empleado");
        rootLayout.addView(loadingState);
        Call<EmpleadoModel> call = Constants.webServices.updateEmpleado(empleado);
        call.enqueue(new Callback<EmpleadoModel>() {
            @Override
            public void onResponse(Call<EmpleadoModel> call, Response<EmpleadoModel> response) {
                rootLayout.removeView(loadingState);
                if (response.code() == 200) {
                    Constants.databaseManager.empleadosManager.updateEmpleadoInDatabase(response.body());
                    EmpleadoDetailActivity.super.onBackPressed();
                } else if (response.code() == Constants.logoutResponseValue) {
                    CommonFunctions.logout(EmpleadoDetailActivity.this);
                } else {
                    CommonFunctions.showGenericAlertMessage(EmpleadoDetailActivity.this, "Error actualizando el empleado");
                }
            }

            @Override
            public void onFailure(Call<EmpleadoModel> call, Throwable t) {
                rootLayout.removeView(loadingState);
                CommonFunctions.showGenericAlertMessage(EmpleadoDetailActivity.this, "Error actualizando el empleado");
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
                empleado.setNombre(data.getExtras().getString("TEXTO"));
                break;
            case APELLIDOS_FIELD_REF:
                apellidosLabel.setText(data.getExtras().getString("TEXTO"));
                empleado.setApellidos(data.getExtras().getString("TEXTO"));
                break;
            case TELEFONO_FIELD_REF:
                telefonoLabel.setText(data.getExtras().getString("TEXTO"));
                empleado.setTelefono(data.getExtras().getString("TEXTO"));
                break;
            case EMAIL_FIELD_REF:
                emailLabel.setText(data.getExtras().getString("TEXTO"));
                empleado.setEmail(data.getExtras().getString("TEXTO"));
                break;
            case FECHA_FIELD_REF:
                fechaLabel.setText(DateFunctions.convertTimestampToBirthdayString(data.getLongExtra("timestamp", 0)));
                empleado.setFecha(data.getLongExtra("timestamp", 0));
                break;
            default:
                break;
        }
    }
}

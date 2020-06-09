package com.example.gestorheme.Activities.Login;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorheme.Activities.Login.Adapter.DeviceListAdapter;
import com.example.gestorheme.Activities.Main.MainActivity;
import com.example.gestorheme.BuildConfig;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.Preferencias;
import com.example.gestorheme.Models.Dispositivo.DispositivoModel;
import com.example.gestorheme.Models.Login.LoginModel;
import com.example.gestorheme.Models.LoginMasDispositivos.LoginMasDispositivosModel;
import com.example.gestorheme.R;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText usuarioField;
    private EditText passwordField;
    private RelativeLayout rootLayout;
    private RelativeLayout loadingStateView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        getFields();
        setOnClickListener();
    }

    private void getFields() {
        loginButton = findViewById(R.id.loginButton);
        usuarioField = findViewById(R.id.usuarioField);
        passwordField = findViewById(R.id.passwordField);
        rootLayout = findViewById(R.id.root);
    }

    private void setOnClickListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFields();
            }
        });
    }

    private void checkFields() {
        if (usuarioField.getText().length() == 0) {
            CommonFunctions.showGenericAlertMessage(this, "Debe rellenar los campos");
            return;
        }

        if (passwordField.getText().length() == 0) {
            CommonFunctions.showGenericAlertMessage(this, "Debe rellenar los campos");
            return;
        }

        LoginModel login = new LoginModel(usuarioField.getText().toString(), passwordField.getText().toString(), BuildConfig.APPLICATION_ID.replace(".", ""), CommonFunctions.getDeviceModel(), CommonFunctions.getDeviceId(getApplicationContext()));
        login(login);
    }

    private void login(LoginModel login) {
        loadingStateView = CommonFunctions.createLoadingStateView(getApplicationContext(), "Iniciando sesión");
        rootLayout.addView(loadingStateView);
        Call<LoginMasDispositivosModel> call = Constants.webServices.login(login);
        call.enqueue(new Callback<LoginMasDispositivosModel>() {
            @Override
            public void onResponse(Call<LoginMasDispositivosModel> call, Response<LoginMasDispositivosModel> response) {
                rootLayout.removeView(loadingStateView);
                int code = response.code();
                switch (code) {
                    case 200:
                        saveDataAndChangeActivity(response.body().getLogin());
                        break;
                    case 404:
                        CommonFunctions.showGenericAlertMessage(LoginActivity.this, "El usuario no está registrado");
                        break;
                    case 411:
                        CommonFunctions.showGenericAlertMessage(LoginActivity.this, "El usuario no está registrado");
                        break;
                    case 412:
                        CommonFunctions.showGenericAlertMessage(LoginActivity.this, "Las credenciales del usuario no son correctas");
                        break;
                    case 413:
                        try {
                            String respuesta = response.errorBody().string();
                            LoginMasDispositivosModel model = new Gson().fromJson(respuesta,LoginMasDispositivosModel.class);
                            showDevicesFirstDialog(new LoginMasDispositivosModel(login, model.getDispositivos()));
                        } catch (Exception e) {
                            CommonFunctions.showGenericAlertMessage(LoginActivity.this, "Error iniciando sesión intentelo de nuevo");
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<LoginMasDispositivosModel> call, Throwable t) {
                rootLayout.removeView(loadingStateView);
                CommonFunctions.showGenericAlertMessage(LoginActivity.this, "Error iniciando sesión, intentelo de nuevo");
            }
        });
    }

    private void saveDataAndChangeActivity(LoginModel login) {
        Preferencias.savePasswordInSharedPreferences(getApplicationContext(), login.getPassword());
        Preferencias.saveTokenInSharedPreferences(getApplicationContext(), login.getToken());
        Preferencias.saveComercioIdInSharedPreferences(getApplicationContext(), login.getComercioId());
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDevicesFirstDialog(LoginMasDispositivosModel loginModel) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.devices_dialog_layout, null);

        Button aceptarButton  = dialogView.findViewById(R.id.aceptarButton);
        Button cancelarButton = dialogView.findViewById(R.id.cancelarButton);
        aceptarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                showDevicesSecondDialog(loginModel);
            }
        });

        cancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void showDevicesSecondDialog(LoginMasDispositivosModel loginModel) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.devices_dialog_layout2, null);
        ListView devicesList = dialogView.findViewById(R.id.devicesList);

        DeviceListAdapter adapter = new DeviceListAdapter(getApplicationContext(), loginModel.getDispositivos());
        devicesList.setAdapter(adapter);
        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialogBuilder.dismiss();
                desRegistrarYRegistrarDispositivo(loginModel.getLogin(), loginModel.getDispositivos().get(i));
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void desRegistrarYRegistrarDispositivo(LoginModel login, DispositivoModel dispositivo) {
        ArrayList<DispositivoModel> dispositivos = new ArrayList<>();
        dispositivos.add(dispositivo);
        LoginMasDispositivosModel model = new LoginMasDispositivosModel(login, dispositivos);

        loadingStateView = CommonFunctions.createLoadingStateView(getApplicationContext(), "Iniciando sesión");
        rootLayout.addView(loadingStateView);
        Call<LoginMasDispositivosModel> call = Constants.webServices.swapDevicesAndLogin(model);
        call.enqueue(new Callback<LoginMasDispositivosModel>() {
            @Override
            public void onResponse(Call<LoginMasDispositivosModel> call, Response<LoginMasDispositivosModel> response) {
                rootLayout.removeView(loadingStateView);
                if (response.code() == 200) {
                    saveDataAndChangeActivity(response.body().getLogin());
                } else {
                    CommonFunctions.showGenericAlertMessage(LoginActivity.this, "Error iniciando sesión, intentelo de nuevo");
                }
            }

            @Override
            public void onFailure(Call<LoginMasDispositivosModel> call, Throwable t) {
                rootLayout.removeView(loadingStateView);
                CommonFunctions.showGenericAlertMessage(LoginActivity.this, "Error iniciando sesión, intentelo de nuevo");
            }
        });
    }
}

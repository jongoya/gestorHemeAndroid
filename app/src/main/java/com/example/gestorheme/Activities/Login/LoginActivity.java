package com.example.gestorheme.Activities.Login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorheme.Activities.Main.MainActivity;
import com.example.gestorheme.BuildConfig;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.Preferencias;
import com.example.gestorheme.Models.Login.LoginModel;
import com.example.gestorheme.R;

import retrofit2.Call;
import retrofit2.Callback;
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
        String modelo = CommonFunctions.getDeviceModel();
        LoginModel login = new LoginModel(usuarioField.getText().toString(), passwordField.getText().toString(), BuildConfig.APPLICATION_ID.replace(".", ""), getDeviceId());
        login(login);
    }



    private String getDeviceId() {
        return Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void login(LoginModel login) {
        loadingStateView = CommonFunctions.createLoadingStateView(getApplicationContext(), "Iniciando sesión");
        rootLayout.addView(loadingStateView);
        Call<LoginModel> call = Constants.webServices.login(login);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                rootLayout.removeView(loadingStateView);
                int code = response.code();
                switch (code) {
                    case 200:
                        saveDataAndChangeActivity(response.body());
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
                        CommonFunctions.showGenericAlertMessage(LoginActivity.this, "Ha superado el limite de dispostivos permitidos");
                        //TODO gestion de dispositivos registrados
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
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
}

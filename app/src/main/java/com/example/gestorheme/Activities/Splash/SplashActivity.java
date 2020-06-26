package com.example.gestorheme.Activities.Splash;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorheme.Activities.Login.LoginActivity;
import com.example.gestorheme.Activities.Main.MainActivity;
import com.example.gestorheme.ApiServices.RetrofitClientInstance;
import com.example.gestorheme.ApiServices.WebServices;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.Preferencias;
import com.example.gestorheme.LocalDatabase.DatabaseManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.databaseManager = new DatabaseManager(getApplicationContext());
        AppStyle.setStatusBarColor(this);
        initializeWebServicesApi();
        checkTokenInSharedPreferences();
        if (Preferencias.checkComercioIdInSharedPreferences(getApplicationContext())) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("login", false);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initializeWebServicesApi() {
        Constants.webServices = RetrofitClientInstance.getRetrofitInstance(getApplicationContext()).create(WebServices.class);
    }

    private void checkTokenInSharedPreferences() {
        if (Preferencias.getTokenFromSharedPreferences(getApplicationContext()) == null) {
            Preferencias.saveTokenInSharedPreferences(getApplicationContext(), "");
        }
    }
}

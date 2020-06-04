package com.example.gestorheme.Activities.Splash;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorheme.Activities.Login.LoginActivity;
import com.example.gestorheme.Activities.Main.MainActivity;
import com.example.gestorheme.Common.Preferencias;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Preferencias.checkComercioIdInSharedPreferences(getApplicationContext())) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

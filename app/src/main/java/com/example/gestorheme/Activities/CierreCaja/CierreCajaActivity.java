package com.example.gestorheme.Activities.CierreCaja;

import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gestorheme.R;

import java.util.Date;

public class CierreCajaActivity extends AppCompatActivity {

    private long fecha;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.cierre_caja_layout);
        getCierreCajaIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getCierreCajaIntent() {
        fecha = getIntent().getLongExtra("fecha", 0);
    }
}

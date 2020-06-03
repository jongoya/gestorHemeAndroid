package com.example.gestorheme.Activities.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorheme.Activities.Empleados.EmpleadosActivity;
import com.example.gestorheme.R;

public class AgendaSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_settings_layout);
        setClickListeners();
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

package com.example.gestorheme.Activities.TipoServicios;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorheme.Activities.NuevoTipoServicio.NuevoTipoServicioActivity;
import com.example.gestorheme.Activities.TipoServicios.Adapter.TipoServiciosListAdapter;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.TipoServicio.TipoServicioModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

public class TipoServiciosActivity extends AppCompatActivity {
    private ListView tipoServiciosList;
    private RelativeLayout addButton;
    private ImageView addImage;

    private TipoServiciosListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tipo_servicios_activity_layout);
        getFields();
        customizeButton();
        setOnClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setList();
    }

    private void getFields() {
        tipoServiciosList = findViewById(R.id.tipoServicio_list);
        addButton = findViewById(R.id.saveButton);
        addImage = findViewById(R.id.saveImage);
    }

    private void customizeButton() {
        CommonFunctions.selectLayout(getApplicationContext(), addButton, addImage);
    }

    private void setOnClickListener() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TipoServiciosActivity.this, NuevoTipoServicioActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setList() {
        ArrayList<TipoServicioModel> servicios = Constants.databaseManager.tipoServiciosManager.getTipoServiciosFromDatabase();
        adapter =new TipoServiciosListAdapter(getApplicationContext(), servicios);
        tipoServiciosList.setAdapter(adapter);
        tipoServiciosList.setDivider(null);
    }
}

package com.example.gestorheme.Activities.StadisticaDetail;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorheme.Activities.StadisticaDetail.Adapter.StadisticaDetailListAdapter;
import com.example.gestorheme.Activities.Stadisticas.Views.StadisticaView;
import com.example.gestorheme.Models.CierreCaja.CierreCajaModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

public class StadisticaDetailActivity extends AppCompatActivity {
    private RelativeLayout chartLayout;
    private ListView stadisticaList;

    private boolean isMensual;
    private boolean isAnual;
    private String titulo;
    private ArrayList<CierreCajaModel> cierreCajas;
    private StadisticaDetailListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statdistica_detail_layout);
        getStadisticaIntent();
        getFields();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addChart();
        initializeList();
    }

    private void getStadisticaIntent() {
        isMensual = getIntent().getBooleanExtra("isMensual", false);
        isAnual = getIntent().getBooleanExtra("isAnual", false);
        titulo = getIntent().getStringExtra("titulo");
        cierreCajas = (ArrayList<CierreCajaModel>) getIntent().getSerializableExtra("cierreCajas");
    }

    private void getFields() {
        chartLayout = findViewById(R.id.chartLayout);
        stadisticaList = findViewById(R.id.stadisticaList);
    }

    private void addChart() {
        StadisticaView totalCajaView = new StadisticaView(getApplicationContext(), cierreCajas, titulo, isMensual, isAnual);
        chartLayout.addView(totalCajaView);
    }

    private void initializeList() {
        adapter = new StadisticaDetailListAdapter(getApplicationContext(), cierreCajas, isMensual, isAnual, titulo);
        stadisticaList.setAdapter(adapter);
        stadisticaList.setDivider(null);
    }
}

package com.example.gestorheme.Activities.Empleados;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.gestorheme.Activities.EmpleadoDetail.EmpleadoDetailActivity;
import com.example.gestorheme.Activities.Empleados.Adapter.EmpleadosListAdapter;
import com.example.gestorheme.Activities.Empleados.Interfaces.EmpleadosListInterface;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

public class EmpleadosActivity extends AppCompatActivity implements EmpleadosListInterface {
    private ListView empleadosList;
    private RelativeLayout plusButton;
    private ImageView plusImage;
    private ConstraintLayout rootLayout;
    private RelativeLayout loadingState;

    private ArrayList<EmpleadoModel> empleados;
    private EmpleadosListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empleados_layout);
        getFields();
        customizeButtons();
        setOnClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setList();
    }

    private void getFields() {
        empleadosList = findViewById(R.id.empleadosList);
        plusButton = findViewById(R.id.plusButton);
        plusImage = findViewById(R.id.plusImage);
        rootLayout = findViewById(R.id.root);
    }

    private void customizeButtons() {
        CommonFunctions.selectLayout(getApplicationContext(), plusButton, plusImage);
    }

    private void setOnClickListeners() {
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmpleadosActivity.this, EmpleadoDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setList() {
        empleados = Constants.databaseManager.empleadosManager.getEmpleadosFromDatabase();
        adapter = new EmpleadosListAdapter(getApplicationContext(), empleados, false, this);
        empleadosList.setAdapter(adapter);
        empleadosList.setDivider(null);
    }

    @Override
    public void showErrorMessage(String message) {
        CommonFunctions.showGenericAlertMessage(this, message);
    }

    @Override
    public void showLoadingState() {
        loadingState = CommonFunctions.createLoadingStateView(getApplicationContext());
        rootLayout.addView(loadingState);
    }

    @Override
    public void hideLoadingState() {
        rootLayout.removeView(loadingState);
    }

    @Override
    public void reloadList() {
        setList();
    }
}

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.gestorheme.Activities.EmpleadoDetail.EmpleadoDetailActivity;
import com.example.gestorheme.Activities.Empleados.Adapter.EmpleadosListAdapter;
import com.example.gestorheme.Activities.Empleados.Interfaces.EmpleadosListInterface;
import com.example.gestorheme.Activities.Empleados.Interfaces.EmpleadosRefreshInterface;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.SyncronizationManager;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.R;
import java.util.ArrayList;

public class EmpleadosActivity extends AppCompatActivity implements EmpleadosListInterface, EmpleadosRefreshInterface {
    private ListView empleadosList;
    private RelativeLayout plusButton;
    private ImageView plusImage;
    private ConstraintLayout rootLayout;
    private RelativeLayout loadingState;
    private SwipeRefreshLayout refreshLayout;

    private ArrayList<EmpleadoModel> empleados;
    private EmpleadosListAdapter adapter;
    private boolean showColorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empleados_layout);
        AppStyle.setStatusBarColor(this);
        getFields();
        getEmpleadosIntent();
        customizeButtons();
        customizeBackground();
        setOnClickListeners();
        setRefreshLayoutListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setList();
        if (showColorLayout) {
            plusButton.setVisibility(View.INVISIBLE);
        }
    }

    private void getEmpleadosIntent() {
        showColorLayout = getIntent().getBooleanExtra("showColorLayout", false);
    }

    private void getFields() {
        empleadosList = findViewById(R.id.empleadosList);
        plusButton = findViewById(R.id.plusButton);
        plusImage = findViewById(R.id.plusImage);
        rootLayout = findViewById(R.id.root);
        refreshLayout = findViewById(R.id.refreshLayout);
    }

    private void setRefreshLayoutListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SyncronizationManager.getAllEmpleados(EmpleadosActivity.this, getApplicationContext());
            }
        });
    }

    private void customizeButtons() {
        CommonFunctions.customizeViewWithImage(getApplicationContext(), plusButton, plusImage, AppStyle.getPrimaryColor(), AppStyle.getPrimaryColor());
    }

    private void customizeBackground() {
        rootLayout.setBackgroundColor(AppStyle.getBackgroundColor());
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
        adapter = new EmpleadosListAdapter(getApplicationContext(), empleados, showColorLayout, this);
        empleadosList.setAdapter(adapter);
        empleadosList.setDivider(null);
    }

    @Override
    public void showErrorMessage(String message) {
        CommonFunctions.showGenericAlertMessage(this, message);
    }

    @Override
    public void showLoadingState(String description) {
        loadingState = CommonFunctions.createLoadingStateView(getApplicationContext(), description);
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

    @Override
    public void logout() {
        CommonFunctions.logout(EmpleadosActivity.this);
    }

    @Override
    public void empleadosLoaded() {
        refreshLayout.setRefreshing(false);
        setList();
    }

    @Override
    public void errorLoadingEmpleados() {
        refreshLayout.setRefreshing(false);
    }
}

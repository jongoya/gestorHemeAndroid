package com.example.gestorheme.Activities.Stadisticas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorheme.Activities.StadisticaDetail.StadisticaDetailActivity;
import com.example.gestorheme.Activities.Stadisticas.Common.StadisticasFunctions;
import com.example.gestorheme.Activities.Stadisticas.Interfaces.IStadisticasFilterInterface;
import com.example.gestorheme.Activities.Stadisticas.Views.StadisticaView;
import com.example.gestorheme.Activities.Stadisticas.Views.StadisticasFilterActionSheet;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Models.CierreCaja.CierreCajaModel;
import com.example.gestorheme.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StadisticasActivity extends AppCompatActivity  implements IStadisticasFilterInterface {
    private LinearLayout scrollContentView;
    private RelativeLayout filterButton;
    private TextView filterText;
    private BottomSheetDialog filterDialog;

    private boolean isMensual = true;
    private boolean isAnual = false;
    private Date presentDate = new Date();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stadisticas_activity_layout);
        getFields();
        customizeButton();
        setOnClickListeners();
        setButtonText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addCharts();
    }

    private void getFields() {
        scrollContentView = findViewById(R.id.scrollContent);
        filterButton  = findViewById(R.id.filterButton);
        filterText = findViewById(R.id.filterText);
    }

    private void customizeButton() {
        CommonFunctions.customizeView(getApplicationContext(), filterButton, R.color.dividerColor);
    }

    private void setOnClickListeners() {
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog();
            }
        });
    }

    private void setButtonText() {
        if (!isMensual && !isAnual) {
            filterText.setText("Total");
            return;
        }

        if (isAnual) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(presentDate);
            filterText.setText(String.valueOf(calendar.get(Calendar.YEAR)));
            return;
        }

        if (isMensual) {
            filterText.setText(new SimpleDateFormat( "LLLL", Locale.getDefault()).format(presentDate));
        }
    }

    private void addCharts() {
        ArrayList<CierreCajaModel> cierreCajas = new ArrayList<>();

        if (isMensual) {
            long fechaInicio = DateFunctions.getBeginingOfMonthFromDate(presentDate).getTime();
            long fechaFin = DateFunctions.getEndOfMonthFromDate(presentDate).getTime();
            cierreCajas = Constants.databaseManager.cierreCajaManager.getCierreCajasForRange(fechaInicio, fechaFin);
        }

        if (isAnual) {
            long fechaInicio = DateFunctions.getBeginingOfYearFromDate(presentDate).getTime();
            long fechaFin = DateFunctions.getEndOfYearFromDate(presentDate).getTime();
            ArrayList<CierreCajaModel> cajas = Constants.databaseManager.cierreCajaManager.getCierreCajasForRange(fechaInicio, fechaFin);
            cierreCajas = StadisticasFunctions.groupCierreCajasAnual(cajas);
        }

        if (!isMensual && !isAnual) {
            ArrayList<CierreCajaModel> cajas = Constants.databaseManager.cierreCajaManager.getCierreCajasFromDatabase();
            cierreCajas = StadisticasFunctions.groupCierreCajasTotal(cajas);
        }

        StadisticaView numeroServiciosView = new StadisticaView(getApplicationContext(), cierreCajas, "Numero Servicios", isMensual, isAnual);
        ArrayList<CierreCajaModel> finalCierreCajas = cierreCajas;
        numeroServiciosView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStadisticasDetailActivity(finalCierreCajas, "Numero Servicios");
            }
        });
        scrollContentView.addView(numeroServiciosView);

        StadisticaView totalCajaView = new StadisticaView(getApplicationContext(), cierreCajas, "Total Caja", isMensual, isAnual);
        totalCajaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStadisticasDetailActivity(finalCierreCajas, "Total Caja");
            }
        });
        scrollContentView.addView(totalCajaView);

        StadisticaView totalProductosView = new StadisticaView(getApplicationContext(), cierreCajas, "Total Productos", isMensual, isAnual);
        totalProductosView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStadisticasDetailActivity(finalCierreCajas, "Total Productos");
            }
        });
        scrollContentView.addView(totalProductosView);

        StadisticaView efectivoView = new StadisticaView(getApplicationContext(), cierreCajas, "Efectivo", isMensual, isAnual);
        efectivoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStadisticasDetailActivity(finalCierreCajas, "Efectivo");
            }
        });
        scrollContentView.addView(efectivoView);

        StadisticaView tarjetaView = new StadisticaView(getApplicationContext(), cierreCajas, "Tarjeta", isMensual, isAnual);
        tarjetaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStadisticasDetailActivity(finalCierreCajas, "Tarjeta");
            }
        });
        scrollContentView.addView(tarjetaView);
    }

    private void openStadisticasDetailActivity(ArrayList<CierreCajaModel> cierreCajas, String titulo) {
        Intent intent = new Intent(StadisticasActivity.this, StadisticaDetailActivity.class);
        intent.putExtra("isMensual", isMensual);
        intent.putExtra("isAnual", isAnual);
        intent.putExtra("titulo", titulo);
        intent.putExtra("cierreCajas", cierreCajas);
        startActivity(intent);
    }

    private void showFilterDialog() {
        try {
            LinearLayout filterActionView = new StadisticasFilterActionSheet(getApplicationContext(), presentDate, this);
            filterDialog = new BottomSheetDialog(this);
            filterDialog.setContentView(filterActionView);
            filterDialog.show();
            FrameLayout bottomSheet = filterDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            bottomSheet.setBackground(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mensualButtonClicked(Date date) {
        filterDialog.hide();
        scrollContentView.removeAllViews();
        isMensual = true;
        isAnual = false;
        presentDate = date;
        setButtonText();
        addCharts();
    }

    @Override
    public void anualButtonClicked(Date date) {
        filterDialog.hide();
        scrollContentView.removeAllViews();
        isMensual = false;
        isAnual = true;
        presentDate = date;
        setButtonText();
        addCharts();
    }

    @Override
    public void totalButtonClicked() {
        filterDialog.hide();
        scrollContentView.removeAllViews();
        isMensual = false;
        isAnual = false;
        setButtonText();
        presentDate = new Date();
        addCharts();
    }
}

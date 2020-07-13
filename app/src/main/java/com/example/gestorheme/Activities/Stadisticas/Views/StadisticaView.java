package com.example.gestorheme.Activities.Stadisticas.Views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gestorheme.Activities.Stadisticas.ValueFormatter.AxisFormatter;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Models.CierreCaja.CierreCajaModel;
import com.example.gestorheme.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class StadisticaView extends RelativeLayout {
    private TextView titulo;
    private TextView valor;
    private LineChart chart;

    private ArrayList<CierreCajaModel> valores;

    private boolean isMensual;
    private boolean isAnual;
    private String title;

    public StadisticaView(Context context, ArrayList<CierreCajaModel> valores, String titulo, boolean isMensual, boolean isAnual) {
        super(context);
        View.inflate(context, R.layout.statdistica_view_layout, this);
        this.valores = valores;
        this.title = titulo;
        this.isMensual = isMensual;
        this.isAnual = isAnual;
        getFields();
        setFields();
        addChart();
    }

    private void getFields() {
        chart = findViewById(R.id.chart);
        titulo = findViewById(R.id.titulo);
        valor = findViewById(R.id.valor);
    }

    private void setFields() {
        titulo.setText(title);
        titulo.setTextColor(AppStyle.getPrimaryColor());
        if (valores.size() == 0) {
            valor.setText("No hay valores para este rango");
            return;
        }

        if (title.equals("Numero Servicios")) {
            valor.setText(String.valueOf((int) getValorForChart()));
        } else {
            valor.setText(new DecimalFormat("#0.00").format(getValorForChart()) + " €");
        }
    }

    private double getValorForChart() {
        switch (title) {
            case "Numero Servicios":
                return valores.get(valores.size() -1).getNumeroServicios();
            case "Total Caja" :
                return valores.get(valores.size() -1).getTotalCaja();
            case "Total Productos":
                return valores.get(valores.size() -1).getTotalProductos();
            case  "Efectivo":
                return valores.get(valores.size() -1).getEfectivo();
            case "Tarjeta":
                return valores.get(valores.size() -1).getTarjeta();
            default:
                return 0.0;
        }
    }

    private double getChartValueForPosition(int position) {
        switch (title) {
            case "Numero Servicios":
                return valores.get(position).getNumeroServicios();
            case "Total Caja" :
                return valores.get(position).getTotalCaja();
            case "Total Productos":
                return valores.get(position).getTotalProductos();
            case  "Efectivo":
                return valores.get(position).getEfectivo();
            case "Tarjeta":
                return valores.get(position).getTarjeta();
            default:
                return 0.0;
        }
    }

    private void addChart() {
        chart.setEnabled(false);
        chart.setClickable(false);
        chart.setTouchEnabled(false);
        chart.setNoDataText("No hay valores para esta grafica");
        customizeChart();
        Collections.sort(valores, (o1, o2) -> Long.compare(o1.getFecha(), o2.getFecha()));
        ArrayList<Entry> dataEntries = new ArrayList<>();

        for (int i = 0; i < valores.size(); i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(valores.get(i).getFecha()));
            float xValue = calendar.get(Calendar.YEAR);

            if (isMensual) {
                xValue = calendar.get(Calendar.DAY_OF_MONTH);
            }

            if (isAnual) {
                xValue = calendar.get(Calendar.MONTH);
            }

            Entry entry = new Entry(xValue, (float) getChartValueForPosition(i));
            dataEntries. add(entry);
        }


        LineData lineData = createDataSet(dataEntries);
        chart.setData(lineData);
    }

    private void customizeChart() {
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.animateXY(500, 500);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setGridColor(Color.TRANSPARENT);
        chart.getXAxis().setValueFormatter(new AxisFormatter(isAnual));
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.setExtraLeftOffset(20);
        chart.setExtraRightOffset(20);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setTextSize(12);
        chart.getXAxis().setTypeface(Typeface.DEFAULT_BOLD);
        chart.getXAxis().setTextColor(AppStyle.getPrimaryColor());
    }

    private LineData createDataSet(ArrayList<Entry> dataEntries) {
        LineDataSet dataSet = new LineDataSet(dataEntries, title);
        dataSet.setColor(AppStyle.getPrimaryColor());
        dataSet.setLineWidth(2);
        dataSet.setCircleColor(AppStyle.getPrimaryColor());
        dataSet.setCircleRadius(6);
        dataSet.setCircleHoleColor(AppStyle.getPrimaryColor());
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setValueTextSize(10);
        dataSet.setHighLightColor(AppStyle.getPrimaryColor());
        dataSet.setHighlightEnabled(false);

        return new LineData(dataSet);
    }
}

package com.example.gestorheme.Activities.Stadisticas.Views;

import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.example.gestorheme.Activities.Stadisticas.Interfaces.IStadisticasFilterInterface;
import com.example.gestorheme.R;

import java.util.Calendar;
import java.util.Date;

public class StadisticasFilterActionSheet extends LinearLayout {
    private DatePicker datePicker;
    private Date presentDate;
    private IStadisticasFilterInterface delegate;

    public StadisticasFilterActionSheet(Context context, Date presentDate, IStadisticasFilterInterface delegate) {
        super(context);
        View.inflate(context, R.layout.stadisticas_filter_layout, this);
        this.presentDate = presentDate;
        this.delegate = delegate;
        setOnClickListeners();
        getFields();
        customizeDatePicker();
    }

    private void getFields() {
        datePicker = findViewById(R.id.datePicker);
    }

    private void customizeDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(presentDate);
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(presentDate);
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                presentDate = calendar.getTime();
            }
        });
    }

    public void setOnClickListeners() {
        findViewById(R.id.totalButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.totalButtonClicked();
            }
        });

        findViewById(R.id.mensualButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.mensualButtonClicked(presentDate);
            }
        });

        findViewById(R.id.anualButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.anualButtonClicked(presentDate);
            }
        });
    }
}

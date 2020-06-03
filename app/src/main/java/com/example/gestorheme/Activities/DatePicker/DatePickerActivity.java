package com.example.gestorheme.Activities.DatePicker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gestorheme.R;
import java.util.Calendar;
import java.util.Date;

public class DatePickerActivity extends AppCompatActivity {
    private DatePicker datePicker;
    private TimePicker timePicker;

    private boolean showTimePicker = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_picker_layout);
        getFields();
        getDatePickerIntent();
    }

    @Override
    public void onBackPressed() {
        Intent i = getIntent();
        i.putExtra("timestamp", getTime());
        setResult(RESULT_OK, i);
        super.onBackPressed();
    }

    private void getFields() {
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
    }

    private void getDatePickerIntent() {
        long timestamp = getIntent().getLongExtra("timestamp", 0);
        showTimePicker = getIntent().getBooleanExtra("showTimePicker", false);
        timePicker.setVisibility(showTimePicker ? View.VISIBLE : View.INVISIBLE);

        Date date;
        if (timestamp == 0) {
            date = new Date();
        } else {
            date = new Date(timestamp);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        if (showTimePicker) {
            timePicker.setHour(calendar.get(Calendar.HOUR));
            timePicker.setMinute(calendar.get(Calendar.MINUTE));
            timePicker.setIs24HourView(true);
        }
    }

    private long getTime(){
        Calendar cal = Calendar.getInstance();
        cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        if (!showTimePicker) {
            cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        } else {
            cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute());
        }

        return cal.getTime().getTime();
    }
}

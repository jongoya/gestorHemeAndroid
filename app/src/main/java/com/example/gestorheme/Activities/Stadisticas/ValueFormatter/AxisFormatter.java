package com.example.gestorheme.Activities.Stadisticas.ValueFormatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class AxisFormatter extends ValueFormatter {
    float actualValue = 0;
    boolean isAnual;

    public AxisFormatter(boolean isAnual) {
        this.isAnual = isAnual;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        if (actualValue != value) {
            actualValue = value;
            return isAnual ? getMonthNameForMonthNumber((int)value) : String.valueOf((int)value);
        }

        return "";
    }

    private String getMonthNameForMonthNumber(int monthNumber) {
        switch (monthNumber) {
            case 0:
                return "Ene";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Abr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Ago";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            default:
                return "Dic";
        }
    }
}

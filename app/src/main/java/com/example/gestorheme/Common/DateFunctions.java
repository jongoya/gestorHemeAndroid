package com.example.gestorheme.Common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFunctions {

    public static String convertTimestampToBirthdayString(long timestamp) {
        SimpleDateFormat sf = new SimpleDateFormat("dd/MMMM/yyyy", Locale.getDefault());
        Date date = new Date(timestamp);
        return  sf.format(date);
    }

    public static String convertTimestampToServiceDateString(long timestamp) {
        SimpleDateFormat sf = new SimpleDateFormat("dd/MMMM/yyyy HH:mm", Locale.getDefault());
        Date date = new Date(timestamp);
        return  sf.format(date);
    }
}

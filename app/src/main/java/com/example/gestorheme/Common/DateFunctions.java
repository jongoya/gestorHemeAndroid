package com.example.gestorheme.Common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static String getHoursAndMinutesFromDate(long timestamp) {
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date date = new Date(timestamp);
        return  sf.format(date);
    }

    public static Date getBeginingOfWorkingDayFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    public static Date getEndOfWorkingDayFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 21);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    public static Date add15MinsToDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 15);
        return calendar.getTime();
    }

    public static Date getBeginingOfDayFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    public static Date getEndOfDayFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    public static Date add2MonthsToDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 2);
        return calendar.getTime();
    }

    public static Date remove2MonthsToDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -2);
        return calendar.getTime();
    }

    public static Date remove1DayToDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }



}

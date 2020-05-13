package com.example.gestorheme.LocalDatabase.Managers.CierreCaja;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;

import java.util.Date;

public class CierreCajaManager {
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;

    public CierreCajaManager(SQLiteDatabase writableDatabase, SQLiteDatabase readableDatabase) {
        this.writableDatabase = writableDatabase;
        this.readableDatabase = readableDatabase;
    }

    public boolean cierreCajaRealizadoEnFecha(Date fecha) {
        long begginingOfDay = DateFunctions.getBeginingOfDayFromDate(fecha).getTime();
        long endOfDay = DateFunctions.getEndOfDayFromDate(fecha).getTime();

        String Query = "Select * from " + Constants.databaseCierreCajaTableName + " where " + Constants.databaseFecha + " > " + begginingOfDay + " AND " + Constants.databaseFecha + " <= " + endOfDay;
        Cursor cursor = readableDatabase.rawQuery(Query, null);
        if (cursor.moveToNext()) {
            return true;
        }

        return false;
    }
}

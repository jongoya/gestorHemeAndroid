package com.example.gestorheme.LocalDatabase.Managers.Cesta;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Models.Cesta.CestaModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CestaManager {
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;

    public CestaManager(SQLiteDatabase writableDatabase, SQLiteDatabase readableDatabase) {
        this.writableDatabase = writableDatabase;
        this.readableDatabase = readableDatabase;
    }

    public ArrayList<CestaModel> getAllCestas() {
        ArrayList<CestaModel> cestas = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("select * from " + Constants.databaseCestaTableName, null);
        while (cursor.moveToNext()) {
            cestas.add(parseCursorToCestaModel(cursor));
        }

        cursor.close();
        return cestas;
    }

    public void saveCesta(CestaModel cesta) {
        if (!checkCesta(cesta.getCestaId())) {
            ContentValues cv = fillCestaDataToDatabaseObject(cesta);
            writableDatabase.insert(Constants.databaseCestaTableName, null, cv);
        }
    }

    public void updateCesta(CestaModel cesta) {
        ContentValues cv = fillCestaDataToDatabaseObject(cesta);
        writableDatabase.update(Constants.databaseCestaTableName, cv, Constants.databaseCestaId + "=" + String.valueOf(cesta.getCestaId()), null);
    }

    public CestaModel getCesta(long cestaId) {
        String Query = "Select * from " + Constants.databaseCestaTableName + " where " + Constants.databaseCestaId + " = " + cestaId;
        Cursor cursor = readableDatabase.rawQuery(Query, null);
        if (cursor.moveToNext()) {
            return parseCursorToCestaModel(cursor);
        }

        return null;
    }

    public ArrayList<CestaModel> getCestasForClientId(long clientId) {
        ArrayList<CestaModel> cestas = new ArrayList<>();
        String Query = "Select * from " + Constants.databaseCestaTableName + " where " + Constants.databaseClientId + " = " + clientId;
        Cursor cursor = readableDatabase.rawQuery(Query, null);
        while (cursor.moveToNext()) {
            cestas.add(parseCursorToCestaModel(cursor));
        }

        return cestas;
    }

    public ArrayList<CestaModel> getCestasForDay(Date date) {
        ArrayList<CestaModel> cestas = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateFunctions.getBeginingOfWorkingDayFromDate(date));
        long beginingOfDay = calendar.getTimeInMillis() / 1000;
        calendar.setTime(DateFunctions.getEndOfWorkingDayFromDate(date));
        long endOfDay = calendar.getTimeInMillis() / 1000;
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM " + Constants.databaseCestaTableName + " WHERE " + Constants.databaseFecha + " >= " + beginingOfDay + " AND " + Constants.databaseFecha + " < " + endOfDay, null);
        while (cursor.moveToNext()) {
            cestas.add(parseCursorToCestaModel(cursor));
        }

        cursor.close();
        return cestas;
    }

    public void deleteCesta(CestaModel cesta) {
        writableDatabase.delete(Constants.databaseCestaTableName, Constants.databaseCestaId + " = " + cesta.getCestaId(), null);
    }

    public void cleanDatabase() {
        writableDatabase.delete(Constants.databaseCestaTableName, null, null);
    }

    private boolean checkCesta(long cestaId) {
        String Query = "Select * from " + Constants.databaseCestaTableName + " where " + Constants.databaseCestaId + " = " + String.valueOf(cestaId);
        Cursor cursor = readableDatabase.rawQuery(Query, null);
        if (cursor.getCount() == 0){
            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }

    private CestaModel parseCursorToCestaModel(Cursor cursor) {
        CestaModel cesta = new CestaModel();
        cesta.setCestaId(cursor.getLong(cursor.getColumnIndex(Constants.databaseCestaId)));
        cesta.setClientId(cursor.getLong(cursor.getColumnIndex(Constants.databaseClientId)));
        cesta.setComercioId(cursor.getLong(cursor.getColumnIndex(Constants.databaseComercioId)));
        cesta.setEfectivo(cursor.getInt(cursor.getColumnIndex(Constants.databaseIsEfectivo)) == 1);
        cesta.setFecha(cursor.getLong(cursor.getColumnIndex(Constants.databaseFecha)));

        return cesta;
    }

    private ContentValues fillCestaDataToDatabaseObject(CestaModel cesta) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.databaseCestaId, cesta.getCestaId());
        cv.put(Constants.databaseClientId, cesta.getClientId());
        cv.put(Constants.databaseComercioId, cesta.getComercioId());
        cv.put(Constants.databaseIsEfectivo, cesta.isEfectivo() ? 1 : 0);
        cv.put(Constants.databaseFecha, cesta.getFecha());

        return cv;
    }
}

package com.example.gestorheme.LocalDatabase.Managers.CierreCaja;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Models.CierreCaja.CierreCajaModel;

import java.util.ArrayList;
import java.util.Date;

public class CierreCajaManager {
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;

    public CierreCajaManager(SQLiteDatabase writableDatabase, SQLiteDatabase readableDatabase) {
        this.writableDatabase = writableDatabase;
        this.readableDatabase = readableDatabase;
    }

    public ArrayList<CierreCajaModel> getCierreCajasFromDatabase() {
        ArrayList<CierreCajaModel> cierreCajas = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("select * from " + Constants.databaseCierreCajaTableName, null);
        while (cursor.moveToNext()) {
            cierreCajas.add(parseCursorToCierreCajaModel(cursor));
        }

        cursor.close();
        return cierreCajas;
    }

    public ArrayList<CierreCajaModel> getCierreCajasForRange(long fechaInicio, long fechaFin) {
        ArrayList<CierreCajaModel> cierreCajas = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("select * from " + Constants.databaseCierreCajaTableName + " WHERE " + Constants.databaseFecha + " >= " + fechaInicio + " AND " + Constants.databaseFecha + " < " + fechaFin, null);
        while (cursor.moveToNext()) {
            cierreCajas.add(parseCursorToCierreCajaModel(cursor));
        }

        cursor.close();
        return cierreCajas;
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

    public CierreCajaModel getCierreCajaForCierreCajaId(long cierreCajaId) {
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM " + Constants.databaseCierreCajaTableName + " WHERE " + Constants.databaseCajaId + " = " + Long.toString(cierreCajaId), null);
        while (cursor.moveToNext()) {
            CierreCajaModel cierreCaja = parseCursorToCierreCajaModel(cursor);
            cursor.close();
            return cierreCaja;
        }

        cursor.close();
        return null;
    }

    public void addCierreCajaToDatabase(CierreCajaModel cierreCaja) {
        ContentValues cv = fillCierreCajaDataToDatabaseObject(cierreCaja);
        writableDatabase.insert(Constants.databaseCierreCajaTableName, null, cv);
    }

    private ContentValues fillCierreCajaDataToDatabaseObject(CierreCajaModel cierreCaja) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.databaseCajaId, cierreCaja.getCajaId());
        cv.put(Constants.databaseFecha, cierreCaja.getFecha());
        cv.put(Constants.databaseNumeroServicios, cierreCaja.getNumeroServicios());
        cv.put(Constants.databaseTotalCaja, cierreCaja.getTotalCaja());
        cv.put(Constants.databaseTotalProductos, cierreCaja.getTotalProductos());
        cv.put(Constants.databaseEfectivo, cierreCaja.getEfectivo());
        cv.put(Constants.databaseTarjeta, cierreCaja.getTarjeta());

        return cv;
    }

    private CierreCajaModel parseCursorToCierreCajaModel(Cursor cursor) {
        CierreCajaModel cierreCaja = new CierreCajaModel();
        cierreCaja.setCajaId(cursor.getLong(cursor.getColumnIndex(Constants.databaseCajaId)));
        cierreCaja.setFecha(cursor.getLong(cursor.getColumnIndex(Constants.databaseFecha)));
        cierreCaja.setNumeroServicios(cursor.getInt(cursor.getColumnIndex(Constants.databaseNumeroServicios)));
        cierreCaja.setTotalCaja(cursor.getDouble(cursor.getColumnIndex(Constants.databaseTotalCaja)));
        cierreCaja.setTotalProductos(cursor.getDouble(cursor.getColumnIndex(Constants.databaseTotalProductos)));
        cierreCaja.setEfectivo(cursor.getDouble(cursor.getColumnIndex(Constants.databaseEfectivo)));
        cierreCaja.setTarjeta(cursor.getDouble(cursor.getColumnIndex(Constants.databaseTarjeta)));
        return cierreCaja;
    }
}

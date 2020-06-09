package com.example.gestorheme.LocalDatabase.Managers.TipoServicio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.TipoServicio.TipoServicioModel;

import java.util.ArrayList;

public class TipoServicioManager {
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;

    public TipoServicioManager(SQLiteDatabase writableDatabase, SQLiteDatabase readableDatabase) {
        this.writableDatabase = writableDatabase;
        this.readableDatabase = readableDatabase;
    }

    public ArrayList<TipoServicioModel> getTipoServiciosFromDatabase() {
        ArrayList<TipoServicioModel> servicios = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("select * from " + Constants.databaseTipoServiciosTableName, null);
        while (cursor.moveToNext()) {
            servicios.add(parseCursorToTipoServicioModel(cursor));
        }

        cursor.close();
        return servicios;
    }

    public void addTipoServicioToDatabase(TipoServicioModel servicio) {
        if (!checkTipoServicioInDatabase(servicio.getServicioId())) {
            ContentValues cv = fillTipoServicioDataToDatabaseObject(servicio);
            writableDatabase.insert(Constants.databaseTipoServiciosTableName, null, cv);
        }
    }

    public TipoServicioModel getServicioForServicioId(long servicioId) {
        String Query = "Select * from " + Constants.databaseTipoServiciosTableName + " where " + Constants.databaseServicioId + " = " + String.valueOf(servicioId);
        Cursor cursor = readableDatabase.rawQuery(Query, null);
        if (cursor.moveToNext()) {
            return parseCursorToTipoServicioModel(cursor);
        }

        return null;
    }

    private boolean checkTipoServicioInDatabase(long servicioId) {
        String Query = "Select * from " + Constants.databaseTipoServiciosTableName + " where " + Constants.databaseServicioId + " = " + String.valueOf(servicioId);
        Cursor cursor = readableDatabase.rawQuery(Query, null);
        if (cursor.getCount() == 0){
            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }

    private ContentValues fillTipoServicioDataToDatabaseObject(TipoServicioModel servicio) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.databaseServicioId, servicio.getServicioId());
        cv.put(Constants.databaseNombre, servicio.getNombre());
        cv.put(Constants.databaseComercioId, servicio.getComercioId());
        return cv;
    }

    private TipoServicioModel parseCursorToTipoServicioModel(Cursor cursor) {
        TipoServicioModel servicio = new TipoServicioModel();
        servicio.setServicioId(cursor.getLong(cursor.getColumnIndex(Constants.databaseServicioId)));
        servicio.setNombre(cursor.getString(cursor.getColumnIndex(Constants.databaseNombre)));
        servicio.setComercioId(cursor.getLong(cursor.getColumnIndex(Constants.databaseComercioId)));

        return servicio;
    }

    public void cleanDatabase() {
        writableDatabase.delete(Constants.databaseTipoServiciosTableName, null, null);
    }
}

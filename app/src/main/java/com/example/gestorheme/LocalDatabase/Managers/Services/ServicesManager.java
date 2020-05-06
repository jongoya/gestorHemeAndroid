package com.example.gestorheme.LocalDatabase.Managers.Services;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Service.ServiceModel;


public class ServicesManager {
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;

    public ServicesManager(SQLiteDatabase writableDatabase, SQLiteDatabase readableDatabase) {
        this.writableDatabase = writableDatabase;
        this.readableDatabase = readableDatabase;
    }

    public void addServiceToDatabase(ServiceModel servicio) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.databaseClientId, servicio.getClientId());
        cv.put(Constants.databaseServicioId, servicio.getServiceId());
        cv.put(Constants.databaseNombre, servicio.getNombre());
        cv.put(Constants.databaseApellidos, servicio.getApellidos());
        cv.put(Constants.databaseFecha, servicio.getFecha());
        cv.put(Constants.databaseProfesional, servicio.getProfesional());
        cv.put(Constants.databaseServicios, TextUtils.join(",", servicio.getServicios()));
        cv.put(Constants.databaseObservaciones, servicio.getObservaciones());
        cv.put(Constants.databasePrecio, servicio.getPrecio());
        writableDatabase.insert(Constants.databaseServiciosTableName, null, cv);
    }
}

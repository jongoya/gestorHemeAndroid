package com.example.gestorheme.LocalDatabase.Managers.Services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Service.ServiceModel;
import java.util.ArrayList;
import java.util.Arrays;

public class ServicesManager {
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;

    public ServicesManager(SQLiteDatabase writableDatabase, SQLiteDatabase readableDatabase) {
        this.writableDatabase = writableDatabase;
        this.readableDatabase = readableDatabase;
    }

    public void addServiceToDatabase(ServiceModel servicio) {
        if (!checkServiceInDatabase(servicio.getServiceId())) {
            ContentValues cv = fillServiceDataToDatabaseObject(servicio);
            writableDatabase.insert(Constants.databaseServiciosTableName, null, cv);
        }
    }

    public ArrayList<ServiceModel> getServicesForClient(long clientId) {
        ArrayList<ServiceModel> services = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM " + Constants.databaseServiciosTableName + " WHERE " + Constants.databaseClientId + " = " + Long.toString(clientId) + " ORDER BY " + Constants.databaseFecha + " DESC", null);
        while (cursor.moveToNext()) {
            services.add(parseCursorToServiceModel(cursor));
        }
        cursor.close();
        return services;
    }

    public void updateServiceInDatabase(ServiceModel servicio) {
        ContentValues cv = fillServiceDataToDatabaseObject(servicio);
        writableDatabase.update(Constants.databaseServiciosTableName, cv, Constants.databaseServicioId + "=" + String.valueOf(servicio.getServiceId()), null);
    }

    private boolean checkServiceInDatabase(long serviceId) {
        String Query = "Select * from " + Constants.databaseServiciosTableName + " where " + Constants.databaseServicioId + " = " + String.valueOf(serviceId);
        Cursor cursor = readableDatabase.rawQuery(Query, null);
        if (cursor.getCount() == 0){
            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }

    private ContentValues fillServiceDataToDatabaseObject(ServiceModel servicio) {
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

        return  cv;
    }

    private ServiceModel parseCursorToServiceModel(Cursor cursor) {
        ServiceModel service = new ServiceModel();
        service.setClientId(cursor.getLong(cursor.getColumnIndex(Constants.databaseClientId)));
        service.setServiceId(cursor.getLong(cursor.getColumnIndex(Constants.databaseServicioId)));
        service.setNombre(cursor.getString(cursor.getColumnIndex(Constants.databaseNombre)));
        service.setApellidos(cursor.getString(cursor.getColumnIndex(Constants.databaseApellidos)));
        service.setFecha(cursor.getLong(cursor.getColumnIndex(Constants.databaseFecha)));
        service.setProfesional(cursor.getLong(cursor.getColumnIndex(Constants.databaseProfesional)));//TODO get el nombre del profesional
        String text = cursor.getString(cursor.getColumnIndex(Constants.databaseServicios));
        service.setServicios(getServiciosFromString(text));
        service.setObservaciones(cursor.getString(cursor.getColumnIndex(Constants.databaseObservaciones)));
        service.setPrecio(cursor.getDouble(cursor.getColumnIndex(Constants.databasePrecio)));
        return service;
    }

    private ArrayList getServiciosFromString(String servicios) {
        ArrayList services = new ArrayList();
        if (servicios.length() == 0) {
            return new ArrayList();
        }

        ArrayList<String> stringServices = new ArrayList<>(Arrays.asList(servicios.split(",")));
        for (int i = 0; i < stringServices.size(); i++) {
            String service = stringServices.get(i);
            services.add(Long.parseLong(service));
        }

        return services;
    }
}

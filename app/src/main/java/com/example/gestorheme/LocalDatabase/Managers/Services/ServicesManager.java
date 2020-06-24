package com.example.gestorheme.LocalDatabase.Managers.Services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Models.Service.ServiceModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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

    public ArrayList<ServiceModel> getServicesFromDatabase() {
        ArrayList<ServiceModel> services = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM " + Constants.databaseServiciosTableName, null);
        while (cursor.moveToNext()) {
            services.add(parseCursorToServiceModel(cursor));
        }

        cursor.close();
        return services;
    }

    public ServiceModel getServiceForServiceId(long serviceId) {
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM " + Constants.databaseServiciosTableName + " WHERE " + Constants.databaseServicioId + " = " + Long.toString(serviceId), null);
        while (cursor.moveToNext()) {
            ServiceModel service = parseCursorToServiceModel(cursor);
            cursor.close();
            return service;
        }

        cursor.close();
        return null;
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

    public ArrayList<ServiceModel> getServicesForEmpleadoId(long empleadoId) {
        ArrayList<ServiceModel> services = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM " + Constants.databaseServiciosTableName + " WHERE " + Constants.databaseEmpleadoId + " = " + Long.toString(empleadoId), null);
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

    public ArrayList<ServiceModel> getServicesForDate(Date date) {
        ArrayList<ServiceModel> arrayServicios = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateFunctions.getBeginingOfWorkingDayFromDate(date));
        long beginingOfDay = calendar.getTimeInMillis() / 1000;
        calendar.setTime(DateFunctions.getEndOfWorkingDayFromDate(date));
        long endOfDay = calendar.getTimeInMillis() / 1000;
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM " + Constants.databaseServiciosTableName + " WHERE " + Constants.databaseFecha + " >= " + beginingOfDay + " AND " + Constants.databaseFecha + " < " + endOfDay, null);
        while (cursor.moveToNext()) {
            arrayServicios.add(parseCursorToServiceModel(cursor));
        }

        cursor.close();
        return arrayServicios;
    }

    public void deleteServiceFromDatabase(long serviceId) {
        writableDatabase.delete(Constants.databaseServiciosTableName, Constants.databaseServicioId + " = " + serviceId, null);
    }

    private boolean checkServiceInDatabase(long serviceId) {
        String Query = "Select * from " + Constants.databaseServiciosTableName + " where " + Constants.databaseServicioId + " = " + serviceId;
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
        cv.put(Constants.databaseFecha, servicio.getFecha());
        cv.put(Constants.databaseEmpleadoId, servicio.getEmpleadoId());
        cv.put(Constants.databaseServicios, TextUtils.join(",", servicio.getServicios()));
        cv.put(Constants.databaseObservacion, servicio.getObservaciones());
        cv.put(Constants.databasePrecio, servicio.getPrecio());
        cv.put(Constants.databaseComercioId, servicio.getComercioId());
        cv.put(Constants.databaseIsEfectivo, servicio.isEfectivo() ? 1 : 0);

        return  cv;
    }

    private ServiceModel parseCursorToServiceModel(Cursor cursor) {
        ServiceModel service = new ServiceModel();
        service.setClientId(cursor.getLong(cursor.getColumnIndex(Constants.databaseClientId)));
        service.setServiceId(cursor.getLong(cursor.getColumnIndex(Constants.databaseServicioId)));
        service.setFecha(cursor.getLong(cursor.getColumnIndex(Constants.databaseFecha)));
        service.setEmpleadoId(cursor.getLong(cursor.getColumnIndex(Constants.databaseEmpleadoId)));
        String text = cursor.getString(cursor.getColumnIndex(Constants.databaseServicios));
        service.setServicios(getServiciosFromString(text));
        service.setObservaciones(cursor.getString(cursor.getColumnIndex(Constants.databaseObservacion)));
        service.setPrecio(cursor.getDouble(cursor.getColumnIndex(Constants.databasePrecio)));
        service.setComercioId(cursor.getLong(cursor.getColumnIndex(Constants.databaseComercioId)));
        service.setEfectivo(cursor.getInt(cursor.getColumnIndex(Constants.databaseIsEfectivo)) == 1);

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

    public void cleanDatabase() {
        writableDatabase.delete(Constants.databaseServiciosTableName, null, null);
    }
}

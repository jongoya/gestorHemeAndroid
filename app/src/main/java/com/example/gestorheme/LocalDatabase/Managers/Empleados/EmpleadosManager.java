package com.example.gestorheme.LocalDatabase.Managers.Empleados;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;

import java.util.ArrayList;

public class EmpleadosManager {
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;

    public EmpleadosManager(SQLiteDatabase writableDatabase, SQLiteDatabase readableDatabase) {
        this.writableDatabase = writableDatabase;
        this.readableDatabase = readableDatabase;
    }

    public void addEmpleadoToDatabase(EmpleadoModel empleado) {
        if (!checkEmpleadoInDatabase(empleado.getEmpleadoId())) {
            ContentValues cv = fillEmpleadoDataToDatabaseObject(empleado);
            writableDatabase.insert(Constants.databaseEmpleadosTableName, null, cv);
        }
    }

    public EmpleadoModel getEmpleadoForEmpleadoId(long empleadoId) {
        String Query = "Select * from " + Constants.databaseEmpleadosTableName + " where " + Constants.databaseEmpleadoId + " = " + String.valueOf(empleadoId);
        Cursor cursor = readableDatabase.rawQuery(Query, null);
        if (cursor.moveToNext()) {
            return parseCursorToEmpleadoModel(cursor);
        }

        return null;
    }

    public ArrayList<EmpleadoModel> getEmpleadosFromDatabase() {
        ArrayList<EmpleadoModel> clientes = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("select * from " + Constants.databaseEmpleadosTableName, null);
        while (cursor.moveToNext()) {
            clientes.add(parseCursorToEmpleadoModel(cursor));
        }

        cursor.close();
        return clientes;
    }

    public void updateEmpleadoInDatabase(EmpleadoModel empleado) {
        ContentValues cv = fillEmpleadoDataToDatabaseObject(empleado);
        writableDatabase.update(Constants.databaseEmpleadosTableName, cv, Constants.databaseEmpleadoId + "=" + String.valueOf(empleado.getEmpleadoId()), null);
    }

    public void deleteEmpleadoFromDatabase(long empleadoId) {
        writableDatabase.delete(Constants.databaseEmpleadosTableName, Constants.databaseEmpleadoId + " = " + empleadoId, null);
    }

    private boolean checkEmpleadoInDatabase(long empleadoId) {
        String Query = "Select * from " + Constants.databaseEmpleadosTableName + " where " + Constants.databaseEmpleadoId + " = " + String.valueOf(empleadoId);
        Cursor cursor = readableDatabase.rawQuery(Query, null);
        if (cursor.getCount() == 0){
            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }

    private ContentValues fillEmpleadoDataToDatabaseObject(EmpleadoModel empleado) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.databaseNombre, empleado.getNombre());
        cv.put(Constants.databaseApellidos, empleado.getApellidos());
        cv.put(Constants.databaseFecha, empleado.getFecha());
        cv.put(Constants.databaseTelefono, empleado.getTelefono());
        cv.put(Constants.databaseEmail, empleado.getEmail());
        cv.put(Constants.databaseEmpleadoId, empleado.getEmpleadoId());
        cv.put(Constants.databaseRedColorValue, empleado.getRedColorValue());
        cv.put(Constants.databaseBlueColorValue, empleado.getBlueColorValue());
        cv.put(Constants.databaseGreenColorValue, empleado.getGreenColorValue());
        cv.put(Constants.databaseIsEmpleadoJefe, empleado.isEmpleadoJefe() ? 1 : 0);
        cv.put(Constants.databaseComercioId, empleado.getComercioId());

        return  cv;
    }

    private EmpleadoModel parseCursorToEmpleadoModel(Cursor cursor) {
        EmpleadoModel empleado = new EmpleadoModel();
        empleado.setEmpleadoId(cursor.getLong(cursor.getColumnIndex(Constants.databaseEmpleadoId)));
        empleado.setNombre(cursor.getString(cursor.getColumnIndex(Constants.databaseNombre)));
        empleado.setApellidos(cursor.getString(cursor.getColumnIndex(Constants.databaseApellidos)));
        empleado.setFecha(cursor.getLong(cursor.getColumnIndex(Constants.databaseFecha)));
        empleado.setTelefono(cursor.getString(cursor.getColumnIndex(Constants.databaseTelefono)));
        empleado.setEmail(cursor.getString(cursor.getColumnIndex(Constants.databaseEmail)));
        empleado.setRedColorValue(cursor.getFloat(cursor.getColumnIndex(Constants.databaseRedColorValue)));
        empleado.setGreenColorValue(cursor.getFloat(cursor.getColumnIndex(Constants.databaseGreenColorValue)));
        empleado.setBlueColorValue(cursor.getFloat(cursor.getColumnIndex(Constants.databaseBlueColorValue)));
        empleado.setEmpleadoJefe(getBooleanFromInt(cursor.getInt(cursor.getColumnIndex(Constants.databaseIsEmpleadoJefe))));
        empleado.setComercioId(cursor.getLong(cursor.getColumnIndex(Constants.databaseComercioId)));

        return empleado;
    }

    private boolean getBooleanFromInt(int valor) {
        return valor == 1 ? true : false;
    }

    public void cleanDatabase() {
        writableDatabase.delete(Constants.databaseEmpleadosTableName, null, null);
    }
}

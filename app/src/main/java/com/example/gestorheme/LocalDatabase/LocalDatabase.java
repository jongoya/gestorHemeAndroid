package com.example.gestorheme.LocalDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDatabase extends SQLiteOpenHelper {
    private static final String CLIENTS_TABLE_CREATE = "CREATE TABLE Clientes(clientId INTEGER, nombre TEXT, apellidos TEXT, " +
            "fecha INTEGER, telefono TEXT, email TEXT, direccion TEXT, cadenciaVisita TEXT, observaciones TEXT, notificacionPersonalizada INTEGER, " +
            "imagen TEXT)";
    private static final String SERVICES_TABLE_CREATE = "CREATE TABLE Servicios(clientId INTEGER, servicioId INTEGER, nombre TEXT, " +
            "apellidos TEXT, fecha INTEGER, profesional INTEGER, servicios BLOB, observaciones TEXT, precio REAL)";
    private static final String NOTIFICATIONS_TABLE_CREATE = "CREATE TABLE Notificaciones(clientId INTEGER, notificationId INTEGER, " +
            "descripcion TEXT, fecha INTEGER, leido INTEGER, type TEXT)";
    private static final String EMPLEADOS_TABLE_CREATE = "CREATE TABLE Empleados(empleadoId INTEGER, nombre TEXT, apellidos TEXT, " +
            "fecha INTEGER, telefono TEXT, email TEXT, redColorValue REAL, greenColorValue REAL, blueColorValue REAL)";
    private static final String TIPOSERVICIOS_TABLE_CREATE = "CREATE TABLE TipoServicios(servicioId INTEGER, nombre TEXT)";
    private static final String CIERRECAJA_TABLE_CREATE = "CREATE TABLE CierreCaja(cajaId INTEGER, fecha INTEGER, numeroServicios INTEGER, " +
            "totalCaja REAL, totalProductos REAL, efectivo REAL, tarjeta REAL)";
    private static final String DB_NAME = "Heme";
    private static final int DB_VERSION = 1;

    public LocalDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CLIENTS_TABLE_CREATE);
        sqLiteDatabase.execSQL(SERVICES_TABLE_CREATE);
        sqLiteDatabase.execSQL(NOTIFICATIONS_TABLE_CREATE);
        sqLiteDatabase.execSQL(EMPLEADOS_TABLE_CREATE);
        sqLiteDatabase.execSQL(TIPOSERVICIOS_TABLE_CREATE);
        sqLiteDatabase.execSQL(CIERRECAJA_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

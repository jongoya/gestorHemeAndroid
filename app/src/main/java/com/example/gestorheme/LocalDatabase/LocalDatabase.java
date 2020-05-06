package com.example.gestorheme.LocalDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gestorheme.Common.Constants;

public class LocalDatabase extends SQLiteOpenHelper {
    private static final String CLIENTS_TABLE_CREATE = "CREATE TABLE " + Constants.databaseClientesTableName + "(" + Constants.databaseClientId + " INTEGER, " + Constants.databaseNombre + " TEXT, " +
            Constants.databaseApellidos + " TEXT, " + Constants.databaseFecha + " INTEGER, " + Constants.databaseTelefono + " TEXT, " +
            Constants.databaseEmail + " TEXT, " + Constants.databaseDireccion + " TEXT, " + Constants.databaseCadenciaVisita + " TEXT, " + Constants.databaseObservaciones + " TEXT," +
            Constants.databaseNotificacionPersonalizada + " INTEGER, " + Constants.databaseImagen + " TEXT)";
    private static final String SERVICES_TABLE_CREATE = "CREATE TABLE " + Constants.databaseServiciosTableName + "(" + Constants.databaseClientId + " INTEGER, " + Constants.databaseServicioId + " INTEGER, " + Constants.databaseNombre + " TEXT, " +
            Constants.databaseApellidos + " TEXT, " + Constants.databaseFecha + " INTEGER, " + Constants.databaseProfesional + " INTEGER, " + Constants.databaseServicios + " TEXT, " + Constants.databaseObservaciones + " TEXT, " + Constants.databasePrecio + " REAL)";
    private static final String NOTIFICATIONS_TABLE_CREATE = "CREATE TABLE " + Constants.databaseNotificacionesTableName + "(" + Constants.databaseClientId + " INTEGER, " + Constants.databaseNotificationId + " INTEGER, " +
            Constants.databaseDescripcion + " TEXT, " + Constants.databaseFecha + " INTEGER, " + Constants.databaseLeido + " INTEGER, " + Constants.databaseType + " TEXT)";
    private static final String EMPLEADOS_TABLE_CREATE = "CREATE TABLE " + Constants.databaseEmpleadosTableName + "(" + Constants.databaseEmpleadoId + " INTEGER, " + Constants.databaseNombre + " TEXT, " + Constants.databaseApellidos + " TEXT, " +
            Constants.databaseFecha + " INTEGER, " + Constants.databaseTelefono + " TEXT, " + Constants.databaseEmail + " TEXT, " + Constants.databaseRedColorValue + " REAL, " + Constants.databaseGreenColorValue + " REAL, " + Constants.databaseBlueColorValue + " REAL)";
    private static final String TIPOSERVICIOS_TABLE_CREATE = "CREATE TABLE " + Constants.databaseTipoServiciosTableName + "(" + Constants.databaseServicioId + " INTEGER, " + Constants.databaseNombre + " TEXT)";
    private static final String CIERRECAJA_TABLE_CREATE = "CREATE TABLE " + Constants.databaseCierreCajaTableName + "(" + Constants.databaseCajaId + " INTEGER, " + Constants.databaseFecha + " INTEGER, " + Constants.databaseNumeroServicios + " INTEGER, " +
            Constants.databaseTotalCaja + " REAL, " + Constants.databaseTotalProductos + " REAL, " + Constants.databaseEfectivo + " REAL, " + Constants.databaseTarjeta + " REAL)";
    private static final String DB_NAME = Constants.databaseName;
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

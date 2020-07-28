package com.example.gestorheme.LocalDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gestorheme.Common.Constants;

public class LocalDatabase extends SQLiteOpenHelper {
    private static final String CLIENTS_TABLE_CREATE = "CREATE TABLE " + Constants.databaseClientesTableName + "(" + Constants.databaseClientId + " INTEGER, " + Constants.databaseNombre + " TEXT, " +
            Constants.databaseApellidos + " TEXT, " + Constants.databaseFecha + " INTEGER, " + Constants.databaseTelefono + " TEXT, " +
            Constants.databaseEmail + " TEXT, " + Constants.databaseDireccion + " TEXT, " + Constants.databaseCadenciaVisita + " TEXT, " + Constants.databaseObservaciones + " TEXT," + Constants.databaseImagen + " TEXT," + Constants.databaseComercioId + " TEXT," + Constants.databaseFechaNotificacionPersonalizada + " TEXT)";
    private static final String SERVICES_TABLE_CREATE = "CREATE TABLE " + Constants.databaseServiciosTableName + "(" + Constants.databaseClientId + " INTEGER, " + Constants.databaseServicioId + " INTEGER, " + Constants.databaseFecha +
            " INTEGER, " + Constants.databaseEmpleadoId + " INTEGER, " + Constants.databaseServicios + " TEXT, " + Constants.databaseObservacion + " TEXT, " + Constants.databasePrecio + " REAL," + Constants.databaseComercioId + " TEXT, " + Constants.databaseIsEfectivo + " INTEGER)";
    private static final String NOTIFICATIONS_TABLE_CREATE = "CREATE TABLE " + Constants.databaseNotificacionesTableName + "(" + Constants.databaseClientId + " INTEGER, " + Constants.databaseNotificationId + " INTEGER, " +
            Constants.databaseDescripcion + " TEXT, " + Constants.databaseFecha + " INTEGER, " + Constants.databaseLeido + " INTEGER, " + Constants.databaseType + " TEXT," + Constants.databaseComercioId + " INTEGER)";
    private static final String EMPLEADOS_TABLE_CREATE = "CREATE TABLE " + Constants.databaseEmpleadosTableName + "(" + Constants.databaseEmpleadoId + " INTEGER, " + Constants.databaseNombre + " TEXT, " + Constants.databaseApellidos + " TEXT, " +
            Constants.databaseFecha + " INTEGER, " + Constants.databaseTelefono + " TEXT, " + Constants.databaseEmail + " TEXT, " + Constants.databaseRedColorValue + " REAL, " + Constants.databaseGreenColorValue + " REAL, " + Constants.databaseBlueColorValue + " REAL," + Constants.databaseComercioId + " TEXT," + Constants.databaseIsEmpleadoJefe + " INTEGER)";
    private static final String TIPOSERVICIOS_TABLE_CREATE = "CREATE TABLE " + Constants.databaseTipoServiciosTableName + "(" + Constants.databaseServicioId + " INTEGER, " + Constants.databaseNombre + " TEXT," + Constants.databaseComercioId + " TEXT)";
    private static final String CIERRECAJA_TABLE_CREATE = "CREATE TABLE " + Constants.databaseCierreCajaTableName + "(" + Constants.databaseCajaId + " INTEGER, " + Constants.databaseFecha + " INTEGER, " + Constants.databaseNumeroServicios + " INTEGER, " +
            Constants.databaseTotalCaja + " REAL, " + Constants.databaseTotalProductos + " REAL, " + Constants.databaseEfectivo + " REAL, " + Constants.databaseTarjeta + " REAL," + Constants.databaseComercioId + " TEXT)";
    private static final String ESTILOAPP_TABLE_CREATE = "CREATE TABLE " + Constants.databaseEstiloAppTableName + "(" + Constants.databaseEstiloId + " INTEGER, " + Constants.databasePrimaryTextColor + " TEXT, " +
            Constants.databaseSecondaryTextColor + " TEXT, " + Constants.databasePrimaryColor + " TEXT, " + Constants.databaseSecondaryColor + " TEXT, " +
            Constants.databaseBackgroundColor + " TEXT, " + Constants.databaseNavigationColor + " TEXT, " + Constants.databaseAppSmallIcon + " TEXT, " + Constants.databaseAppName + " TEXT," + Constants.databaseComercioId + " TEXT)";
    private static final String PRODUCTOS_TABLE_CREATE = "CREATE TABLE " + Constants.databaseProductoTableName + "(" + Constants.databaseProductoId + " INTEGER, " + Constants.databaseNombre + " TEXT, " +
            Constants.databaseCodigoBarras + " TEXT, " + Constants.databaseImagen + " TEXT, " + Constants.databaseNumProductos + " INTEGER, " +
            Constants.databaseComercioId + " INTEGER, " + Constants.databasePrecio + " REAL)";
    private static final String CESTA_TABLE_CREATE = "CREATE TABLE " + Constants.databaseCestaTableName + "(" + Constants.databaseCestaId + " INTEGER, " + Constants.databaseClientId + " INTEGER, " +
            Constants.databaseFecha + " INTEGER, " + Constants.databaseIsEfectivo + " INTEGER, " + Constants.databaseComercioId + " INTEGER)";
    private static final String VENTA_TABLE_CREATE = "CREATE TABLE " + Constants.databaseVentaTableName + "(" + Constants.databaseCestaId + " INTEGER, " + Constants.databaseVentaId + " INTEGER, " +
            Constants.databaseProductoId + " INTEGER, " + Constants.databaseCantidad + " INTEGER, " + Constants.databaseComercioId + " INTEGER)";

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
        sqLiteDatabase.execSQL(ESTILOAPP_TABLE_CREATE);
        sqLiteDatabase.execSQL(PRODUCTOS_TABLE_CREATE);
        sqLiteDatabase.execSQL(CESTA_TABLE_CREATE);
        sqLiteDatabase.execSQL(VENTA_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

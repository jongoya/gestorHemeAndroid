package com.example.gestorheme.Common;

import com.example.gestorheme.Activities.Main.MainActivity;
import com.example.gestorheme.ApiServices.RetrofitClientInstance;
import com.example.gestorheme.ApiServices.WebServices;
import com.example.gestorheme.LocalDatabase.DatabaseManager;

public class Constants {
    public static String databaseName = "Heme";

    public static String databaseClientesTableName = "Clientes";
    public static String databaseServiciosTableName = "Servicios";
    public static String databaseNotificacionesTableName = "Notificaciones";
    public static String databaseEmpleadosTableName = "Empleados";
    public static String databaseTipoServiciosTableName = "TipoServicios";
    public static String databaseCierreCajaTableName = "CierreCaja";
    public static String databaseEstiloAppTableName = "EstiloApp";
    public static String databaseProductoTableName = "Producto";
    public static String databaseCestaTableName = "Cesta";
    public static String databaseVentaTableName = "Venta";
    public static String databaseProductoId = "productoId";
    public static String databaseCodigoBarras = "codigoBarras";
    public static String databaseNumProductos = "numProductos";
    public static String databaseCestaId = "cestaId";
    public static String databaseVentaId = "ventaId";
    public static String databaseCantidad = "cantidad";
    public static String databaseClientId = "clientId";
    public static String databaseNombre = "nombre";
    public static String databaseApellidos = "apellidos";
    public static String databaseFecha = "fecha";
    public static String databaseTelefono = "telefono";
    public static String databaseEmail = "email";
    public static String databaseDireccion = "direccion";
    public static String databaseCadenciaVisita = "cadenciaVisita";
    public static String databaseObservaciones = "observaciones";
    public static String databaseObservacion = "observacion";
    public static String databaseImagen = "imagen";
    public static String databaseServicioId = "servicioId";
    public static String databaseServicios = "servicios";
    public static String databasePrecio = "precio";
    public static String databaseNotificationId = "notificationId";
    public static String databaseDescripcion = "descripcion";
    public static String databaseLeido = "leido";
    public static String databaseType = "type";
    public static String databaseEmpleadoId = "empleadoId";
    public static String databaseRedColorValue = "redColorValue";
    public static String databaseGreenColorValue = "greenColorValue";
    public static String databaseBlueColorValue = "blueColorValue";
    public static String databaseIsEmpleadoJefe = "is_empleado_jefe";
    public static String databaseCajaId = "cajaId";
    public static String databaseNumeroServicios = "numeroServicios";
    public static String databaseTotalCaja = "totalCaja";
    public static String databaseTotalProductos = "totalProductos";
    public static String databaseEfectivo = "efectivo";
    public static String databaseTarjeta = "tarjeta";
    public static String databaseComercioId = "comercioId";
    public static String databaseFechaNotificacionPersonalizada = "fechaNotificacionPersonalizada";
    public static String databaseEstiloId = "estiloId";
    public static String databasePrimaryTextColor = "primaryTextColor";
    public static String databaseSecondaryTextColor = "secondaryTextColor";
    public static String databasePrimaryColor = "primaryColor";
    public static String databaseSecondaryColor = "secondaryColor";
    public static String databaseBackgroundColor = "backgroundColor";
    public static String databaseNavigationColor = "navigationColor";
    public static String databaseAppSmallIcon = "appSmallIcon";
    public static String databaseAppName = "appName";
    public static String databaseIsEfectivo = "isEfectivo";

    public static final String unaSemana = "cada semana";
    public static final String dosSemanas = "cada 2 semanas";
    public static final String tresSemanas = "cada 3 semanas";
    public static final String unMes = "cada mes";
    public static final String unMesUnaSemana = "cada mes y 1 semana";
    public static final String unMesDosSemanas = "cada mes y 2 semanas";
    public static final String unMesTresSemanas = "cada mes y 3 semanas";
    public static final String dosMeses = "cada 2 meses";
    public static final String dosMesesYUnaSemana = "cada 2 meses y 1 semana";
    public static final String dosMesesYDosSemanas = "cada 2 meses y 2 semanas";
    public static final String dosMesesYTresSemanas = "cada 2 meses y 3 semanas";
    public static final String tresMeses = "cada 3 meses";
    public static final String masDeTresMeses = "mas de 3 meses";

    public static DatabaseManager databaseManager;

    public static final String notificationCumpleañosType = "cumpleaños";
    public static final String notificationCadenciaType = "cadencia";
    public static final String notificationcajaType = "cajaCierre";
    public static final String notificationpersonalizadaType = "personalizada";

    public static final String notificationCellRowType = "row";
    public static final String notificationCellHeaderType = "header";

    public static WebServices webServices;

    //Shared preferences Constants

    public static final String preferencesPasswordKey = "password";
    public static final String preferencesTokenKey = "token";
    public static final String preferencesComercioIdKey = "comercioId";
    public static final String preferencesFondoLoginKey = "fondoLogin";
    public static final String preferencesPrimaryColorKey = "primaryColor";
    public static final String preferencesPrimaryTextColorKey = "primaryTextColor";
    public static final String preferencesSecondaryTextColorKey = "secondaryTextColor";
    public static final String preferencesIconoAppKey = "iconoApp";
    public static final String preferencesNombreAppKey = "nombreApp";
    public static final int logoutResponseValue = 420;


    public static MainActivity mainActivityReference;
}

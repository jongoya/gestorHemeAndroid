package com.example.gestorheme.LocalDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.gestorheme.LocalDatabase.Managers.Cesta.CestaManager;
import com.example.gestorheme.LocalDatabase.Managers.CierreCaja.CierreCajaManager;
import com.example.gestorheme.LocalDatabase.Managers.Clients.ClientsManager;
import com.example.gestorheme.LocalDatabase.Managers.Empleados.EmpleadosManager;
import com.example.gestorheme.LocalDatabase.Managers.EstiloApp.EstiloAppManager;
import com.example.gestorheme.LocalDatabase.Managers.Notifications.NotificationsManager;
import com.example.gestorheme.LocalDatabase.Managers.Producto.ProductoManager;
import com.example.gestorheme.LocalDatabase.Managers.Services.ServicesManager;
import com.example.gestorheme.LocalDatabase.Managers.TipoServicio.TipoServicioManager;
import com.example.gestorheme.LocalDatabase.Managers.Venta.VentaManager;

public class DatabaseManager {
    public ClientsManager clientsManager;
    public ServicesManager servicesManager;
    public TipoServicioManager tipoServiciosManager;
    public EmpleadosManager empleadosManager;
    public CierreCajaManager cierreCajaManager;
    public NotificationsManager notificationsManager;
    public EstiloAppManager estiloAppManager;
    public ProductoManager productoManager;
    public CestaManager cestaManager;
    public VentaManager ventaManager;

    public DatabaseManager(Context context) {
        LocalDatabase localDatabase = new LocalDatabase(context);
        SQLiteDatabase writableDatabase = localDatabase.getWritableDatabase();
        SQLiteDatabase readableDatabase = localDatabase.getReadableDatabase();
        clientsManager = new ClientsManager(writableDatabase, readableDatabase);
        servicesManager = new ServicesManager(writableDatabase, readableDatabase);
        tipoServiciosManager = new TipoServicioManager(writableDatabase, readableDatabase);
        empleadosManager = new EmpleadosManager(writableDatabase, readableDatabase);
        cierreCajaManager = new CierreCajaManager(writableDatabase, readableDatabase);
        notificationsManager = new NotificationsManager(writableDatabase, readableDatabase);
        estiloAppManager = new EstiloAppManager(writableDatabase, readableDatabase);
        productoManager = new ProductoManager(writableDatabase, readableDatabase);
        cestaManager = new CestaManager(writableDatabase, readableDatabase);
        ventaManager = new VentaManager(writableDatabase, readableDatabase);
    }

    public void deleteAllRecordsFromDatabase() {
        clientsManager.cleanDatabase();
        servicesManager.cleanDatabase();
        tipoServiciosManager.cleanDatabase();
        empleadosManager.cleanDatabase();
        cierreCajaManager.cleanDatabase();
        notificationsManager.cleanDatabase();
        estiloAppManager.cleanDatabase();
        productoManager.cleanDatabase();
        cestaManager.cleanDatabase();
        ventaManager.cleanDatabase();
    }
}

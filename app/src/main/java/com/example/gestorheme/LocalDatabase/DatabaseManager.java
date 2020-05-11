package com.example.gestorheme.LocalDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.gestorheme.LocalDatabase.Managers.Clients.ClientsManager;
import com.example.gestorheme.LocalDatabase.Managers.Empleados.EmpleadosManager;
import com.example.gestorheme.LocalDatabase.Managers.Services.ServicesManager;
import com.example.gestorheme.LocalDatabase.Managers.TipoServicio.TipoServicioManager;

public class DatabaseManager {
    public ClientsManager clientsManager;
    public ServicesManager servicesManager;
    public TipoServicioManager tipoServiciosManager;
    public EmpleadosManager empleadosManager;

    public DatabaseManager(Context context) {
        LocalDatabase localDatabase = new LocalDatabase(context);
        SQLiteDatabase writableDatabase = localDatabase.getWritableDatabase();
        SQLiteDatabase readableDatabase = localDatabase.getReadableDatabase();
        clientsManager = new ClientsManager(writableDatabase, readableDatabase);
        servicesManager = new ServicesManager(writableDatabase, readableDatabase);
        tipoServiciosManager = new TipoServicioManager(writableDatabase, readableDatabase);
        empleadosManager = new EmpleadosManager(writableDatabase, readableDatabase);
    }
}

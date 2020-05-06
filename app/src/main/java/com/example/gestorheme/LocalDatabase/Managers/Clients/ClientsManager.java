package com.example.gestorheme.LocalDatabase.Managers.Clients;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Client.ClientModel;

import java.util.ArrayList;

public class ClientsManager {
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;

    public ClientsManager(SQLiteDatabase writableDatabase, SQLiteDatabase readableDatabase) {
        this.writableDatabase = writableDatabase;
        this.readableDatabase = readableDatabase;
    }

    public ArrayList<ClientModel> getClientsFromDatabase() {
        ArrayList<ClientModel> clientes = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("select * from " + Constants.databaseClientesTableName, null);
        while (cursor.moveToNext()) {
            clientes.add(parseCursorToClientModel(cursor));
        }

        return clientes;
    }

    public void addClientToDatabase(ClientModel client) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.databaseClientId, client.getClientId());
        cv.put(Constants.databaseNombre, client.getNombre());
        cv.put(Constants.databaseApellidos, client.getApellidos());
        cv.put(Constants.databaseFecha, client.getFecha());
        cv.put(Constants.databaseTelefono, client.getTelefono());
        cv.put(Constants.databaseEmail, client.getEmail());
        cv.put(Constants.databaseDireccion, client.getDireccion());
        cv.put(Constants.databaseCadenciaVisita, client.getCadenciaVisita());
        cv.put(Constants.databaseObservaciones, client.getObservaciones());
        cv.put(Constants.databaseNotificacionPersonalizada, client.getNotificacionPersonalizada());
        cv.put(Constants.databaseImagen, client.getImagen());
        writableDatabase.insert(Constants.databaseClientesTableName, null, cv);
    }

    public ClientModel parseCursorToClientModel(Cursor cursor) {
        ClientModel cliente = new ClientModel();
        cliente.setClientId(cursor.getLong(cursor.getColumnIndex(Constants.databaseClientId)));
        cliente.setNombre(cursor.getString(cursor.getColumnIndex(Constants.databaseNombre)));
        cliente.setApellidos(cursor.getString(cursor.getColumnIndex(Constants.databaseApellidos)));
        cliente.setFecha(cursor.getLong(cursor.getColumnIndex(Constants.databaseFecha)));
        cliente.setTelefono(cursor.getString(cursor.getColumnIndex(Constants.databaseTelefono)));
        cliente.setEmail(cursor.getString(cursor.getColumnIndex(Constants.databaseEmail)));
        cliente.setDireccion(cursor.getString(cursor.getColumnIndex(Constants.databaseDireccion)));
        cliente.setCadenciaVisita(cursor.getString(cursor.getColumnIndex(Constants.databaseCadenciaVisita)));
        cliente.setObservaciones(cursor.getString(cursor.getColumnIndex(Constants.databaseObservaciones)));
        cliente.setNotificacionPersonalizada(cursor.getLong(cursor.getColumnIndex(Constants.databaseNotificacionPersonalizada)));
        cliente.setImagen(cursor.getString(cursor.getColumnIndex(Constants.databaseImagen)));

        return cliente;
    }
}

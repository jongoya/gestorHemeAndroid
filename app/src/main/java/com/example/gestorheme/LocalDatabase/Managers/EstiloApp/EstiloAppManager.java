package com.example.gestorheme.LocalDatabase.Managers.EstiloApp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.EstiloApp.EstiloAppModel;

public class EstiloAppManager {

    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;

    public EstiloAppManager(SQLiteDatabase writableDatabase, SQLiteDatabase readableDatabase) {
        this.writableDatabase = writableDatabase;
        this.readableDatabase = readableDatabase;
    }

    public void addEstiloAppToDatabase(EstiloAppModel estiloApp) {
        ContentValues cv = fillEstiloAppDataToDatabaseObject(estiloApp);
        writableDatabase.insert(Constants.databaseEstiloAppTableName, null, cv);
    }

    public EstiloAppModel getEstiloAppFromDatabase() {
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM " + Constants.databaseEstiloAppTableName, null);
        if (cursor.moveToNext()) {
            EstiloAppModel model = parseCursorToEstiloAppModel(cursor);
            cursor.close();
            return model;
        }

        cursor.close();

        return null;
    }

    public void updateEstiloAppInDatabase(EstiloAppModel estiloApp) {
        ContentValues cv = fillEstiloAppDataToDatabaseObject(estiloApp);
        writableDatabase.update(Constants.databaseEstiloAppTableName, cv, Constants.databaseEstiloId + "=" + String.valueOf(estiloApp.getEstiloId()), null);
    }


    private ContentValues fillEstiloAppDataToDatabaseObject(EstiloAppModel estiloApp) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.databaseEstiloId, estiloApp.getEstiloId());
        cv.put(Constants.databaseComercioId, estiloApp.getComercioId());
        cv.put(Constants.databasePrimaryTextColor, estiloApp.getPrimaryTextColor());
        cv.put(Constants.databaseSecondaryTextColor, estiloApp.getSecondaryTextColor());
        cv.put(Constants.databasePrimaryColor, estiloApp.getPrimaryColor());
        cv.put(Constants.databaseSecondaryColor, estiloApp.getSecondaryColor());
        cv.put(Constants.databaseBackgroundColor, estiloApp.getBackgroundColor());
        cv.put(Constants.databaseNavigationColor, estiloApp.getNavigationColor());
        cv.put(Constants.databaseAppSmallIcon, estiloApp.getAppSmallIcon());
        cv.put(Constants.databaseAppName, estiloApp.getAppName());

        return  cv;
    }

    private EstiloAppModel parseCursorToEstiloAppModel(Cursor cursor) {
        EstiloAppModel estiloApp = new EstiloAppModel();
        estiloApp.setEstiloId(cursor.getLong(cursor.getColumnIndex(Constants.databaseEstiloId)));
        estiloApp.setComercioId(cursor.getLong(cursor.getColumnIndex(Constants.databaseComercioId)));
        estiloApp.setPrimaryTextColor(cursor.getString(cursor.getColumnIndex(Constants.databasePrimaryTextColor)));
        estiloApp.setSecondaryTextColor(cursor.getString(cursor.getColumnIndex(Constants.databaseSecondaryTextColor)));
        estiloApp.setPrimaryColor(cursor.getString(cursor.getColumnIndex(Constants.databasePrimaryColor)));
        estiloApp.setSecondaryColor(cursor.getString(cursor.getColumnIndex(Constants.databaseSecondaryColor)));
        estiloApp.setBackgroundColor(cursor.getString(cursor.getColumnIndex(Constants.databaseBackgroundColor)));
        estiloApp.setNavigationColor(cursor.getString(cursor.getColumnIndex(Constants.databaseNavigationColor)));
        estiloApp.setAppSmallIcon(cursor.getString(cursor.getColumnIndex(Constants.databaseAppSmallIcon)));
        estiloApp.setAppName(cursor.getString(cursor.getColumnIndex(Constants.databaseAppName)));

        return estiloApp;
    }

    public void cleanDatabase() {
        writableDatabase.delete(Constants.databaseEstiloAppTableName, null, null);
    }
}

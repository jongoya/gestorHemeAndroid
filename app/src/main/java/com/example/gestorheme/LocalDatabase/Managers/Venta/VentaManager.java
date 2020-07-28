package com.example.gestorheme.LocalDatabase.Managers.Venta;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Venta.VentaModel;

import java.util.ArrayList;

public class VentaManager {
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;

    public VentaManager(SQLiteDatabase writableDatabase, SQLiteDatabase readableDatabase) {
        this.writableDatabase = writableDatabase;
        this.readableDatabase = readableDatabase;
    }

    public ArrayList<VentaModel> getAllVentas() {
        ArrayList<VentaModel> ventas = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("select * from " + Constants.databaseVentaTableName, null);
        while (cursor.moveToNext()) {
            ventas.add(parseCursorToVentaModel(cursor));
        }

        cursor.close();
        return ventas;
    }

    public void saveVenta(VentaModel venta) {
        if (!checkVenta(venta.getVentaId())) {
            ContentValues cv = fillVentaDataToDatabaseObject(venta);
            writableDatabase.insert(Constants.databaseVentaTableName, null, cv);
        }
    }

    public void updateVenta(VentaModel venta) {
        ContentValues cv = fillVentaDataToDatabaseObject(venta);
        writableDatabase.update(Constants.databaseVentaTableName, cv, Constants.databaseVentaId + "=" + String.valueOf(venta.getVentaId()), null);
    }

    public VentaModel getVenta(long ventaId) {
        String Query = "Select * from " + Constants.databaseVentaTableName + " where " + Constants.databaseVentaId + " = " + ventaId;
        Cursor cursor = readableDatabase.rawQuery(Query, null);
        if (cursor.moveToNext()) {
            return parseCursorToVentaModel(cursor);
        }

        return null;
    }

    public ArrayList<VentaModel> getVentas(long cestaId) {
        ArrayList<VentaModel> ventas = new ArrayList<>();
        String Query = "Select * from " + Constants.databaseVentaTableName + " where " + Constants.databaseCestaId + " = " + cestaId;
        Cursor cursor = readableDatabase.rawQuery(Query, null);
        while (cursor.moveToNext()) {
            ventas.add(parseCursorToVentaModel(cursor));
        }

        return ventas;
    }

    public void deleteVenta(VentaModel venta) {
        writableDatabase.delete(Constants.databaseVentaTableName, Constants.databaseVentaId + " = " + venta.getVentaId(), null);
    }

    public void cleanDatabase() {
        writableDatabase.delete(Constants.databaseVentaTableName, null, null);
    }

    private boolean checkVenta(long ventaId) {
        String Query = "Select * from " + Constants.databaseVentaTableName + " where " + Constants.databaseVentaId + " = " + String.valueOf(ventaId);
        Cursor cursor = readableDatabase.rawQuery(Query, null);
        if (cursor.getCount() == 0){
            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }

    private VentaModel parseCursorToVentaModel(Cursor cursor) {
        VentaModel venta = new VentaModel();
        venta.setCantidad(cursor.getInt(cursor.getColumnIndex(Constants.databaseCantidad)));
        venta.setCestaId(cursor.getLong(cursor.getColumnIndex(Constants.databaseCestaId)));
        venta.setComercioId(cursor.getLong(cursor.getColumnIndex(Constants.databaseComercioId)));
        venta.setProductoId(cursor.getLong(cursor.getColumnIndex(Constants.databaseProductoId)));
        venta.setVentaId(cursor.getLong(cursor.getColumnIndex(Constants.databaseVentaId)));

        return venta;
    }

    private ContentValues fillVentaDataToDatabaseObject(VentaModel venta) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.databaseCantidad, venta.getCantidad());
        cv.put(Constants.databaseCestaId, venta.getCestaId());
        cv.put(Constants.databaseComercioId, venta.getComercioId());
        cv.put(Constants.databaseProductoId, venta.getProductoId());
        cv.put(Constants.databaseVentaId, venta.getVentaId());

        return cv;
    }
}

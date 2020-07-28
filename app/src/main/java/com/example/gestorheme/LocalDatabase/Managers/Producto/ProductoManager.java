package com.example.gestorheme.LocalDatabase.Managers.Producto;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Producto.ProductoModel;

import java.util.ArrayList;

public class ProductoManager {
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;

    public ProductoManager(SQLiteDatabase writableDatabase, SQLiteDatabase readableDatabase) {
        this.writableDatabase = writableDatabase;
        this.readableDatabase = readableDatabase;
    }

    public ArrayList<ProductoModel> getAllProductos() {
        ArrayList<ProductoModel> productos = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("select * from " + Constants.databaseProductoTableName, null);
        while (cursor.moveToNext()) {
            productos.add(parseCursorToProductoModel(cursor));
        }

        cursor.close();
        return productos;
    }

    public void saveProducto(ProductoModel producto) {
        if (!checkProducto(producto.getProductoId())) {
            ContentValues cv = fillProductoDataToDatabaseObject(producto);
            writableDatabase.insert(Constants.databaseProductoTableName, null, cv);
        }
    }

    public void updateProducto(ProductoModel producto) {
        ContentValues cv = fillProductoDataToDatabaseObject(producto);
        writableDatabase.update(Constants.databaseProductoTableName, cv, Constants.databaseProductoId + "=" + String.valueOf(producto.getProductoId()), null);
    }

    public ProductoModel getProductoWithBarcode(String barcode) {
        String Query = "Select * from " + Constants.databaseProductoTableName + " where " + Constants.databaseCodigoBarras + " = " + barcode;
        Cursor cursor = readableDatabase.rawQuery(Query, null);
        if (cursor.moveToNext()) {
            return parseCursorToProductoModel(cursor);
        }

        return null;
    }

    public ProductoModel getProductoWithProductId(long productId) {
        String Query = "Select * from " + Constants.databaseProductoTableName + " where " + Constants.databaseProductoId + " = " + productId;
        Cursor cursor = readableDatabase.rawQuery(Query, null);
        if (cursor.moveToNext()) {
            return parseCursorToProductoModel(cursor);
        }

        return null;
    }

    public void deleteProducto(ProductoModel producto) {
        writableDatabase.delete(Constants.databaseProductoTableName, Constants.databaseProductoId + " = " + producto.getProductoId(), null);
    }

    public void cleanDatabase() {
        writableDatabase.delete(Constants.databaseProductoTableName, null, null);
    }

    private boolean checkProducto(long productoId) {
        String Query = "Select * from " + Constants.databaseProductoTableName + " where " + Constants.databaseProductoId + " = " + String.valueOf(productoId);
        Cursor cursor = readableDatabase.rawQuery(Query, null);
        if (cursor.getCount() == 0){
            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }

    private ProductoModel parseCursorToProductoModel(Cursor cursor) {
        ProductoModel producto = new ProductoModel();
        producto.setProductoId(cursor.getLong(cursor.getColumnIndex(Constants.databaseProductoId)));
        producto.setCodigoBarras(cursor.getString(cursor.getColumnIndex(Constants.databaseCodigoBarras)));
        producto.setComercioId(cursor.getLong(cursor.getColumnIndex(Constants.databaseComercioId)));
        producto.setImagen(cursor.getString(cursor.getColumnIndex(Constants.databaseImagen)));
        producto.setNombre(cursor.getString(cursor.getColumnIndex(Constants.databaseNombre)));
        producto.setNumProductos(cursor.getInt(cursor.getColumnIndex(Constants.databaseNumProductos)));
        producto.setPrecio(cursor.getDouble(cursor.getColumnIndex(Constants.databasePrecio)));

        return producto;
    }

    private ContentValues fillProductoDataToDatabaseObject(ProductoModel producto) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.databaseCodigoBarras, producto.getCodigoBarras());
        cv.put(Constants.databaseComercioId, producto.getComercioId());
        cv.put(Constants.databaseImagen, producto.getImagen());
        cv.put(Constants.databaseNombre, producto.getNombre());
        cv.put(Constants.databaseNumProductos, producto.getNumProductos());
        cv.put(Constants.databasePrecio, producto.getPrecio());
        cv.put(Constants.databaseProductoId, producto.getProductoId());

        return cv;
    }
}

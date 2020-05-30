package com.example.gestorheme.LocalDatabase.Managers.Notifications;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Notification.NotificationModel;
import java.util.ArrayList;
import java.util.Arrays;

public class NotificationsManager {

    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;

    public NotificationsManager(SQLiteDatabase writableDatabase, SQLiteDatabase readableDatabase) {
        this.writableDatabase = writableDatabase;
        this.readableDatabase = readableDatabase;
    }

    public void addNotificationToDatabase(NotificationModel notification) {
        ContentValues cv = fillNotificationDataToDatabaseObject(notification);
        writableDatabase.insert(Constants.databaseNotificacionesTableName, null, cv);
    }

    public ArrayList<NotificationModel> getNotificationsFromDatabase() {
        ArrayList<NotificationModel> arrayNotificaciones = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM " + Constants.databaseNotificacionesTableName, null);
        while (cursor.moveToNext()) {
            arrayNotificaciones.add(parseCursorToNotificationModel(cursor));
        }

        cursor.close();
        return arrayNotificaciones;
    }

    public ArrayList<NotificationModel> getNotificationsForType(String type) {
        ArrayList<NotificationModel> arrayNotificaciones = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM " + Constants.databaseNotificacionesTableName + " WHERE " + Constants.databaseType + " = ?" , new String[] {type});
        while (cursor.moveToNext()) {
            arrayNotificaciones.add(parseCursorToNotificationModel(cursor));
        }

        cursor.close();
        return arrayNotificaciones;
    }

    public NotificationModel getNotificationForId(long notificationId) {
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM " + Constants.databaseNotificacionesTableName + " WHERE " + Constants.databaseNotificationId + " = ?" , new String[] {String.valueOf(notificationId)});
        while (cursor.moveToNext()) {
            NotificationModel notification = parseCursorToNotificationModel(cursor);
            cursor.close();
            return  notification;
        }

        cursor.close();
        return null;
    }

    public void updateNotificationInDatabase(NotificationModel notification) {
        ContentValues cv = fillNotificationDataToDatabaseObject(notification);
        writableDatabase.update(Constants.databaseNotificacionesTableName, cv, Constants.databaseNotificationId + "=" + String.valueOf(notification.getNotificationId()), null);
    }

    public void deleteNotificationFromDatabase(long notificationId) {
        writableDatabase.delete(Constants.databaseNotificacionesTableName, Constants.databaseNotificationId + " = " + notificationId, null);
    }

    private ContentValues fillNotificationDataToDatabaseObject(NotificationModel notification) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.databaseClientId, notification.getClientId());
        cv.put(Constants.databaseNotificationId, notification.getNotificationId());
        cv.put(Constants.databaseDescripcion, notification.getDescripcion());
        cv.put(Constants.databaseFecha, notification.getFecha());
        cv.put(Constants.databaseLeido, notification.isLeido() ? 1 : 0);
        cv.put(Constants.databaseType, notification.getType());
        cv.put(Constants.databaseComercioId, notification.getComercioId());

        return  cv;
    }

    private NotificationModel parseCursorToNotificationModel(Cursor cursor) {
        NotificationModel service = new NotificationModel();
        service.setClientId(cursor.getLong(cursor.getColumnIndex(Constants.databaseClientId)));
        service.setNotificationId(cursor.getLong(cursor.getColumnIndex(Constants.databaseNotificationId)));
        service.setDescripcion(cursor.getString(cursor.getColumnIndex(Constants.databaseDescripcion)));
        service.setFecha(cursor.getLong(cursor.getColumnIndex(Constants.databaseFecha)));
        service.setLeido(getBooleanFromInt(cursor.getInt(cursor.getColumnIndex(Constants.databaseLeido))));
        service.setType(cursor.getString(cursor.getColumnIndex(Constants.databaseType)));
        service.setComercioId(cursor.getLong(cursor.getColumnIndex(Constants.databaseComercioId)));
        return service;
    }

    private boolean getBooleanFromInt(int valor) {
        return valor == 1 ? true : false;
    }

}

package com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Functions;
import android.content.Context;
import android.provider.CalendarContract;

import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Common.Preferencias;
import com.example.gestorheme.Models.Cadencia.CadenciaModel;
import com.example.gestorheme.Models.CierreCaja.CierreCajaModel;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Notification.NotificationModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFunctions {

    private static ArrayList<NotificationModel> checkBirthdays(Context context) {
        ArrayList<ClientModel> birthdayClients = getTodayBirthdayClients();
        ArrayList<NotificationModel> todayNotifications = getTodayNotifications();
        ArrayList<ClientModel> todayBirthdayClients = new ArrayList<>();
        ArrayList<NotificationModel> birthdayNotifications = new ArrayList<>();

        if (birthdayClients.size() == 0) {
            return new ArrayList<>();
        }

        for (int i = 0; i < birthdayClients.size(); i++) {
            ClientModel client = birthdayClients.get(i);
            boolean notificationExists = false;
            for (int j = 0; j < todayNotifications.size(); j++) {
                NotificationModel notification = todayNotifications.get(j);
                if (notification.getClientId() == client.getClientId()) {
                    notificationExists = true;
                }
            }

            if (!notificationExists) {
                todayBirthdayClients.add(client);
            }
        }

        if (todayBirthdayClients.size() > 0) {
            for (int i = 0; i < todayBirthdayClients.size(); i++) {
                birthdayNotifications.add(createBirthdayNotification(todayBirthdayClients.get(i), context));
            }
        }

        return birthdayNotifications;
    }



    private static ArrayList<ClientModel> getTodayBirthdayClients() {
        ArrayList<ClientModel> clients = Constants.databaseManager.clientsManager.getClientsFromDatabase();
        ArrayList<ClientModel> birthdayClients = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int todayDay = calendar.get(Calendar.DAY_OF_MONTH);
        int todayMonth = calendar.get(Calendar.MONTH);

        for (int i = 0; i < clients.size(); i++) {
            ClientModel cliente = clients.get(i);
            calendar.setTimeInMillis(cliente.getFecha() * 1000);
            int clientDay = calendar.get(Calendar.DAY_OF_MONTH);
            int clientMonth = calendar.get(Calendar.MONTH);
            if (clientDay == todayDay && clientMonth == todayMonth) {
                birthdayClients.add(cliente);
            }
        }

        return birthdayClients;
    }

    private static ArrayList<NotificationModel> getTodayNotifications() {
        ArrayList<NotificationModel> todayNotifications = new ArrayList<>();
        ArrayList<NotificationModel> allNotifications = Constants.databaseManager.notificationsManager.getNotificationsForType(Constants.notificationCumpleañosType);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateFunctions.getBeginingOfDayFromDate(new Date()));
        long begginingOfDay = calendar.getTimeInMillis() / 1000;
        calendar.setTime(DateFunctions.getEndOfDayFromDate(new Date()));
        long endOfDay = calendar.getTimeInMillis() / 1000;
        for (int i = 0; i < allNotifications.size(); i++) {
            NotificationModel notification = allNotifications.get(i);
            if (notification.getFecha() > begginingOfDay && notification.getFecha() < endOfDay) {
                todayNotifications.add(notification);
            }
        }

        return todayNotifications;
    }

    private static NotificationModel createBirthdayNotification(ClientModel cliente, Context context) {
        NotificationModel notification = new NotificationModel();
        notification.setComercioId(Preferencias.getComercioIdFromSharedPreferences(context));
        notification.setFecha(Calendar.getInstance().getTimeInMillis() / 1000);
        notification.setClientId(cliente.getClientId());
        notification.setLeido(false);
        notification.setType(Constants.notificationCumpleañosType);

        return notification;
    }

    private static ArrayList<NotificationModel> checkCadencias(Context contexto) {
        ArrayList<ClientModel> clientesConCadenciaSuperada = new ArrayList<>();
        ArrayList<NotificationModel> notificacionesCadencia = new ArrayList<>();
        ArrayList<ClientModel> clientes = Constants.databaseManager.clientsManager.getClientsFromDatabase();
        ArrayList<NotificationModel> notifications = Constants.databaseManager.notificationsManager.getNotificationsForType(Constants.notificationCadenciaType);

        for (int i = 0; i < clientes.size(); i++) {
            ClientModel cliente = clientes.get(i);
            CadenciaModel cadencia = new CadenciaModel(cliente.getCadenciaVisita());
            ArrayList<ServiceModel> servicios = Constants.databaseManager.servicesManager.getServicesForClient(cliente.getClientId());

            if (aSuperadoCadencia(servicios, cadencia, cliente.getFecha())) {
                if (!hasClientANotification(notifications, cliente.getClientId())) {
                    clientesConCadenciaSuperada.add(cliente);
                }
            }
        }

        if (clientesConCadenciaSuperada.size() > 0) {
            for (int i = 0; i < clientesConCadenciaSuperada.size(); i++) {
                NotificationModel notification = new NotificationModel();
                notification.setFecha(Calendar.getInstance().getTimeInMillis() / 1000);
                notification.setComercioId(Preferencias.getComercioIdFromSharedPreferences(contexto));
                notification.setClientId(clientesConCadenciaSuperada.get(i).getClientId());
                notification.setLeido(false);
                notification.setType(Constants.notificationCadenciaType);
                notificacionesCadencia.add(notification);
            }
        }

        return notificacionesCadencia;
    }

    private static boolean aSuperadoCadencia(ArrayList<ServiceModel> servicios, CadenciaModel cadencia, long fecha) {
        if (servicios.size() > 0) {
            for (int i = 0; i < servicios.size(); i++) {
                ServiceModel servicio = servicios.get(i);
                if (servicio.getFecha() > cadencia.getCandenciaTime()) {
                    return false;
                }
            }

            return true;
        } else {
            //si el usuario no tiene servicios, comparamos con la fecha de creación de usuario
            return fecha < cadencia.getCandenciaTime();
        }
    }

    private static boolean hasClientANotification(ArrayList<NotificationModel> notifications, long clientId) {
        boolean notificationExists = false;
        for (int i = 0; i < notifications.size(); i++) {
            NotificationModel notification = notifications.get(i);
            if (notification.getClientId() == clientId) {
                notificationExists = true;
            }
        }

        return notificationExists;
    }

    private static ArrayList<NotificationModel> checkCierreCajas(Context contexto) {
        ArrayList<NotificationModel> cierreCajas = new ArrayList<>();
        Date yesterday = DateFunctions.remove1DayToDate(new Date());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(DateFunctions.getBeginingOfDayFromDate(yesterday));
        long begginingOfDay = calendar1.getTimeInMillis() / 1000;
        calendar1.setTime(DateFunctions.getEndOfDayFromDate(yesterday));
        long endOfDay = calendar1.getTimeInMillis() / 1000;

        boolean cierreCajaExiste = checkCierreCajasInRange(begginingOfDay, endOfDay);
        boolean serviciosExist = Constants.databaseManager.servicesManager.getServicesForDate(yesterday).size() > 0;
        boolean notificationExist = checkCierreCajasNotificationForRange(begginingOfDay, endOfDay);

        if (notificationExist) {
            return cierreCajas;
        }

        if (!cierreCajaExiste && serviciosExist) {
            NotificationModel notification = new NotificationModel();
            notification.setComercioId(Preferencias.getComercioIdFromSharedPreferences(contexto));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(yesterday);
            notification.setFecha(calendar.getTimeInMillis() / 1000);
            notification.setLeido(false);
            notification.setType(Constants.notificationcajaType);
            cierreCajas.add(notification);
        }

        return cierreCajas;
    }

    private static boolean checkCierreCajasInRange(long beginingOfDay, long endOfDay) {
        ArrayList<CierreCajaModel> cierreCajas = Constants.databaseManager.cierreCajaManager.getCierreCajasFromDatabase();
        for (int i = 0; i < cierreCajas.size(); i++) {
            if (cierreCajas.get(i).getFecha() > beginingOfDay && cierreCajas.get(i).getFecha() < endOfDay) {
                return true;
            }
        }

        return false;
    }

    private static boolean checkCierreCajasNotificationForRange(long beginingOfDay, long endOfDay) {
        ArrayList<NotificationModel> notifications = Constants.databaseManager.notificationsManager.getNotificationsForType(Constants.notificationcajaType);
        for (int i = 0; i < notifications.size(); i++) {
            NotificationModel notification = notifications.get(i);
            if (notification.getFecha() > beginingOfDay && notification.getFecha() < endOfDay) {
                return true;
            }
        }

        return false;
    }

    private static ArrayList<NotificationModel> checkNotificacionesPersonalizadas(Context contexto) {
        ArrayList<ClientModel> clientes = Constants.databaseManager.clientsManager.getClientsFromDatabase();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateFunctions.getBeginingOfDayFromDate(new Date()));
        long begginingOfDay = calendar.getTimeInMillis() / 1000;
        calendar.setTime(DateFunctions.getEndOfDayFromDate(new Date()));
        long endOfDay = calendar.getTimeInMillis() / 1000;
        ArrayList<NotificationModel> notificaciones = new ArrayList<>();
        for (int i = 0; i < clientes.size(); i++) {
            ClientModel cliente = clientes.get(i);
            if (cliente.getFechaNotificacionPersonalizada() > begginingOfDay && cliente.getFechaNotificacionPersonalizada() < endOfDay && !clientHasNotificacionPersonalizada(cliente.getClientId())) {
                NotificationModel notificacion = new NotificationModel();
                notificacion.setFecha(cliente.getFechaNotificacionPersonalizada());
                notificacion.setClientId(cliente.getClientId());
                notificacion.setLeido(false);
                notificacion.setComercioId(Preferencias.getComercioIdFromSharedPreferences(contexto));
                notificacion.setDescripcion(cliente.getObservaciones());
                notificacion.setType(Constants.notificationpersonalizadaType);

                notificaciones.add(notificacion);
            }
        }

        return notificaciones;
    }

    private static boolean clientHasNotificacionPersonalizada(long clientId) {
        ArrayList<NotificationModel> notificaciones = Constants.databaseManager.notificationsManager.getNotificationsForType(Constants.notificationpersonalizadaType);
        for (int i = 0; i < notificaciones.size(); i++) {
            if (notificaciones.get(i).getClientId() == clientId) {
                return true;
            }
        }

        return false;
    }

    public static void checkNotificaciones(Context context) {
        ArrayList<NotificationModel> notificaciones = new ArrayList<>();
        notificaciones.addAll(checkBirthdays(context));
        notificaciones.addAll(checkCierreCajas(context));
        notificaciones.addAll(checkCadencias(context));
        notificaciones.addAll(checkNotificacionesPersonalizadas(context));
        saveNotifications(notificaciones);

        Constants.mainActivityReference.updateNotificationBadge();
    }

    private static void saveNotifications(ArrayList<NotificationModel> notifications) {
        Call<ArrayList<NotificationModel>> call = Constants.webServices.saveNotifications(notifications);
        call.enqueue(new Callback<ArrayList<NotificationModel>>() {
            @Override
            public void onResponse(Call<ArrayList<NotificationModel>> call, Response<ArrayList<NotificationModel>> response) {
                if (response.code() == 201) {
                    for (int i = 0; i < response.body().size(); i++) {
                        Constants.databaseManager.notificationsManager.addNotificationToDatabase(response.body().get(i));
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationModel>> call, Throwable t) {
                System.out.println("Error guardando notificaciones");
            }
        });
    }
}

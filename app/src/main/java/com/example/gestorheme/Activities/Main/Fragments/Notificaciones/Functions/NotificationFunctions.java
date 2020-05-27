package com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Functions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
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

    private static ArrayList<NotificationModel> checkBirthdays() {
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
                birthdayNotifications.add(createBirthdayNotification(todayBirthdayClients.get(i)));
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
            calendar.setTime(new Date(cliente.getFecha()));
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
        long begginingOfDay = DateFunctions.getBeginingOfDayFromDate(new Date()).getTime();
        long endOfDay = DateFunctions.getEndOfDayFromDate(new Date()).getTime();
        for (int i = 0; i < allNotifications.size(); i++) {
            NotificationModel notification = allNotifications.get(i);
            if (notification.getFecha() > begginingOfDay && notification.getFecha() < endOfDay) {
                todayNotifications.add(notification);
            }
        }

        return todayNotifications;
    }

    private static NotificationModel createBirthdayNotification(ClientModel cliente) {
        NotificationModel notification = new NotificationModel();
        notification.setComercioId(Constants.developmentComercioId);
        notification.setFecha(new Date().getTime());
        notification.setClientId(cliente.getClientId());
        notification.setLeido(false);
        notification.setType(Constants.notificationCumpleañosType);

        return notification;
    }

    private static ArrayList<NotificationModel> checkCadencias() {
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
                notification.setFecha(new Date().getTime());
                notification.setComercioId(Constants.developmentComercioId);
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

    private static ArrayList<NotificationModel> checkCierreCajas() {
        ArrayList<NotificationModel> cierreCajas = new ArrayList<>();
        Date yesterday = DateFunctions.remove1DayToDate(new Date());
        long begginingOfDay = DateFunctions.getBeginingOfDayFromDate(yesterday).getTime();
        long endOfDay = DateFunctions.getEndOfDayFromDate(yesterday).getTime();

        boolean cierreCajaExiste = checkCierreCajasInRange(begginingOfDay, endOfDay);
        boolean serviciosExist = Constants.databaseManager.servicesManager.getServicesForDate(yesterday).size() > 0;
        boolean notificationExist = checkCierreCajasNotificationForRange(begginingOfDay, endOfDay);

        if (notificationExist) {
            return cierreCajas;
        }

        if (!cierreCajaExiste && serviciosExist) {
            NotificationModel notification = new NotificationModel();
            notification.setComercioId(Constants.developmentComercioId);
            notification.setFecha(yesterday.getTime());
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

    private static ArrayList<NotificationModel> checkNotificacionesPersonalizadas() {
        //TODO notificaciones personalizadas

        /*let clientes: [ClientModel] = Constants.databaseManager.clientsManager.getAllClientsFromDatabase()
        let beginingOfDay: Int64 = Int64(AgendaFunctions.getBeginningOfDayFromDate(date: Date()).timeIntervalSince1970)
        let endOfDay: Int64 = Int64(AgendaFunctions.getEndOfDayFromDate(date: Date()).timeIntervalSince1970)
        var notificaciones: [NotificationModel] = []
        for cliente in clientes {
            if cliente.notificacionPersonalizada > beginingOfDay && cliente.notificacionPersonalizada < endOfDay && !existsNotificacionPersonalizada(clientId: cliente.id) {
                let notificacion: NotificationModel = createNotificacionPersonalizada(fecha: cliente.notificacionPersonalizada, clientId: cliente.id, descripcion: cliente.observaciones)
                notificaciones.append(notificacion)
                _ = Constants.databaseManager.notificationsManager.addNotificationToDatabase(newNotification: notificacion)
            }
        }

        Constants.cloudDatabaseManager.notificationManager.saveNotifications(notifications: notificaciones, delegate: nil)

        DispatchQueue.main.async {
            Constants.rootController.setNotificationBarItemBadge()
        }*/

        return new ArrayList<>();
    }

    public static void checkNotificaciones() {
        ArrayList<NotificationModel> notificaciones = new ArrayList<>();
        notificaciones.addAll(checkBirthdays());
        notificaciones.addAll(checkCierreCajas());
        notificaciones.addAll(checkCadencias());
        notificaciones.addAll(checkNotificacionesPersonalizadas());
        saveNotifications(notificaciones);
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

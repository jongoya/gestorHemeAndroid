package com.example.gestorheme.Common;

import android.content.Context;

import com.example.gestorheme.Activities.Empleados.Interfaces.EmpleadosRefreshInterface;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.Interfaces.ServicesRefreshInterface;
import com.example.gestorheme.Activities.Main.Fragments.ListaClientes.ClientesFragment;
import com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Functions.NotificationFunctions;
import com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Interfaces.NotificationsRefreshInterface;
import com.example.gestorheme.Activities.Main.MainActivity;
import com.example.gestorheme.Activities.TipoServicios.Interfaces.TipoServiciosRefreshInterface;
import com.example.gestorheme.Models.CierreCaja.CierreCajaModel;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.Models.Notification.NotificationModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.Models.TipoServicio.TipoServicioModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncronizationManager {

    public static void syncAllDataFromServer(MainActivity activity, Context context) {
        getAllClients(activity, context);
        getAllEmpleados(null,context);
        getTipoServiciosFromServer(null, context);
        getServiciosFromServer(null, context);
        getAllCierreCajas(context);
        getAllNotifications(null, context);
    }

    public static void getAllClients(MainActivity activity, Context contexto) {
        Call<ArrayList<ClientModel>> call = Constants.webServices.getAllClientes(Preferencias.getComercioIdFromSharedPreferences(contexto));
        call.enqueue(new Callback<ArrayList<ClientModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ClientModel>> call, Response<ArrayList<ClientModel>> response) {
                if (response.code() == 200 && response.body().size() > 0) {
                    for (int i = 0; i < response.body().size(); i++) {
                        ClientModel cliente = response.body().get(i);
                        if (Constants.databaseManager.clientsManager.getClientForClientId(cliente.getClientId()) != null) {
                            Constants.databaseManager.clientsManager.updateClientInDatabase(cliente);
                        } else {
                            Constants.databaseManager.clientsManager.addClientToDatabase(response.body().get(i));
                        }
                    }

                    ClientesFragment fragment = (ClientesFragment)activity.getSupportFragmentManager().findFragmentByTag("listaClientes");
                    if (fragment != null && fragment.isVisible()) {
                        fragment.refreshLayout.setRefreshing(false);
                        fragment.setClientList();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ClientModel>> call, Throwable t) {
                System.out.println("Error getting clients");
            }
        });
    }

    public static void getAllEmpleados(EmpleadosRefreshInterface delegate, Context contexto) {
        Call<ArrayList<EmpleadoModel>> call = Constants.webServices.getAllEmpleados(Preferencias.getComercioIdFromSharedPreferences(contexto));
        call.enqueue(new Callback<ArrayList<EmpleadoModel>>() {
            @Override
            public void onResponse(Call<ArrayList<EmpleadoModel>> call, Response<ArrayList<EmpleadoModel>> response) {
                if (response.code() == 200 && response.body().size() > 0) {
                    for (int i = 0; i < response.body().size(); i++) {
                        EmpleadoModel empleado = response.body().get(i);
                        if (Constants.databaseManager.empleadosManager.getEmpleadoForEmpleadoId(empleado.getEmpleadoId()) != null) {
                            Constants.databaseManager.empleadosManager.updateEmpleadoInDatabase(empleado);
                        } else {
                            Constants.databaseManager.empleadosManager.addEmpleadoToDatabase(empleado);
                        }
                    }

                    deleteLocalEmpleadosIfNeeded(response.body());

                    if (delegate != null) {
                        delegate.empleadosLoaded();
                    }
                } else {
                    if (delegate != null) {
                        delegate.errorLoadingEmpleados();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EmpleadoModel>> call, Throwable t) {
                System.out.println("Error getting empleados");
                if (delegate != null) {
                    delegate.errorLoadingEmpleados();
                }
            }
        });
    }

    private static void deleteLocalEmpleadosIfNeeded(ArrayList<EmpleadoModel> serverEmpleados) {
        ArrayList<EmpleadoModel> localEmpleados = Constants.databaseManager.empleadosManager.getEmpleadosFromDatabase();
        for (int i = 0; i < localEmpleados.size(); i++) {
            boolean estaEnServer = false;
            for (int j = 0; j < serverEmpleados.size(); j++) {
                if (serverEmpleados.get(j).getEmpleadoId() == localEmpleados.get(i).getEmpleadoId()) {
                    estaEnServer = true;
                }
            }

            if (!estaEnServer) {
                Constants.databaseManager.empleadosManager.deleteEmpleadoFromDatabase(localEmpleados.get(i).getEmpleadoId());
            }
        }
    }

    public static void getTipoServiciosFromServer(TipoServiciosRefreshInterface delegate, Context context) {
        Call <ArrayList<TipoServicioModel>> call = Constants.webServices.getAllTipoServicios(Preferencias.getComercioIdFromSharedPreferences(context));
        call.enqueue(new Callback<ArrayList<TipoServicioModel>>() {
            @Override
            public void onResponse(Call<ArrayList<TipoServicioModel>> call, Response<ArrayList<TipoServicioModel>> response) {
                if (response.code() == 200 && response.body().size() > 0) {
                    for (int i = 0; i < response.body().size(); i++) {
                        TipoServicioModel tipoServicio = response.body().get(i);
                        if (Constants.databaseManager.tipoServiciosManager.getServicioForServicioId(tipoServicio.getServicioId()) == null) {
                            Constants.databaseManager.tipoServiciosManager.addTipoServicioToDatabase(tipoServicio);
                        }
                    }
                    if (delegate != null) {
                        delegate.serviciosLoaded();
                    }
                } else {
                    if (delegate != null) {
                        delegate.errorLoadingServicios();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TipoServicioModel>> call, Throwable t) {
                System.out.println("Error recogiendo los tipo de servicios");
                if (delegate != null) {
                    delegate.errorLoadingServicios();
                }
            }
        });
    }

    public static void getServiciosFromServer(ServicesRefreshInterface delegate, Context context) {
        Call <ArrayList<ServiceModel>> call = Constants.webServices.getAllServicios(Preferencias.getComercioIdFromSharedPreferences(context));
        call.enqueue(new Callback<ArrayList<ServiceModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ServiceModel>> call, Response<ArrayList<ServiceModel>> response) {
                if (response.code() == 200 && response.body().size() > 0) {
                    for (int i = 0; i < response.body().size(); i++) {
                        ServiceModel service = response.body().get(i);
                        if (Constants.databaseManager.servicesManager.getServiceForServiceId(service.getServiceId()) != null) {
                            Constants.databaseManager.servicesManager.updateServiceInDatabase(service);
                        } else {
                            Constants.databaseManager.servicesManager.addServiceToDatabase(service);
                        }
                    }

                    deleteLocalServicesIfNeeded(response.body());

                    if (delegate != null) {
                        delegate.servicesLoaded();
                    }
                } else {
                    if (delegate != null) {
                        delegate.errorLoadingServices();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ServiceModel>> call, Throwable t) {
                System.out.println("Error guardando los servicios");
                if (delegate != null) {
                    delegate.errorLoadingServices();
                }
            }
        });
    }

    private static void deleteLocalServicesIfNeeded(ArrayList<ServiceModel> serverServices) {
        ArrayList<ServiceModel> localServices = Constants.databaseManager.servicesManager.getServicesFromDatabase();
        for (int i = 0; i < localServices.size(); i++) {
            boolean estaEnServer = false;
            for (int j = 0; j < serverServices.size(); j++) {
                if (serverServices.get(j).getServiceId() == localServices.get(i).getServiceId()) {
                    estaEnServer = true;
                }
            }

            if (!estaEnServer) {
                Constants.databaseManager.servicesManager.deleteServiceFromDatabase(localServices.get(i).getServiceId());
            }
        }
    }

    private static void getAllCierreCajas(Context context) {
        Call <ArrayList<CierreCajaModel>> call = Constants.webServices.getAllCierreCajas(Preferencias.getComercioIdFromSharedPreferences(context));
        call.enqueue(new Callback<ArrayList<CierreCajaModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CierreCajaModel>> call, Response<ArrayList<CierreCajaModel>> response) {
                if (response.code() == 200 && response.body().size() > 0) {
                    for (int i = 0; i < response.body().size(); i++) {
                        CierreCajaModel cierreCaja = response.body().get(i);
                        if (Constants.databaseManager.cierreCajaManager.getCierreCajaForCierreCajaId(cierreCaja.getCajaId()) == null) {
                            Constants.databaseManager.cierreCajaManager.addCierreCajaToDatabase(cierreCaja);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CierreCajaModel>> call, Throwable t) {
                System.out.println("Error recogiendo el cierre de cajas");
            }
        });
    }

    public static void getAllNotifications(NotificationsRefreshInterface delegate, Context context) {
        Call <ArrayList<NotificationModel>> call = Constants.webServices.getAllNotifications(Preferencias.getComercioIdFromSharedPreferences(context));
        call.enqueue(new Callback<ArrayList<NotificationModel>>() {
            @Override
            public void onResponse(Call<ArrayList<NotificationModel>> call, Response<ArrayList<NotificationModel>> response) {
                if (response.code() == 200) {
                    for (int i = 0; i < response.body().size(); i++) {
                        NotificationModel notification = response.body().get(i);
                        if (Constants.databaseManager.notificationsManager.getNotificationForId(notification.getNotificationId()) != null) {
                            Constants.databaseManager.notificationsManager.updateNotificationInDatabase(notification);
                        } else {
                            Constants.databaseManager.notificationsManager.addNotificationToDatabase(notification);
                        }
                    }

                    deleteLocalNotificationsIfNeeded(response.body());

                    removeOldNotifications();

                    if (delegate != null) {
                        delegate.notificationsLoaded();
                    } else {
                        NotificationFunctions.checkNotificaciones(context);
                    }
                } else {
                    if (delegate != null) {
                        delegate.errorLoadingNotifications();
                    } else {
                        NotificationFunctions.checkNotificaciones(context);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationModel>> call, Throwable t) {
                System.out.println("Error recogiendo las notificaciones");
                NotificationFunctions.checkNotificaciones(context);
                if (delegate != null) {
                    delegate.errorLoadingNotifications();
                }
            }
        });
    }

    private static void deleteLocalNotificationsIfNeeded(ArrayList<NotificationModel> serverNotifications) {
        ArrayList<NotificationModel> localNotifications = Constants.databaseManager.notificationsManager.getNotificationsFromDatabase();
        if (serverNotifications.size() == 0) {
            for (int i = 0; i < localNotifications.size(); i++) {
                Constants.databaseManager.notificationsManager.deleteNotificationFromDatabase(localNotifications.get(i).getNotificationId());
            }

            return;
        }

        for (int i = 0; i < localNotifications.size(); i++) {
            boolean estaEnServer = false;
            for (int j = 0; j < serverNotifications.size(); j++) {
                if (serverNotifications.get(j).getNotificationId() == localNotifications.get(i).getNotificationId()) {
                    estaEnServer = true;
                }
            }

            if (!estaEnServer) {
                Constants.databaseManager.notificationsManager.deleteNotificationFromDatabase(localNotifications.get(i).getNotificationId());
            }
        }
    }

    private static void removeOldNotifications() {
        ArrayList<NotificationModel> notificacionesPorEliminar  = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -7);
        ArrayList<NotificationModel> notificaciones = Constants.databaseManager.notificationsManager.getNotificationsFromDatabase();
        for (int i = 0; i < notificaciones.size(); i++) {
            if (notificaciones.get(i).getFecha() < cal.getTimeInMillis() / 1000) {
                notificacionesPorEliminar.add(notificaciones.get(i));
            }
        }

        Call<Void> call = Constants.webServices.deleteNotificacions(notificacionesPorEliminar);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() != 200) {
                    System.out.println("Error eliminando notificaciones");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Error eliminando notificaciones");
            }
        });
    }
}

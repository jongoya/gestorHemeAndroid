package com.example.gestorheme.Common;

import com.example.gestorheme.Activities.Empleados.Interfaces.EmpleadosRefreshInterface;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.Interfaces.ServicesRefreshInterface;
import com.example.gestorheme.Activities.Main.Fragments.ListaClientes.ClientesFragment;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncronizationManager {

    public static void syncAllDataFromServer(MainActivity activity) {
        getAllClients(activity);
        getAllEmpleados(null);
        getTipoServiciosFromServer(null);
        getServiciosFromServer(null);
        getAllCierreCajas();
        getAllNotifications(null);
    }

    public static void getAllClients(MainActivity activity) {
        Call<ArrayList<ClientModel>> call = Constants.webServices.getAllClientes();
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

    public static void getAllEmpleados(EmpleadosRefreshInterface delegate) {
        Call<ArrayList<EmpleadoModel>> call = Constants.webServices.getAllEmpleados();
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
                //TODO eliminar empleado
            }
        }
    }

    public static void getTipoServiciosFromServer(TipoServiciosRefreshInterface delegate) {
        Call <ArrayList<TipoServicioModel>> call = Constants.webServices.getAllTipoServicios();
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

    public static void getServiciosFromServer(ServicesRefreshInterface delegate) {
        Call <ArrayList<ServiceModel>> call = Constants.webServices.getAllServicios();
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

    private static void getAllCierreCajas() {
        Call <ArrayList<CierreCajaModel>> call = Constants.webServices.getAllCierreCajas();
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

    public static void getAllNotifications(NotificationsRefreshInterface delegate) {
        Call <ArrayList<NotificationModel>> call = Constants.webServices.getAllNotifications();
        call.enqueue(new Callback<ArrayList<NotificationModel>>() {
            @Override
            public void onResponse(Call<ArrayList<NotificationModel>> call, Response<ArrayList<NotificationModel>> response) {
                if (response.code() == 200 && response.body().size() > 0) {
                    for (int i = 0; i < response.body().size(); i++) {
                        NotificationModel notification = response.body().get(i);
                        if (Constants.databaseManager.notificationsManager.getNotificationForId(notification.getNotificationId()) != null) {
                            Constants.databaseManager.notificationsManager.updateNotificationInDatabase(notification);
                        } else {
                            Constants.databaseManager.notificationsManager.addNotificationToDatabase(notification);
                        }
                    }

                    deleteLocalNotificationsIfNeeded(response.body());

                    if (delegate != null) {
                        delegate.notificationsLoaded();
                    }
                } else {
                    if (delegate != null) {
                        delegate.errorLoadingNotifications();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationModel>> call, Throwable t) {
                System.out.println("Error recogiendo las notificaciones");
                if (delegate != null) {
                    delegate.errorLoadingNotifications();
                }
            }
        });
    }

    private static void deleteLocalNotificationsIfNeeded(ArrayList<NotificationModel> serverNotifications) {
        ArrayList<NotificationModel> localnotifications = Constants.databaseManager.notificationsManager.getNotificationsFromDatabase();
        for (int i = 0; i < localnotifications.size(); i++) {
            boolean estaEnServer = false;
            for (int j = 0; j < serverNotifications.size(); j++) {
                if (serverNotifications.get(j).getNotificationId() == localnotifications.get(i).getNotificationId()) {
                    estaEnServer = true;
                }
            }

            if (!estaEnServer) {
                Constants.databaseManager.notificationsManager.deleteNotificationFromDatabase(localnotifications.get(i).getNotificationId());
            }
        }
    }
}

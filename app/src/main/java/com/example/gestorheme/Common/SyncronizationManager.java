package com.example.gestorheme.Common;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import com.example.gestorheme.Activities.Empleados.Interfaces.EmpleadosRefreshInterface;
import com.example.gestorheme.Activities.Main.Fragments.ListaClientes.ClientesFragment;
import com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Functions.NotificationFunctions;
import com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Interfaces.NotificationsRefreshInterface;
import com.example.gestorheme.Activities.Main.MainActivity;
import com.example.gestorheme.Activities.TipoServicios.Interfaces.TipoServiciosRefreshInterface;
import com.example.gestorheme.Models.Cesta.CestaModel;
import com.example.gestorheme.Models.CierreCaja.CierreCajaModel;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.Models.Notification.NotificationModel;
import com.example.gestorheme.Models.Producto.ProductoModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.Models.TipoServicio.TipoServicioModel;
import com.example.gestorheme.Models.Venta.VentaModel;

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
        getServiciosFromServer(context);
        getAllCierreCajas(context);
        getAllNotifications(null, context, null);
        getProductos(context);
        getCestas(context);
        getVentas(context);
    }

    public static void getAllClients(MainActivity activity, Context contexto) {
        Call<ArrayList<ClientModel>> call = Constants.webServices.getAllClientes(Preferencias.getComercioIdFromSharedPreferences(contexto));
        call.enqueue(new Callback<ArrayList<ClientModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ClientModel>> call, Response<ArrayList<ClientModel>> response) {
                if (response.code() == 200 && response.body().size() > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("DESCARGA DE CLIENTES");
                            for (int i = 0; i < response.body().size(); i++) {
                                ClientModel cliente = response.body().get(i);
                                if (Constants.databaseManager.clientsManager.getClientForClientId(cliente.getClientId()) != null) {
                                    Constants.databaseManager.clientsManager.updateClientInDatabase(cliente);
                                } else {
                                    Constants.databaseManager.clientsManager.addClientToDatabase(response.body().get(i));
                                }
                            }

                            updateListaClientes(activity);
                        }
                    }).start();
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
                    System.out.println("DESCARGA DE EMPLEADOS");
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
                    System.out.println("DESCARGA DE TIPO DE SERVICIOS");
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

    public static void getServiciosFromServer(Context context) {
        Call <ArrayList<ServiceModel>> call = Constants.webServices.getAllServicios(Preferencias.getComercioIdFromSharedPreferences(context));
        call.enqueue(new Callback<ArrayList<ServiceModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ServiceModel>> call, Response<ArrayList<ServiceModel>> response) {
                if (response.code() == 200 && response.body().size() > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("DESCARGA DE SERVICIOS");
                            for (int i = 0; i < response.body().size(); i++) {
                                ServiceModel service = response.body().get(i);
                                if (Constants.databaseManager.servicesManager.getServiceForServiceId(service.getServiceId()) != null) {
                                    Constants.databaseManager.servicesManager.updateServiceInDatabase(service);
                                } else {
                                    Constants.databaseManager.servicesManager.addServiceToDatabase(service);
                                }
                            }

                            deleteLocalServicesIfNeeded(response.body(), Constants.databaseManager.servicesManager.getServicesFromDatabase());
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ServiceModel>> call, Throwable t) {
                System.out.println("Error guardando los servicios");
            }
        });
    }

    public static void deleteLocalServicesIfNeeded(ArrayList<ServiceModel> serverServices, ArrayList<ServiceModel> localServices) {
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
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < response.body().size(); i++) {
                                CierreCajaModel cierreCaja = response.body().get(i);
                                if (Constants.databaseManager.cierreCajaManager.getCierreCajaForCierreCajaId(cierreCaja.getCajaId()) == null) {
                                    Constants.databaseManager.cierreCajaManager.addCierreCajaToDatabase(cierreCaja);
                                }
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CierreCajaModel>> call, Throwable t) {
                System.out.println("Error recogiendo el cierre de cajas");
            }
        });
    }

    public static void getAllNotifications(NotificationsRefreshInterface delegate, Context context, Activity activity) {
        Call <ArrayList<NotificationModel>> call = Constants.webServices.getAllNotifications(Preferencias.getComercioIdFromSharedPreferences(context));
        call.enqueue(new Callback<ArrayList<NotificationModel>>() {
            @Override
            public void onResponse(Call<ArrayList<NotificationModel>> call, Response<ArrayList<NotificationModel>> response) {
                if (response.code() == 200) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
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

                            if (delegate != null && activity != null) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        delegate.notificationsLoaded();
                                    }
                                });
                            } else {
                                NotificationFunctions.checkNotificaciones(context);
                            }

                            Constants.mainActivityReference.updateNotificationBadge();
                        }
                    }).start();
                } else {
                    if (delegate != null) {
                        delegate.errorLoadingNotifications();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                NotificationFunctions.checkNotificaciones(context);
                            }
                        }).start();
                    }

                    Constants.mainActivityReference.updateNotificationBadge();
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

    private static void updateListaClientes(MainActivity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ClientesFragment fragment = (ClientesFragment)activity.getSupportFragmentManager().findFragmentByTag("listaClientes");
                if (fragment != null && fragment.isVisible()) {
                    fragment.refreshLayout.setRefreshing(false);
                    fragment.setClientList();
                }
            }
        });
    }

    public static void deleteLocalProductosIfNeeded(ArrayList<ProductoModel> serverProductos, ArrayList<ProductoModel> localProductos) {
        for (int i = 0; i < localProductos.size(); i++) {
            boolean estaEnServer = false;
            for (int j = 0; j < serverProductos.size(); j++) {
                if (serverProductos.get(j).getProductoId() == localProductos.get(i).getProductoId()) {
                    estaEnServer = true;
                }
            }

            if (!estaEnServer) {
                Constants.databaseManager.servicesManager.deleteServiceFromDatabase(localProductos.get(i).getProductoId());
            }
        }
    }

    public static void deleteLocalCestasIfNeeded(ArrayList<CestaModel> serverCestas, ArrayList<CestaModel> localCestas) {
        for (int i = 0; i < localCestas.size(); i++) {
            boolean estaEnServer = false;
            for (int j = 0; j < serverCestas.size(); j++) {
                if (serverCestas.get(j).getCestaId() == localCestas.get(i).getCestaId()) {
                    estaEnServer = true;
                }
            }

            if (!estaEnServer) {
                Constants.databaseManager.cestaManager.deleteCesta(localCestas.get(i));
            }
        }
    }

    public static void deleteLocalVentasIfNeeded(ArrayList<VentaModel> serverVentas, ArrayList<VentaModel> localVentas) {
        for (int i = 0; i < localVentas.size(); i++) {
            boolean estaEnServer = false;
            for (int j = 0; j < serverVentas.size(); j++) {
                if (serverVentas.get(j).getVentaId() == localVentas.get(i).getVentaId()) {
                    estaEnServer = true;
                }
            }

            if (!estaEnServer) {
                Constants.databaseManager.ventaManager.deleteVenta(localVentas.get(i));
            }
        }
    }

    public static void getProductos(Context context) {
        Call<ArrayList<ProductoModel>> call = Constants.webServices.getProductos(Preferencias.getComercioIdFromSharedPreferences(context));
        call.enqueue(new Callback<ArrayList<ProductoModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductoModel>> call, Response<ArrayList<ProductoModel>> response) {
                if (response.code() == 200) {
                    ArrayList<ProductoModel> productos = response.body();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (ProductoModel producto: productos) {
                                if (Constants.databaseManager.productoManager.getProductoWithProductId(producto.getProductoId()) == null) {
                                    Constants.databaseManager.productoManager.saveProducto(producto);
                                } else {
                                    Constants.databaseManager.productoManager.updateProducto(producto);
                                }
                            }
                            ArrayList<ProductoModel> localProductos = Constants.databaseManager.productoManager.getAllProductos();
                            deleteLocalProductosIfNeeded(productos, localProductos);

                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProductoModel>> call, Throwable t) {
            }
        });
    }

    public static void getCestas(Context contexto) {
        Call<ArrayList<CestaModel>> call = Constants.webServices.getCestas(Preferencias.getComercioIdFromSharedPreferences(contexto));
        call.enqueue(new Callback<ArrayList<CestaModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CestaModel>> call, Response<ArrayList<CestaModel>> response) {
                if (response.code() == 200) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (CestaModel cesta: response.body()) {
                                if (Constants.databaseManager.cestaManager.getCesta(cesta.getCestaId()) == null) {
                                    Constants.databaseManager.cestaManager.saveCesta(cesta);
                                } else {
                                    Constants.databaseManager.cestaManager.updateCesta(cesta);
                                }
                            }

                            ArrayList<CestaModel> localCestas = Constants.databaseManager.cestaManager.getAllCestas();
                            deleteLocalCestasIfNeeded(response.body(), localCestas);
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CestaModel>> call, Throwable t) {
            }
        });
    }

    public static void getVentas(Context context) {
        Call<ArrayList<VentaModel>> call = Constants.webServices.getVentas(Preferencias.getComercioIdFromSharedPreferences(context));
        call.enqueue(new Callback<ArrayList<VentaModel>>() {
            @Override
            public void onResponse(Call<ArrayList<VentaModel>> call, Response<ArrayList<VentaModel>> response) {
                if (response.code() == 200) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (VentaModel venta: response.body()) {
                                if (Constants.databaseManager.ventaManager.getVenta(venta.getVentaId()) == null) {
                                    Constants.databaseManager.ventaManager.saveVenta(venta);
                                } else {
                                    Constants.databaseManager.ventaManager.updateVenta(venta);
                                }
                            }

                            ArrayList<VentaModel> localVentas = Constants.databaseManager.ventaManager.getAllVentas();
                            deleteLocalVentasIfNeeded(response.body(), localVentas);
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<VentaModel>> call, Throwable t) {

            }
        });
    }
}

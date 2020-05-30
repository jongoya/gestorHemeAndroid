package com.example.gestorheme.ApiServices;

import com.example.gestorheme.Models.CierreCaja.CierreCajaModel;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.Models.Notification.NotificationModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.Models.TipoServicio.TipoServicioModel;
import com.example.gestorheme.Models.WebServicesModels.ClientesMasServiciosModel;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface WebServices {

    @GET("get_clientes")
    Call<ArrayList<ClientModel>> getAllClientes();

    @Headers("Content-Type: application/json")
    @POST("save_cliente")
    Call<ClientesMasServiciosModel> saveCliente(@Body ClientesMasServiciosModel clienteMasServicios);

    @Headers("Content-Type: application/json")
    @PUT("update_cliente")
    Call<ClientesMasServiciosModel> updateCliente(@Body ClientModel cliente);

    @GET("get_servicios")
    Call<ArrayList<ServiceModel>> getAllServicios();

    @Headers("Content-Type: application/json")
    @POST("save_servicio")
    Call<ServiceModel> saveService(@Body ServiceModel service);

    @Headers("Content-Type: application/json")
    @PUT("update_servicio")
    Call<ServiceModel> updateService(@Body ServiceModel service);

    @Headers("Content-Type: application/json")
    @POST("delete_servicio")
    Call<Void> deleteService(@Body ServiceModel servicio);

    @Headers("Content-Type: application/json")
    @POST("save_cierre_caja")
    Call<CierreCajaModel> saveCierreCaja(@Body CierreCajaModel cierreCaja);

    @GET("get_cierre_cajas")
    Call<ArrayList<CierreCajaModel>> getAllCierreCajas();

    @GET("get_notifications")
    Call<ArrayList<NotificationModel>> getAllNotifications();

    @Headers("Content-Type: application/json")
    @POST("save_notifications")
    Call<ArrayList<NotificationModel>> saveNotifications(@Body ArrayList<NotificationModel> notifications);

    @Headers("Content-Type: application/json")
    @PUT("update_notificacion_personalizada")
    Call<ClientModel> updateNotificacionPersonalizada(@Body ClientModel cliente);

    @Headers("Content-Type: application/json")
    @PUT("update_notification")
    Call<NotificationModel> updateNotificacion(@Body NotificationModel notification);

    @Headers("Content-Type: application/json")
    @PUT("update_notifications")
    Call<ArrayList<NotificationModel>> updateNotificaciones(@Body ArrayList<NotificationModel> notifications);

    @Headers("Content-Type: application/json")
    @POST("delete_notification")
    Call<Void> deleteNotificacion(@Body NotificationModel notification);

    @GET("get_empleados")
    Call<ArrayList<EmpleadoModel>> getAllEmpleados();

    @Headers("Content-Type: application/json")
    @POST("save_empleado")
    Call <EmpleadoModel> saveEmpleado(@Body EmpleadoModel empleado);

    @Headers("Content-Type: application/json")
    @PUT("update_empleado")
    Call <EmpleadoModel> updateEmpleado(@Body EmpleadoModel empleado);

    @Headers("Content-Type: application/json")
    @POST("delete_empleado")
    Call<EmpleadoModel> deleteEmpleado(@Body EmpleadoModel empleado);

    @GET("get_tipo_servicios")
    Call<ArrayList<TipoServicioModel>> getAllTipoServicios();

    @Headers("Content-Type: application/json")
    @POST("save_tipo_servicio")
    Call<TipoServicioModel> saveTipoServicio(@Body TipoServicioModel tipoServicio);
}

package com.example.gestorheme.ApiServices;

import com.example.gestorheme.Models.CierreCaja.CierreCajaModel;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.Models.EstiloPublico.EstiloPublicoModel;
import com.example.gestorheme.Models.Login.LoginModel;
import com.example.gestorheme.Models.LoginMasDispositivos.LoginMasDispositivosModel;
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
import retrofit2.http.Path;

public interface WebServices {

    @GET("get_clientes/{comercioId}")
    Call<ArrayList<ClientModel>> getAllClientes(@Path("comercioId") long comercioId);

    @Headers("Content-Type: application/json")
    @POST("save_cliente")
    Call<ClientesMasServiciosModel> saveCliente(@Body ClientesMasServiciosModel clienteMasServicios);

    @Headers("Content-Type: application/json")
    @PUT("update_cliente")
    Call<ClientesMasServiciosModel> updateCliente(@Body ClientModel cliente);

    @GET("get_servicios/{comercioId}")
    Call<ArrayList<ServiceModel>> getAllServicios(@Path("comercioId") long comercioId);

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

    @GET("get_cierre_cajas/{comercioId}")
    Call<ArrayList<CierreCajaModel>> getAllCierreCajas(@Path("comercioId") long comercioId);

    @GET("get_notifications/{comercioId}")
    Call<ArrayList<NotificationModel>> getAllNotifications(@Path("comercioId") long comercioId);

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

    @Headers("Content-Type: application/json")
    @POST("delete_notifications")
    Call<Void> deleteNotificacions(@Body ArrayList<NotificationModel> notifications);

    @GET("get_empleados/{comercioId}")
    Call<ArrayList<EmpleadoModel>> getAllEmpleados(@Path("comercioId") long comercioId);

    @Headers("Content-Type: application/json")
    @POST("save_empleado")
    Call <EmpleadoModel> saveEmpleado(@Body EmpleadoModel empleado);

    @Headers("Content-Type: application/json")
    @PUT("update_empleado")
    Call <EmpleadoModel> updateEmpleado(@Body EmpleadoModel empleado);

    @Headers("Content-Type: application/json")
    @POST("delete_empleado")
    Call<EmpleadoModel> deleteEmpleado(@Body EmpleadoModel empleado);

    @GET("get_tipo_servicios/{comercioId}")
    Call<ArrayList<TipoServicioModel>> getAllTipoServicios(@Path("comercioId") long comercioId);

    @Headers("Content-Type: application/json")
    @POST("save_tipo_servicio")
    Call<TipoServicioModel> saveTipoServicio(@Body TipoServicioModel tipoServicio);

    @Headers("Content-Type: application/json")
    @POST("login_comercio")
    Call<LoginMasDispositivosModel> login(@Body LoginModel login);

    @Headers("Content-Type: application/json")
    @POST("login_swap_devices")
    Call<LoginMasDispositivosModel> swapDevicesAndLogin(@Body LoginMasDispositivosModel login);

    @GET("get_estilo_publico/{bundleId}")
    Call<EstiloPublicoModel> getEstiloPublico(@Path("bundleId") String bundleId);
}


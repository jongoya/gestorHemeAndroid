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
import retrofit2.http.DELETE;
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

    @GET("get_empleados")
    Call<ArrayList<EmpleadoModel>> getAllEmpleados();

    @GET("get_tipo_servicios")
    Call<ArrayList<TipoServicioModel>> getAllTipoServicios();


}

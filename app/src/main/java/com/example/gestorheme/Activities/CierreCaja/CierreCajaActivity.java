package com.example.gestorheme.Activities.CierreCaja;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.gestorheme.Activities.TextInputField.TextInputFieldActivity;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.Preferencias;
import com.example.gestorheme.Models.Cesta.CestaModel;
import com.example.gestorheme.Models.CierreCaja.CierreCajaModel;
import com.example.gestorheme.Models.Notification.NotificationModel;
import com.example.gestorheme.Models.Producto.ProductoModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.Models.TipoServicio.TipoServicioModel;
import com.example.gestorheme.Models.Venta.VentaModel;
import com.example.gestorheme.R;
import java.util.ArrayList;
import java.util.Calendar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CierreCajaActivity extends AppCompatActivity {
    private static final int NUMSERVICIOS_FIELD_REF = 0;
    private static final int TOTALCAJA_FIELD_REF = 1;
    private static final int TOTALPRODUCTOS_FIELD_REF = 2;
    private static final int EFECTIVO_FIELD_REF = 3;
    private static final int TARJETA_FIELD_REF = 4;

    private TextView numeroServiciosLabel;
    private TextView totalCajaLabel;
    private TextView totalProductosLabel;
    private TextView efectivoLabel;
    private TextView tarjetaLabel;
    private ConstraintLayout rootLayout;
    private RelativeLayout loadingView;
    private TextView numeroServiciosField;
    private TextView totalCajaField;
    private TextView totalProductosField;
    private TextView efectivoField;
    private TextView tarjetaField;
    private TextView titulo;
    private ImageView numeroServiciosArrow;
    private ImageView totalCajaArrow;
    private ImageView totalProductosArrow;
    private ImageView efectivoArrow;
    private ImageView tarjetaArrow;
    private RelativeLayout numeroServiciosDivider;
    private RelativeLayout totalCajaDivider;
    private RelativeLayout totalProductosDivider;
    private RelativeLayout efectivoDivider;
    private RelativeLayout saveButton;
    private ImageView saveImage;


    private long fecha;
    private CierreCajaModel cierreCaja = new CierreCajaModel();
    private NotificationModel notification;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cierre_caja_layout);
        AppStyle.setStatusBarColor(this);
        getCierreCajaIntent();
        getFields();
        customizeLabels();
        customizeFields();
        customizeArrows();
        customizeDividers();
        customizeBackground();
        customizeButton();
        setOnClickListeners();
        setFields();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void customizeLabels() {
        numeroServiciosLabel.setTextColor(AppStyle.getSecondaryTextColor());
        totalCajaLabel.setTextColor(AppStyle.getSecondaryTextColor());
        totalProductosLabel.setTextColor(AppStyle.getSecondaryTextColor());
        efectivoLabel.setTextColor(AppStyle.getSecondaryTextColor());
        tarjetaLabel.setTextColor(AppStyle.getSecondaryTextColor());
    }

    private void customizeFields() {
        numeroServiciosField.setTextColor(AppStyle.getPrimaryTextColor());
        totalCajaField.setTextColor(AppStyle.getPrimaryTextColor());
        totalProductosField.setTextColor(AppStyle.getPrimaryTextColor());
        efectivoField.setTextColor(AppStyle.getPrimaryTextColor());
        tarjetaField.setTextColor(AppStyle.getPrimaryTextColor());
        titulo.setTextColor(AppStyle.getPrimaryTextColor());
    }

    private void customizeArrows() {
        numeroServiciosArrow.setColorFilter(AppStyle.getSecondaryColor());
        totalCajaArrow.setColorFilter(AppStyle.getSecondaryColor());
        totalProductosArrow.setColorFilter(AppStyle.getSecondaryColor());
        efectivoArrow.setColorFilter(AppStyle.getSecondaryColor());
        tarjetaArrow.setColorFilter(AppStyle.getSecondaryColor());
    }

    private void customizeDividers() {
        numeroServiciosDivider.setBackgroundColor(AppStyle.getSecondaryColor());
        totalCajaDivider.setBackgroundColor(AppStyle.getSecondaryColor());
        totalProductosDivider.setBackgroundColor(AppStyle.getSecondaryColor());
        efectivoDivider.setBackgroundColor(AppStyle.getSecondaryColor());
    }

    private void customizeBackground() {
        rootLayout.setBackgroundColor(AppStyle.getBackgroundColor());
    }

    private void customizeButton() {
        CommonFunctions.customizeViewWithImage(getApplicationContext(), saveButton, saveImage, AppStyle.getPrimaryColor(), AppStyle.getPrimaryColor());
    }

    private void getCierreCajaIntent() {
        fecha = getIntent().getLongExtra("fecha", 0);
        notification = (NotificationModel) getIntent().getSerializableExtra("notification");
    }

    private void getFields() {
        numeroServiciosLabel = findViewById(R.id.numeroServiciosLabel);
        totalCajaLabel = findViewById(R.id.totalCajaLabel);
        totalProductosLabel = findViewById(R.id.totalProductosLabel);
        efectivoLabel = findViewById(R.id.efectivoLabel);
        tarjetaLabel = findViewById(R.id.tarjetaLabel);
        rootLayout = findViewById(R.id.root);
        numeroServiciosField = findViewById(R.id.numeroServiciosField);
        totalCajaField = findViewById(R.id.totalCajaField);
        totalProductosField = findViewById(R.id.totalProductosField);
        efectivoField = findViewById(R.id.efectivoField);
        tarjetaField = findViewById(R.id.tarjetaField);
        numeroServiciosArrow = findViewById(R.id.numeroArrow);
        totalCajaArrow = findViewById(R.id.totalCajaArrow);
        totalProductosArrow = findViewById(R.id.totalProductosArrow);
        efectivoArrow = findViewById(R.id.efectivoArrow);
        tarjetaArrow = findViewById(R.id.tarjetaArrow);
        numeroServiciosDivider = findViewById(R.id.divider1);
        totalCajaDivider = findViewById(R.id.divider2);
        totalProductosDivider = findViewById(R.id.divider3);
        efectivoDivider = findViewById(R.id.divider4);
        titulo = findViewById(R.id.titulo);
        saveButton = findViewById(R.id.save_button);
        saveImage = findViewById(R.id.saveImage);
    }

    private void setFields() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fecha * 1000);
        ArrayList<ServiceModel> servicios = Constants.databaseManager.servicesManager.getServicesForDate(calendar.getTime());
        ArrayList<CestaModel> cestas = Constants.databaseManager.cestaManager.getCestasForDay(calendar.getTime());
        cierreCaja.setNumeroServicios(servicios.size());
        numeroServiciosLabel.setText(String.valueOf(servicios.size()));

        cierreCaja.setTotalCaja(getTotalCaja(servicios, cestas));
        totalCajaLabel.setText(String.valueOf(cierreCaja.getTotalCaja()) + " €");

        cierreCaja.setTotalProductos(getTotalProductos(cestas));
        totalProductosLabel.setText(String.valueOf(cierreCaja.getTotalProductos()) + " €");

        cierreCaja.setEfectivo(getTotalEfectivo(servicios, cestas));
        efectivoLabel.setText(String.valueOf(cierreCaja.getEfectivo()) + " €");

        cierreCaja.setTarjeta(getTotalTarjeta(servicios, cestas));
        tarjetaLabel.setText(String.valueOf(cierreCaja.getTarjeta()) + " €");
    }

    private double getTotalCaja(ArrayList<ServiceModel> servicios, ArrayList<CestaModel> cestas) {
        double totalCaja = 0;
        for (int i = 0; i < servicios.size(); i++) {
            totalCaja += servicios.get(i).getPrecio();
        }

        for (int i = 0; i < cestas.size(); i++) {
            ArrayList<VentaModel> ventas = Constants.databaseManager.ventaManager.getVentas(cestas.get(i).getCestaId());
            for (int j = 0; j < ventas.size(); j++) {
                ProductoModel producto = Constants.databaseManager.productoManager.getProductoWithProductId(ventas.get(j).getProductoId());
                totalCaja += producto.getPrecio() * ventas.get(j).getCantidad();
            }
        }

        return totalCaja;
    }

    private double getTotalProductos(ArrayList<CestaModel> cestas) {
        double totalProductos = 0.0;

        for (int i = 0; i < cestas.size(); i++) {
            ArrayList<VentaModel> ventas = Constants.databaseManager.ventaManager.getVentas(cestas.get(i).getCestaId());
            for (int j = 0; j < ventas.size(); j++) {
                ProductoModel producto = Constants.databaseManager.productoManager.getProductoWithProductId(ventas.get(j).getProductoId());
                totalProductos += producto.getPrecio() * ventas.get(j).getCantidad();
            }
        }

        return totalProductos;
    }

    private double getTotalEfectivo(ArrayList<ServiceModel> servicios, ArrayList<CestaModel> cestas) {
        double totalEfectivo = 0.0;
        for (int i = 0; i < servicios.size(); i++) {
            if (servicios.get(i).isEfectivo()) {
                totalEfectivo += servicios.get(i).getPrecio();
            }
        }

        for (int i = 0; i < cestas.size(); i++) {
            if (cestas.get(i).isEfectivo()) {
                ArrayList<VentaModel> ventas = Constants.databaseManager.ventaManager.getVentas(cestas.get(i).getCestaId());
                for (int j = 0; j < ventas.size(); j++) {
                    ProductoModel producto = Constants.databaseManager.productoManager.getProductoWithProductId(ventas.get(j).getProductoId());
                    totalEfectivo += producto.getPrecio() * ventas.get(j).getCantidad();
                }
            }
        }

        return totalEfectivo;
    }

    private double getTotalTarjeta(ArrayList<ServiceModel> servicios, ArrayList<CestaModel> cestas) {
        double totalTarjeta = 0.0;
        for (int i = 0; i < servicios.size(); i++) {
            if (!servicios.get(i).isEfectivo()) {
                totalTarjeta += servicios.get(i).getPrecio();
            }
        }

        for (int i = 0; i < cestas.size(); i++) {
            if (!cestas.get(i).isEfectivo()) {
                ArrayList<VentaModel> ventas = Constants.databaseManager.ventaManager.getVentas(cestas.get(i).getCestaId());
                for (int j = 0; j < ventas.size(); j++) {
                    ProductoModel producto = Constants.databaseManager.productoManager.getProductoWithProductId(ventas.get(j).getProductoId());
                    totalTarjeta += producto.getPrecio() * ventas.get(j).getCantidad();
                }
            }
        }

        return totalTarjeta;
    }

    private void setOnClickListeners() {
        findViewById(R.id.numeroServiciosView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("keyboard", InputType.TYPE_CLASS_PHONE);
                startActivityForResult(intent, NUMSERVICIOS_FIELD_REF);

            }
        });

        findViewById(R.id.totalCajaView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("keyboard", InputType.TYPE_CLASS_PHONE);
                startActivityForResult(intent, TOTALCAJA_FIELD_REF);

            }
        });

        findViewById(R.id.totalProductosView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("keyboard", InputType.TYPE_CLASS_PHONE);
                startActivityForResult(intent, TOTALPRODUCTOS_FIELD_REF);

            }
        });

        findViewById(R.id.efectivoView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("keyboard", InputType.TYPE_CLASS_PHONE);
                startActivityForResult(intent, EFECTIVO_FIELD_REF);

            }
        });

        findViewById(R.id.tarjetaView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("keyboard", InputType.TYPE_CLASS_PHONE);
                startActivityForResult(intent, TARJETA_FIELD_REF);

            }
        });

        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cierreCaja.setFecha(fecha);
                saveCierreCajaInServer(cierreCaja);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        String texto = data.getStringExtra("TEXTO").replace(",", ".").replaceAll("[^\\d.]", "");
        if (texto.length() == 0) {
            return;
        }
        switch (requestCode) {
            case NUMSERVICIOS_FIELD_REF:
                int numServicios = Integer.parseInt(texto);
                cierreCaja.setNumeroServicios(numServicios);
                numeroServiciosLabel.setText(texto);
                break;
            case TOTALCAJA_FIELD_REF:
                double totalCaja = Double.parseDouble(texto);
                cierreCaja.setTotalCaja(totalCaja);
                totalCajaLabel.setText(texto + " €");
                break;
            case TOTALPRODUCTOS_FIELD_REF:
                double totalProductos = Double.parseDouble(texto);
                cierreCaja.setTotalProductos(totalProductos);
                totalProductosLabel.setText(texto + " €");
                break;
            case EFECTIVO_FIELD_REF:
                double efectivo = Double.parseDouble(texto);
                cierreCaja.setEfectivo(efectivo);
                efectivoLabel.setText(texto + " €");
                break;
            case TARJETA_FIELD_REF:
                double tarjeta = Double.parseDouble(texto);
                cierreCaja.setTarjeta(tarjeta);
                tarjetaLabel.setText(texto + " €");
                break;
            default:
                break;
        }
    }

    private void saveCierreCajaInServer(CierreCajaModel cierreCaja) {
        loadingView = CommonFunctions.createLoadingStateView(getApplicationContext(), "Guardando cierre de caja");
        rootLayout.addView(loadingView);
        cierreCaja.setComercioId(Preferencias.getComercioIdFromSharedPreferences(getApplicationContext()));
        Call<CierreCajaModel> call = Constants.webServices.saveCierreCaja(cierreCaja);
        call.enqueue(new Callback<CierreCajaModel>() {
            @Override
            public void onResponse(Call<CierreCajaModel> call, Response<CierreCajaModel> response) {
                if (response.code() == 201) {
                    Constants.databaseManager.cierreCajaManager.addCierreCajaToDatabase(response.body());
                    if (notification != null) {
                        deleteNotificacion();
                    } else {
                        rootLayout.removeView(loadingView);
                        CierreCajaActivity.super.onBackPressed();
                    }
                }  else if (response.code() == Constants.logoutResponseValue) {
                    rootLayout.removeView(loadingView);
                    CommonFunctions.logout(CierreCajaActivity.this);
                } else {
                    rootLayout.removeView(loadingView);
                    CommonFunctions.showGenericAlertMessage(CierreCajaActivity.this, "Error guardando el cierre de caja");
                }
            }

            @Override
            public void onFailure(Call<CierreCajaModel> call, Throwable t) {
                rootLayout.removeView(loadingView);
                CommonFunctions.showGenericAlertMessage(CierreCajaActivity.this, "Error guardando el cierre de caja");
            }
        });
    }

    private void deleteNotificacion() {
        Call<Void> call = Constants.webServices.deleteNotificacion(notification);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                rootLayout.removeView(loadingView);
                if (response.code() == 200) {
                    Constants.databaseManager.notificationsManager.deleteNotificationFromDatabase(notification.getNotificationId());
                    CierreCajaActivity.super.onBackPressed();
                }  else if (response.code() == Constants.logoutResponseValue) {
                    CommonFunctions.logout(CierreCajaActivity.this);
                } else {
                    CommonFunctions.showGenericAlertMessage(CierreCajaActivity.this, "Error eliminando la notificación");
                }

                Constants.mainActivityReference.updateNotificationBadge();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                rootLayout.removeView(loadingView);
                CommonFunctions.showGenericAlertMessage(CierreCajaActivity.this, "Error eliminando la notificación");
            }
        });
    }
}

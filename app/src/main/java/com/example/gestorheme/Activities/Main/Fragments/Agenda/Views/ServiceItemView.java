package com.example.gestorheme.Activities.Main.Fragments.Agenda.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gestorheme.Activities.Cesta.CestaActivity;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.Interfaces.ServiceItemViewInterface;
import com.example.gestorheme.Activities.ServiceDetail.ServiceDetailActivity;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.OnSwipeTouchListener;
import com.example.gestorheme.Models.Cesta.CestaModel;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.Models.Producto.ProductoModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.Models.TipoServicio.TipoServicioModel;
import com.example.gestorheme.Models.Venta.VentaModel;
import com.example.gestorheme.R;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceItemView extends RelativeLayout {
    private TextView nombreLabel;
    private TextView serviciosLabel;
    private TextView profesionalLabel;
    private RelativeLayout backgroundLayout;
    private int xDelta = 0;
    private ServiceItemViewInterface delegate;

    public ServiceItemView(Context context, ServiceModel servicio, CestaModel cesta, ServiceItemViewInterface delegate) {
        super(context);
        View.inflate(context, R.layout.service_item_view, this);
        this.delegate = delegate;
        getFields();
        customizeFields();
        setOnClickListeners(servicio, cesta, context);

        if (servicio != null) {
            setServiceFields(servicio);
        } else {
            setCestaFields(cesta);
        }
    }

    private void getFields() {
        nombreLabel = findViewById(R.id.nombreLabel);
        serviciosLabel = findViewById(R.id.serviciosLabel);
        profesionalLabel = findViewById(R.id.profesionalLabel);
        backgroundLayout = findViewById(R.id.background);
    }

    private void customizeFields() {
        nombreLabel.setTextColor(AppStyle.getPrimaryTextColor());
        serviciosLabel.setTextColor(AppStyle.getPrimaryTextColor());
        profesionalLabel.setTextColor(AppStyle.getPrimaryTextColor());
    }

    private void setOnClickListeners(final ServiceModel servicio, CestaModel cesta, final Context context) {
        if (servicio != null) {
            findViewById(R.id.cross_view).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteService(servicio);
                }
            });
        }

        if (servicio != null) {
            backgroundLayout.setOnTouchListener(new OnSwipeTouchListener(context) {
                @Override
                public void onSwipeLeft() {
                    super.onSwipeLeft();
                    if (xDelta == 0) {
                        leftSwipeAnimation(context);
                    }
                }

                @Override
                public void onSwipeRight() {
                    super.onSwipeRight();
                    if (xDelta == 1) {
                        rightSwipeAnimation(context);
                    }
                }

                @Override
                public void onClick() {
                    super.onClick();
                    ClientModel cliente = Constants.databaseManager.clientsManager.getClientForClientId(servicio.getClientId());
                    Intent intent = new Intent(context, ServiceDetailActivity.class);
                    intent.putExtra("servicio", servicio);
                    intent.putExtra("cliente", cliente);
                    context.startActivity(intent);
                }
            });
        } else {
            backgroundLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<VentaModel> ventas = Constants.databaseManager.ventaManager.getVentas(cesta.getCestaId());
                    Intent intent = new Intent(context, CestaActivity.class);
                    intent.putExtra("ventas", ventas);
                    intent.putExtra("cesta", cesta);
                    context.startActivity(intent);
                }
            });
        }
    }

    private void leftSwipeAnimation(final Context context) {
        Animation slide = new TranslateAnimation(-CommonFunctions.convertToPx(xDelta, context), -CommonFunctions.convertToPx(65, context), 0, 0);
        slide.setDuration(600);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        backgroundLayout.startAnimation(slide);
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                backgroundLayout.clearAnimation();
                xDelta = 1;
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(backgroundLayout.getWidth(), backgroundLayout.getHeight());
                lp.setMargins(-CommonFunctions.convertToPx(65, context), CommonFunctions.convertToPx(5, context), -CommonFunctions.convertToPx(65, context), CommonFunctions.convertToPx(5, context));
                backgroundLayout.setLayoutParams(lp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void rightSwipeAnimation(final Context context) {
        Animation slide = new TranslateAnimation(0, CommonFunctions.convertToPx(65, context) , 0, 0);
        slide.setDuration(600);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        backgroundLayout.startAnimation(slide);
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                backgroundLayout.clearAnimation();
                xDelta = 0;
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(backgroundLayout.getWidth(), backgroundLayout.getHeight());
                lp.setMargins(0, CommonFunctions.convertToPx(5, context), 0, CommonFunctions.convertToPx(5, context));
                backgroundLayout.setLayoutParams(lp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void setServiceFields(ServiceModel servicio) {
        EmpleadoModel empleado = Constants.databaseManager.empleadosManager.getEmpleadoForEmpleadoId(servicio.getEmpleadoId());
        ClientModel cliente = Constants.databaseManager.clientsManager.getClientForClientId(servicio.getClientId());
        if (cliente == null) {
            nombreLabel.setText("Actualizando...");
        } else {
            nombreLabel.setText(cliente.getNombre() + " " + cliente.getApellidos());
        }
        serviciosLabel.setText(getServicesStringFromIds(servicio.getServicios()));
        profesionalLabel.setText(empleado.getNombre());
        backgroundLayout.getBackground().setColorFilter(Color.rgb((int)empleado.getRedColorValue(),(int)empleado.getGreenColorValue(), (int)empleado.getBlueColorValue()), PorterDuff.Mode.SRC_ATOP);
    }

    private void setCestaFields(CestaModel cesta) {
        ClientModel cliente = Constants.databaseManager.clientsManager.getClientForClientId(cesta.getClientId());
        nombreLabel.setText("Venta producto");
        serviciosLabel.setText(cliente.getNombre() + " " + cliente.getApellidos());
        profesionalLabel.setText("Precio: " + String.valueOf(calculatePriceForCesta(cesta)) + " €");
        backgroundLayout.getBackground().setColorFilter(AppStyle.getPrimaryColor(), PorterDuff.Mode.SRC_ATOP);
    }

    private String getServicesStringFromIds(ArrayList tipoServicios) {
        String serviciosString = "";
        for (int i = 0; i < tipoServicios.size(); i++) {
            TipoServicioModel servicio = Constants.databaseManager.tipoServiciosManager.getServicioForServicioId((Long) tipoServicios.get(i));
            serviciosString += servicio.getNombre() + ", ";
        }
        return serviciosString;
    }

    private double calculatePriceForCesta(CestaModel cesta) {
        double precio = 0.0;
        ArrayList<VentaModel> ventas = Constants.databaseManager.ventaManager.getVentas(cesta.getCestaId());
        for (int i = 0; i < ventas.size(); i++) {
            ProductoModel producto = Constants.databaseManager.productoManager.getProductoWithProductId(ventas.get(i).getProductoId());
            precio += ventas.get(i).getCantidad() * producto.getPrecio();
        }

        return precio;
    }

    private void deleteService(ServiceModel servicio) {
        delegate.showLoadingState("Eliminando servicio");
        Call<Void> call = Constants.webServices.deleteService(servicio);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                delegate.hideLoadingState();
                if (response.code() == 200) {
                    Constants.databaseManager.servicesManager.deleteServiceFromDatabase(servicio.getServiceId());
                    delegate.serviceRemoved();
                }  else if (response.code() == Constants.logoutResponseValue) {
                    delegate.logout();
                } else {
                    delegate.showErrorMessage("Error eliminando servicio");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                delegate.hideLoadingState();
                delegate.showErrorMessage("Error eliminando servicio");
            }
        });
    }
}

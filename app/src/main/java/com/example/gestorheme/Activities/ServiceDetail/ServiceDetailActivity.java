package com.example.gestorheme.Activities.ServiceDetail;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.gestorheme.Activities.ClientListSelector.ClientListSelectorActivity;
import com.example.gestorheme.Activities.DatePicker.DatePickerActivity;
import com.example.gestorheme.Activities.ItemSelector.ItemSelectorActivity;
import com.example.gestorheme.Activities.Main.Fragments.ListaClientes.Models.ListaClienteCellModel;
import com.example.gestorheme.Activities.TextInputField.TextInputFieldActivity;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Common.Preferencias;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Notification.NotificationModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.Models.TipoServicio.TipoServicioModel;
import com.example.gestorheme.R;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceDetailActivity extends AppCompatActivity {
    private static final int NOMBRE_FIELD_REF = 0;
    private static final int FECHA_FIELD_REF = 1;
    private static final int PROFESIONAL_FIELD_REF = 2;
    private static final int SERVICIOS_FIELD_REF = 3;
    private static final int PRECIO_FIELD_REF = 4;
    private static final int OBSERVACION_FIELD_REF = 5;

    private TextView nombreLabel;
    private TextView fechaLabel;
    private TextView profesionalLabel;
    private TextView serviciosLabel;
    private TextView precioLabel;
    private EditText observacionesLabel;
    private ConstraintLayout rootLayout;
    private RelativeLayout loadingLayout;
    private Switch efectivoSwitch;
    private TextView nombreField;
    private TextView fechaField;
    private TextView profesionalField;
    private TextView serviciosField;
    private TextView precioField;
    private TextView efectivoField;
    private TextView titulo;
    private ImageView nombreArrow;
    private ImageView fechaArrow;
    private ImageView profesionalArrow;
    private ImageView serviciosArrow;
    private ImageView precioArrow;
    private RelativeLayout nombreDivider;
    private RelativeLayout fechaDivider;
    private RelativeLayout profesionalDivider;
    private RelativeLayout servicioDivider;
    private RelativeLayout precioDivider;
    private LinearLayout background;
    private RelativeLayout saveButton;
    private ImageView saveImage;

    private ServiceModel servicio;
    private ClientModel cliente;
    private long date;
    private boolean isEditingService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_detail_activity);
        AppStyle.setStatusBarColor(this);
        getFields();
        customizeLabels();
        customizeFields();
        customizeArrows();
        customizeDividers();
        customizeSwitch();
        customizeBackground();
        customizeButtons();
        getServiceIntent();
        setOnClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getServiceIntent() {
        servicio = (ServiceModel) getIntent().getSerializableExtra("servicio");
        cliente = (ClientModel) getIntent().getSerializableExtra("cliente");
        date = getIntent().getLongExtra("fecha", 0);
        if (servicio != null) {
            isEditingService = true;
            setServiceFields();
        } else if (cliente != null) {
            servicio = new ServiceModel();
            nombreLabel.setText(cliente.getNombre() + " " + cliente.getApellidos());
        } else {
            servicio = new ServiceModel();
            servicio.setFecha(date);
            fechaLabel.setText(DateFunctions.convertTimestampToServiceDateString(date));
        }
    }

    private void getFields() {
        nombreLabel = findViewById(R.id.nombreLabel);
        fechaLabel = findViewById(R.id.fechaLabel);
        profesionalLabel = findViewById(R.id.profesionalLabel);
        serviciosLabel = findViewById(R.id.serviciosLabel);
        precioLabel = findViewById(R.id.precioLabel);
        observacionesLabel = findViewById(R.id.observacionesLabel);
        rootLayout = findViewById(R.id.root);
        efectivoSwitch = findViewById(R.id.efectivoSwitch);
        nombreField = findViewById(R.id.nombreField);
        fechaField = findViewById(R.id.fechaField);
        profesionalField = findViewById(R.id.profesionalField);
        serviciosField = findViewById(R.id.serviciosField);
        precioField = findViewById(R.id.precioField);
        efectivoField = findViewById(R.id.efectivoField);
        titulo = findViewById(R.id.titulo);
        nombreArrow = findViewById(R.id.nombreArrow);
        fechaArrow = findViewById(R.id.fechaArrow);
        profesionalArrow = findViewById(R.id.profesionalArrow);
        serviciosArrow = findViewById(R.id.serviciosArrow);
        precioArrow = findViewById(R.id.precioArrow);
        nombreDivider = findViewById(R.id.nombreDivider);
        fechaDivider = findViewById(R.id.fechaDivider);
        profesionalDivider = findViewById(R.id.profesionalDivider);
        servicioDivider = findViewById(R.id.serviciosDivider);
        precioDivider = findViewById(R.id.precioDivider);
        background = findViewById(R.id.background);
        saveButton = findViewById(R.id.saveButton);
        saveImage = findViewById(R.id.saveImage);
    }

    private void setOnClickListeners() {
        if (cliente == null) {
            findViewById(R.id.nombre_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ServiceDetailActivity.this, ClientListSelectorActivity.class);
                    startActivityForResult(intent, NOMBRE_FIELD_REF);
                }
            });
        }

        findViewById(R.id.fecha_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DatePickerActivity.class);
                intent.putExtra("timestamp", servicio.getFecha());
                intent.putExtra("showTimePicker", true);
                startActivityForResult(intent, FECHA_FIELD_REF);
            }
        });

        findViewById(R.id.profesional_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ItemSelectorActivity.class);
                intent.putExtra("array", Constants.databaseManager.empleadosManager.getEmpleadosFromDatabase());
                startActivityForResult(intent, PROFESIONAL_FIELD_REF);
            }
        });

        findViewById(R.id.servicios_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ItemSelectorActivity.class);
                intent.putExtra("array", Constants.databaseManager.tipoServiciosManager.getTipoServiciosFromDatabase());
                startActivityForResult(intent, SERVICIOS_FIELD_REF);
            }
        });

        findViewById(R.id.precio_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", String.valueOf( servicio.getPrecio() > 0 ? servicio.getPrecio() : ""));
                intent.putExtra("keyboard", InputType.TYPE_CLASS_PHONE);
                startActivityForResult(intent, PRECIO_FIELD_REF);
            }
        });

        findViewById(R.id.observacionesLabel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", servicio.getObservaciones());
                intent.putExtra("keyboard", InputType.TYPE_CLASS_TEXT);
                startActivityForResult(intent, OBSERVACION_FIELD_REF);
            }
        });

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFields();
            }
        });

        efectivoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                servicio.setEfectivo(b);
            }
        });
    }

    private void customizeLabels() {
        nombreLabel.setTextColor(AppStyle.getSecondaryTextColor());
        fechaLabel.setTextColor(AppStyle.getSecondaryTextColor());
        profesionalLabel.setTextColor(AppStyle.getSecondaryTextColor());
        serviciosLabel.setTextColor(AppStyle.getSecondaryTextColor());
        precioLabel.setTextColor(AppStyle.getSecondaryTextColor());
    }

    private void customizeFields() {
        nombreField.setTextColor(AppStyle.getPrimaryTextColor());
        fechaField.setTextColor(AppStyle.getPrimaryTextColor());
        profesionalField.setTextColor(AppStyle.getPrimaryTextColor());
        serviciosField.setTextColor(AppStyle.getPrimaryTextColor());
        precioField.setTextColor(AppStyle.getPrimaryTextColor());
        efectivoField.setTextColor(AppStyle.getPrimaryTextColor());
        titulo.setTextColor(AppStyle.getPrimaryTextColor());
    }

    private void customizeArrows() {
        nombreArrow.setColorFilter(AppStyle.getSecondaryTextColor());
        fechaArrow.setColorFilter(AppStyle.getSecondaryTextColor());
        profesionalArrow.setColorFilter(AppStyle.getSecondaryTextColor());
        serviciosArrow.setColorFilter(AppStyle.getSecondaryTextColor());
        precioArrow.setColorFilter(AppStyle.getSecondaryTextColor());
    }

    private void customizeDividers() {
        nombreDivider.setBackgroundColor(AppStyle.getSecondaryColor());
        fechaDivider.setBackgroundColor(AppStyle.getSecondaryColor());
        profesionalDivider.setBackgroundColor(AppStyle.getSecondaryColor());
        servicioDivider.setBackgroundColor(AppStyle.getSecondaryColor());
        precioDivider.setBackgroundColor(AppStyle.getSecondaryColor());
    }

    private void customizeSwitch() {
        ColorStateList colorStates = new ColorStateList(
                new int[][]{new int[]{android.R.attr.state_checked}, new int[]{}},
                new int[]{AppStyle.getPrimaryColor(), AppStyle.getSecondaryColor()}
        );
        efectivoSwitch.setThumbTintList(colorStates);
        efectivoSwitch.setTrackTintList(colorStates);
    }

    private void customizeBackground() {
        background.setBackgroundColor(AppStyle.getBackgroundColor());
    }

    private void customizeButtons() {
        CommonFunctions.customizeViewWithImage(getApplicationContext(), saveButton, saveImage, AppStyle.getPrimaryColor(), AppStyle.getPrimaryColor());
    }

    private void setServiceFields() {
        ClientModel cliente = Constants.databaseManager.clientsManager.getClientForClientId(servicio.getClientId());
        nombreLabel.setText(cliente.getNombre() + " " + cliente.getApellidos());
        fechaLabel.setText(DateFunctions.convertTimestampToServiceDateString(servicio.getFecha()));
        profesionalLabel.setText(Constants.databaseManager.empleadosManager.getEmpleadoForEmpleadoId(servicio.getEmpleadoId()).getNombre());
        serviciosLabel.setText(CommonFunctions.getServiciosForServicio(servicio.getServicios()));
        precioLabel.setText(Double.toString(servicio.getPrecio()) + " €");
        observacionesLabel.setText(servicio.getObservaciones().length() > 0 ? servicio.getObservaciones() : "Añade una observación");
        efectivoSwitch.setChecked(servicio.isEfectivo());
    }

    private String getServiciosString(ArrayList<TipoServicioModel> servicios) {
        String serviciosString = "";
        for (int i = 0; i < servicios.size(); i++) {
            serviciosString += servicios.get(i).getNombre() + ", ";
        }

        return serviciosString;
    }

    private ArrayList getServiciosIdFromServicios(ArrayList<TipoServicioModel> servicios) {
        ArrayList serviciosId = new ArrayList();
        for (int i = 0; i < servicios.size(); i++) {
            serviciosId.add(servicios.get(i).getServicioId());
        }

        return  serviciosId;
    }

    private void checkFields() {
        if (nombreLabel.getText().length() == 0) {
            CommonFunctions.showGenericAlertMessage(this, "Debe incluir el nombre y apellidos");
            return;
        }

        if (fechaLabel.getText().length() == 0) {
            CommonFunctions.showGenericAlertMessage(this, "Debe incluir una fecha para el servicio");
            return;
        }

        if (profesionalLabel.getText().length() == 0) {
            CommonFunctions.showGenericAlertMessage(this, "Debe asignar un profesional al servicio");
            return;
        }

        if (serviciosLabel.getText().length() == 0) {
            CommonFunctions.showGenericAlertMessage(this, "Debe indicar el tipo de servicio a realizar");
            return;
        }

        saveService();
    }

    private void saveService() {
        if (isEditingService) {
            actualizarServicioEnServidor();
        } else {
            if (cliente.getClientId() == 0) {
                Intent intent = getIntent();
                intent.putExtra("Servicio", servicio);
                setResult(RESULT_OK, intent);
                ServiceDetailActivity.super.onBackPressed();
            } else {
                servicio.setClientId(cliente.getClientId());
                servicio.setComercioId(Preferencias.getComercioIdFromSharedPreferences(getApplicationContext()));
                guardarServicioEnServidor();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case NOMBRE_FIELD_REF:
                ListaClienteCellModel model = (ListaClienteCellModel) data.getSerializableExtra("cliente");
                nombreLabel.setText(model.getCliente().getNombre() + " " + model.getCliente().getApellidos());
                servicio.setClientId(model.getCliente().getClientId());
                cliente = model.getCliente();
                break;
            case PROFESIONAL_FIELD_REF:
                servicio.setEmpleadoId(data.getExtras().getLong("ITEM"));
                profesionalLabel.setText(Constants.databaseManager.empleadosManager.getEmpleadoForEmpleadoId(data.getExtras().getLong("ITEM")).getNombre());
                break;
            case SERVICIOS_FIELD_REF:
                serviciosLabel.setText(getServiciosString((ArrayList<TipoServicioModel>) data.getSerializableExtra("ITEM")));
                servicio.setServicios(getServiciosIdFromServicios((ArrayList<TipoServicioModel>) data.getSerializableExtra("ITEM")));
                break;
            case PRECIO_FIELD_REF:
                String texto = data.getStringExtra("TEXTO").replaceAll("[^\\d.]", "");
                if (texto.length() == 0) {
                    texto = "0.0";
                }
                servicio.setPrecio(Double.valueOf(texto));
                precioLabel.setText(texto + " €");
                break;
            case OBSERVACION_FIELD_REF:
                observacionesLabel.setText(data.getStringExtra("TEXTO"));
                servicio.setObservaciones(data.getStringExtra("TEXTO"));
                break;
            case FECHA_FIELD_REF:
                fechaLabel.setText(DateFunctions.convertTimestampToServiceDateString(data.getLongExtra("timestamp", 0)));
                servicio.setFecha(data.getLongExtra("timestamp", 0));
                break;
            default:
                break;
        }
    }

    private void guardarServicioEnServidor() {
        loadingLayout = CommonFunctions.createLoadingStateView(getApplicationContext(), "Guardando servicio");
        rootLayout.addView(loadingLayout);
        Call<ServiceModel> call = Constants.webServices.saveService(servicio);
        call.enqueue(new Callback<ServiceModel>() {
            @Override
            public void onResponse(Call<ServiceModel> call, Response<ServiceModel> response) {
                rootLayout.removeView(loadingLayout);
                if (response.code() == 201) {
                    Constants.databaseManager.servicesManager.addServiceToDatabase(response.body());
                    ArrayList<NotificationModel> notificaciones = Constants.databaseManager.notificationsManager.getNotificationsForType(Constants.notificationCadenciaType);
                    for (int i = 0; i < notificaciones.size(); i++) {
                        if (notificaciones.get(i).getClientId() == response.body().getClientId()) {
                            Constants.databaseManager.notificationsManager.deleteNotificationFromDatabase(notificaciones.get(i).getNotificationId());
                        }
                    }
                    ServiceDetailActivity.super.onBackPressed();
                }  else if (response.code() == Constants.logoutResponseValue) {
                    CommonFunctions.logout(ServiceDetailActivity.this);
                }  else {
                    CommonFunctions.showGenericAlertMessage(ServiceDetailActivity.this, "Error guardando servicio");
                }
            }

            @Override
            public void onFailure(Call<ServiceModel> call, Throwable t) {
                rootLayout.removeView(loadingLayout);
                CommonFunctions.showGenericAlertMessage(ServiceDetailActivity.this, "Error guardando servicio");
            }
        });
    }

    private void actualizarServicioEnServidor() {
        loadingLayout = CommonFunctions.createLoadingStateView(getApplicationContext(), "Actualizando servicio");
        rootLayout.addView(loadingLayout);
        Call<ServiceModel> call = Constants.webServices.updateService(servicio);
        call.enqueue(new Callback<ServiceModel>() {
            @Override
            public void onResponse(Call<ServiceModel> call, Response<ServiceModel> response) {
                if (response.code() == 200) {
                    rootLayout.removeView(loadingLayout);
                    Constants.databaseManager.servicesManager.updateServiceInDatabase(response.body());
                    ServiceDetailActivity.super.onBackPressed();
                }  else if (response.code() == Constants.logoutResponseValue) {
                    CommonFunctions.logout(ServiceDetailActivity.this);
                }  else {
                    CommonFunctions.showGenericAlertMessage(ServiceDetailActivity.this, "Error actualizando el servicio");
                }
            }

            @Override
            public void onFailure(Call<ServiceModel> call, Throwable t) {
                rootLayout.removeView(loadingLayout);
                CommonFunctions.showGenericAlertMessage(ServiceDetailActivity.this, "Error actualizando el servicio");
            }
        });
    }
}

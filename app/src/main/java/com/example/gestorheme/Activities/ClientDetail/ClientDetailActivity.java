package com.example.gestorheme.Activities.ClientDetail;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.gestorheme.Activities.ClientDetail.Fragment.DatePickerFragment;
import com.example.gestorheme.Activities.ClientDetail.Views.ServiceView;
import com.example.gestorheme.Activities.DatePicker.DatePickerActivity;
import com.example.gestorheme.Activities.ItemSelector.ItemSelectorActivity;
import com.example.gestorheme.Activities.ServiceDetail.ServiceDetailActivity;
import com.example.gestorheme.Activities.TextInputField.TextInputFieldActivity;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Common.Views.AddServiceButton;
import com.example.gestorheme.Common.Views.ServiceAlarmButton;
import com.example.gestorheme.Common.Views.ServiceHeaderTextView;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Notification.NotificationModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.Models.WebServicesModels.ClientesMasServiciosModel;
import com.example.gestorheme.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientDetailActivity extends AppCompatActivity {
    private static final int NOMBRE_FIELD_REF = 0;
    private static final int APELLIDOS_FIELD_REF = 1;
    private static final int TELEFONO_FIELD_REF = 2;
    private static final int FECHA_FIELD_REF = 3;
    private static final int EMAIL_FIELD_REF = 4;
    private static final int DIRECCION_FIELD_REF = 5;
    private static final int OBSERVACIONES_FIELD_REF = 6;
    private static final int CADENCIA_FIELD_REF = 7;
    private static final int REQUEST_IMAGE_CAPTURE = 8;
    private static final int SERVICE_DETAIL_REF = 9;

    private TextView nombreLabel;
    private TextView apellidosLabel;
    private TextView fechaLabel;
    private TextView telefonoLabel;
    private TextView emailLabel;
    private TextView direccionLabel;
    private TextView cadenciaLabel;
    private EditText observacionLabel;
    private CircleImageView clientImage;
    private LinearLayout scrollContentView;
    private RelativeLayout phoneButton;
    private ConstraintLayout rootLayout;
    private RelativeLayout loadingState;
    private ServiceAlarmButton alarmButton;
    private ImageView alarmImage;

    private ClientModel cliente;
    public boolean isClientDetail = false;
    private int observacionesViewLayoutPosition = 9;
    private ArrayList<ServiceModel> serviceArray = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.client_detail_layout);
        getClientIntent();
        getFields();
    }

    @Override
    protected void onResume() {
        super.onResume();
        removeServicesFromScrollView();

        if (isClientDetail) {
            addAlarmButton();
        }

        getClientDetails();

        if (isClientDetail) {
            setClientDetails();
        }

        addAddServiceButton();
        setOnClickListeners();
    }

    private void getClientIntent() {
        cliente = (ClientModel) getIntent().getSerializableExtra("Cliente");
        if (cliente != null) {
            isClientDetail = true;
        } else {
            cliente = new ClientModel();
        }
    }

    private void getClientDetails() {
        ArrayList<ServiceModel> services = serviceArray;

        if (cliente.getClientId() != 0) {
            services = Constants.databaseManager.servicesManager.getServicesForClient(cliente.getClientId());
        }

        addServiceViews(services);
    }

    private void addServiceViews(ArrayList<ServiceModel> services) {
        ArrayList<ServiceView> futurosServicios = new ArrayList<>();
        ArrayList<ServiceView> antiguosServicios = new ArrayList<>();
        for (int i = 0; i < services.size(); i++) {
            ServiceModel servicio = services.get(i);
            ServiceView serviceView = new ServiceView(this, servicio, servicio.getClientId() != 0 ? "" : cliente.getNombre(), servicio.getClientId() != 0 ? "" : cliente.getApellidos());
            if (isClientDetail) {
                serviceView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ServiceModel service = ((ServiceView)view).service;
                        Intent intent = new Intent(ClientDetailActivity.this, ServiceDetailActivity.class);
                        intent.putExtra("servicio", service);
                        intent.putExtra("cliente", cliente);
                        startActivityForResult(intent, SERVICE_DETAIL_REF);
                    }
                });
            }

            if (serviceView.service.getFecha() > new Date().getTime()) {
                futurosServicios.add(serviceView);
            } else {
                antiguosServicios.add(serviceView);
            }
        }

        if (futurosServicios.size() > 0) {
            addServiceHeader("FUTUROS SERVICIOS");
            for (int j = 0; j < futurosServicios.size(); j++) {
                scrollContentView.addView(futurosServicios.get(j));
            }
        }

        if (antiguosServicios.size() > 0) {
            addServiceHeader("ANTIGUOS SERVICIOS");
            for (int j = 0; j < antiguosServicios.size(); j++) {
                scrollContentView.addView(antiguosServicios.get(j));
            }
        }
    }

    private void getFields() {
        nombreLabel = findViewById(R.id.nombreLabel);
        apellidosLabel = findViewById(R.id.apellidosLabel);
        fechaLabel = findViewById(R.id.fechaLabel);
        telefonoLabel = findViewById(R.id.telefonoLabel);
        emailLabel = findViewById(R.id.emailLabel);
        direccionLabel = findViewById(R.id.direccionLabel);
        cadenciaLabel = findViewById(R.id.cadenciaLabel);
        clientImage = findViewById(R.id.client_image);
        observacionLabel = findViewById(R.id.observacionesLabel);
        scrollContentView = findViewById(R.id.scrollContentView);
        phoneButton = findViewById(R.id.phone_button);
        rootLayout = findViewById(R.id.root);

        if (cliente.getClientId() == 0) {
            phoneButton.setVisibility(View.INVISIBLE);
        }
    }

    private void setOnClickListeners() {
        findViewById(R.id.nombreLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", nombreLabel.getText().toString());
                intent.putExtra("keyboard", InputType.TYPE_CLASS_TEXT);
                startActivityForResult(intent, NOMBRE_FIELD_REF);
            }
        });

        findViewById(R.id.apellidosLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", apellidosLabel.getText().toString());
                intent.putExtra("keyboard", InputType.TYPE_CLASS_TEXT);
                startActivityForResult(intent, APELLIDOS_FIELD_REF);
            }
        });

        findViewById(R.id.fechaLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DatePickerActivity.class);
                intent.putExtra("timestamp", cliente.getFecha());
                startActivityForResult(intent, FECHA_FIELD_REF);
            }
        });

        findViewById(R.id.telefonoLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", telefonoLabel.getText().toString());
                intent.putExtra("keyboard", InputType.TYPE_CLASS_PHONE);
                startActivityForResult(intent, TELEFONO_FIELD_REF);
            }
        });

        findViewById(R.id.emailLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", emailLabel.getText().toString());
                intent.putExtra("keyboard", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                startActivityForResult(intent, EMAIL_FIELD_REF);
            }
        });

        findViewById(R.id.direccionLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", direccionLabel.getText().toString());
                intent.putExtra("keyboard", InputType.TYPE_CLASS_TEXT);
                startActivityForResult(intent, DIRECCION_FIELD_REF);
            }
        });

        findViewById(R.id.cadenciaLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ItemSelectorActivity.class);
                intent.putExtra("array", CommonFunctions.getCadenciasArray());
                startActivityForResult(intent, CADENCIA_FIELD_REF);
            }
        });

        observacionLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", observacionLabel.getText().toString());
                intent.putExtra("keyboard", InputType.TYPE_CLASS_TEXT);
                startActivityForResult(intent, OBSERVACIONES_FIELD_REF);
            }
        });

        clientImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFields();
            }
        });

        findViewById(R.id.phone_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+ cliente.getTelefono()));
                startActivity(callIntent);
            }
        });
    }

    private void setClientDetails() {
        nombreLabel.setText(cliente.getNombre());
        apellidosLabel.setText(cliente.getApellidos());
        fechaLabel.setText(DateFunctions.convertTimestampToBirthdayString(cliente.getFecha()));
        telefonoLabel.setText(cliente.getTelefono());
        emailLabel.setText(cliente.getEmail());
        direccionLabel.setText(cliente.getDireccion());
        cadenciaLabel.setText(cliente.getCadenciaVisita());
        observacionLabel.setText(cliente.getObservaciones().length() > 0 ? cliente.getObservaciones() : "Añade una observación");
        if (cliente.getImagen().length() == 0) {
            clientImage.setImageResource(R.drawable.user_placeholder);
        } else {
            clientImage.setImageBitmap(CommonFunctions.convertBase64StringToBitmap(cliente.getImagen()));
        }
    }

    private void addServiceHeader(String headerTitle) {
        ServiceHeaderTextView header = new ServiceHeaderTextView(this, headerTitle);
        scrollContentView.addView(header);
    }
    
    private void removeServicesFromScrollView() {
        scrollContentView.removeViewsInLayout(observacionesViewLayoutPosition, scrollContentView.getChildCount() - observacionesViewLayoutPosition);
    }

    private void addAlarmButton() {
        alarmButton = new ServiceAlarmButton(this);
        alarmImage = alarmButton.findViewById(R.id.image);
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cliente.getFechaNotificacionPersonalizada() == 0) {
                    showDatePickerDialog();
                } else{
                    cliente.setFechaNotificacionPersonalizada(0);
                    updateFechaNotificacionPersonalizada();
                }
            }
        });
        scrollContentView.addView(alarmButton);

        if (cliente.getFechaNotificacionPersonalizada() == 0) {
            CommonFunctions.unSelectLayout(getApplicationContext(), alarmButton, alarmImage);
        } else {
            CommonFunctions.selectLayout(getApplicationContext(), alarmButton, alarmImage);
        }
    }

    private void addAddServiceButton() {
        AddServiceButton addServiceButton = new AddServiceButton(this);
        addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cliente.getClientId() == 0) {
                    if (nombreLabel.getText().length() == 0 || apellidosLabel.getText().length() == 0) {
                        CommonFunctions.showGenericAlertMessage(ClientDetailActivity.this, "Debe incluir almenos el nombre y apellidos para crear un servicio");
                        return;
                    }

                    Intent intent = new Intent(ClientDetailActivity.this, ServiceDetailActivity.class);
                    intent.putExtra("cliente", cliente);
                    startActivityForResult(intent, SERVICE_DETAIL_REF);
                } else {
                    Intent intent = new Intent(ClientDetailActivity.this, ServiceDetailActivity.class);
                    intent.putExtra("cliente", cliente);
                    startActivity(intent);
                }
            }
        });

        scrollContentView.addView(addServiceButton);
    }

    private void checkFields() {
        if (cliente.getNombre().length() == 0) {
            CommonFunctions.showGenericAlertMessage(this, "Debe rellenar el campo nombre");
            return;
        }

        if (cliente.getApellidos().length() == 0) {
            CommonFunctions.showGenericAlertMessage(this, "Debe rellenar el campo apellidos");
            return;
        }

        if (cliente.getTelefono().length() < 9) {
            CommonFunctions.showGenericAlertMessage(this, "Debe incluir un numero de teléfono válido");
            return;
        }

        if (isClientDetail) {
            actualizarClienteEnServidor();
        } else {
            guardarClienteEnServidor();
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
                nombreLabel.setText(data.getExtras().getString("TEXTO"));
                cliente.setNombre(data.getExtras().getString("TEXTO"));
                break;
            case APELLIDOS_FIELD_REF:
                apellidosLabel.setText(data.getExtras().getString("TEXTO"));
                cliente.setApellidos(data.getExtras().getString("TEXTO"));
                break;
            case TELEFONO_FIELD_REF:
                telefonoLabel.setText(data.getExtras().getString("TEXTO"));
                cliente.setTelefono(data.getExtras().getString("TEXTO"));
                break;
            case EMAIL_FIELD_REF:
                emailLabel.setText(data.getExtras().getString("TEXTO"));
                cliente.setEmail(data.getExtras().getString("TEXTO"));
                break;
            case DIRECCION_FIELD_REF:
                direccionLabel.setText(data.getExtras().getString("TEXTO"));
                cliente.setDireccion(data.getExtras().getString("TEXTO"));
                break;
            case OBSERVACIONES_FIELD_REF:
                observacionLabel.setText(data.getExtras().getString("TEXTO"));
                cliente.setObservaciones(data.getExtras().getString("TEXTO"));
                break;
            case FECHA_FIELD_REF:
                fechaLabel.setText(DateFunctions.convertTimestampToBirthdayString(data.getLongExtra("timestamp", 0)));
                cliente.setFecha(data.getLongExtra("timestamp", 0));
                break;
            case CADENCIA_FIELD_REF:
                cadenciaLabel.setText(data.getStringExtra("ITEM"));
                cliente.setCadenciaVisita(data.getStringExtra("ITEM"));
                break;
            case REQUEST_IMAGE_CAPTURE:
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                clientImage.setImageBitmap(imageBitmap);
                cliente.setImagen(CommonFunctions.convertBitmapToBase64String(imageBitmap));
                break;
            case SERVICE_DETAIL_REF:
                ServiceModel servicio = (ServiceModel) data.getSerializableExtra("Servicio");
                serviceArray.add(servicio);
                break;
            default:
                break;
        }
    }

    private void guardarClienteEnServidor() {
        loadingState = CommonFunctions.createLoadingStateView(getApplicationContext());
        rootLayout.addView(loadingState);
        cliente.setComercioId(Constants.developmentComercioId);
        ClientesMasServiciosModel model = new ClientesMasServiciosModel(cliente, serviceArray);
        Call<ClientesMasServiciosModel> call = Constants.webServices.saveCliente(model);
        call.enqueue(new Callback<ClientesMasServiciosModel>() {
            @Override
            public void onResponse(Call<ClientesMasServiciosModel> call, Response<ClientesMasServiciosModel> response) {
                rootLayout.removeView(loadingState);
                if (response.code() == 200) {
                    Constants.databaseManager.clientsManager.addClientToDatabase(response.body().getCliente());
                    for (int i = 0; i < response.body().getServicios().size(); i++) {
                        ServiceModel servicio = response.body().getServicios().get(i);
                        Constants.databaseManager.servicesManager.addServiceToDatabase(servicio);
                    }

                    ClientDetailActivity.super.onBackPressed();
                } else {
                    CommonFunctions.showGenericAlertMessage(ClientDetailActivity.this, "Error guardando el cliente, inténtelo de nuevo");
                }
            }

            @Override
            public void onFailure(Call<ClientesMasServiciosModel> call, Throwable t) {
                rootLayout.removeView(loadingState);
                CommonFunctions.showGenericAlertMessage(ClientDetailActivity.this, "Error guardando el cliente, inténtelo de nuevo");
            }
        });
    }

    private void actualizarClienteEnServidor() {
        loadingState = CommonFunctions.createLoadingStateView(getApplicationContext());
        rootLayout.addView(loadingState);
        Call<ClientesMasServiciosModel> call = Constants.webServices.updateCliente(cliente);
        call.enqueue(new Callback<ClientesMasServiciosModel>() {
            @Override
            public void onResponse(Call<ClientesMasServiciosModel> call, Response<ClientesMasServiciosModel> response) {
                rootLayout.removeView(loadingState);
                if (response.code() == 200) {
                    Constants.databaseManager.clientsManager.updateClientInDatabase(cliente);
                    ClientDetailActivity.super.onBackPressed();
                } else {
                    CommonFunctions.showGenericAlertMessage(ClientDetailActivity.this, "Error actualizando cliente");
                }
            }

            @Override
            public void onFailure(Call<ClientesMasServiciosModel> call, Throwable t) {
                rootLayout.removeView(loadingState);
                CommonFunctions.showGenericAlertMessage(ClientDetailActivity.this, "Error actualizando cliente");
            }
        });
    }

    private void showDatePickerDialog(){
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);
                cliente.setFechaNotificacionPersonalizada(calendar.getTime().getTime());
                updateFechaNotificacionPersonalizada();
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void updateFechaNotificacionPersonalizada() {
        loadingState = CommonFunctions.createLoadingStateView(getApplicationContext());
        rootLayout.addView(loadingState);
        Call<ClientModel> call = Constants.webServices.updateNotificacionPersonalizada(cliente);
        call.enqueue(new Callback<ClientModel>() {
            @Override
            public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                rootLayout.removeView(loadingState);
                if (response.code() == 200) {
                    Constants.databaseManager.clientsManager.updateFechaNotificacionPersonalizada(cliente);
                    if (cliente.getFechaNotificacionPersonalizada() == 0) {
                        CommonFunctions.unSelectLayout(getApplicationContext(), alarmButton, alarmImage);
                        deleteNotificacionPersonalizada();
                    } else {
                        CommonFunctions.selectLayout(getApplicationContext(), alarmButton, alarmImage);
                    }
                } else {
                    CommonFunctions.showGenericAlertMessage(ClientDetailActivity.this, "Error actualizando el cliente");
                }
            }

            @Override
            public void onFailure(Call<ClientModel> call, Throwable t) {
                rootLayout.removeView(loadingState);
                CommonFunctions.showGenericAlertMessage(ClientDetailActivity.this, "Error actualizando el cliente");
            }
        });
    }

    private void deleteNotificacionPersonalizada() {
        ArrayList<NotificationModel> notificaciones = Constants.databaseManager.notificationsManager.getNotificationsForType(Constants.notificationpersonalizadaType);
        for (int i = 0; i < notificaciones.size(); i++) {
            if (notificaciones.get(i).getClientId() == cliente.getClientId()) {
                Constants.databaseManager.notificationsManager.deleteNotificationFromDatabase(notificaciones.get(i).getNotificationId());
            }
        }
    }
}

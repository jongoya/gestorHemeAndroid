package com.example.gestorheme.Activities.ClientDetail;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.gestorheme.Activities.Cesta.CestaActivity;
import com.example.gestorheme.Activities.ClientDetail.Fragment.DatePickerFragment;
import com.example.gestorheme.Activities.ClientDetail.Views.ServiceView;
import com.example.gestorheme.Activities.DatePicker.DatePickerActivity;
import com.example.gestorheme.Activities.ItemSelector.ItemSelectorActivity;
import com.example.gestorheme.Activities.ServiceDetail.ServiceDetailActivity;
import com.example.gestorheme.Activities.TextInputField.TextInputFieldActivity;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Common.Preferencias;
import com.example.gestorheme.Common.SyncronizationManager;
import com.example.gestorheme.Common.Views.AddServiceButton;
import com.example.gestorheme.Common.Views.ServiceAlarmButton;
import com.example.gestorheme.Common.Views.ServiceHeaderTextView;
import com.example.gestorheme.Models.Cesta.CestaModel;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Notification.NotificationModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.Models.Venta.VentaModel;
import com.example.gestorheme.Models.WebServicesModels.ClientesMasServiciosModel;
import com.example.gestorheme.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Calendar;
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
    private TextView nombreField;
    private TextView apellidosField;
    private TextView fechaField;
    private TextView telefonoField;
    private TextView emailField;
    private TextView direccionField;
    private TextView cadenciaField;
    private ImageView nombreArrow;
    private ImageView apellidosArrow;
    private ImageView fechaArrow;
    private ImageView telefonoArrow;
    private ImageView emailArrow;
    private ImageView direccionArrow;
    private ImageView cadenciaArrow;
    private RelativeLayout nombreDivider;
    private RelativeLayout apellidosDivider;
    private RelativeLayout fechaDivider;
    private RelativeLayout telefonoDivider;
    private RelativeLayout emailDivider;
    private RelativeLayout direccionDivider;
    private RoundedImageView clientImage;
    private LinearLayout scrollContentView;
    private RelativeLayout phoneButton;
    private ConstraintLayout rootLayout;
    private RelativeLayout loadingState;
    private ServiceAlarmButton alarmButton;
    private ImageView alarmImage;
    private SwipeRefreshLayout refreshLayout;
    private RelativeLayout saveButton;
    private ImageView saveImage;
    private ImageView phoneImage;

    private ClientModel cliente;
    public boolean isClientDetail = false;
    private int observacionesViewLayoutPosition = 9;
    private ArrayList<ServiceModel> serviceArray = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.client_detail_layout);
        AppStyle.setStatusBarColor(this);
        getClientIntent();
        getFields();

        customizeLabels();
        customizeFields();
        customizeDividers();
        customizeArrows();
        customizeBackground();
        customizeClientImage();
        customizeButtons();
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
        setRefreshLayoutListener();

        if (isClientDetail) {
            refreshLayout.setEnabled(true);
        } else {
            refreshLayout.setEnabled(false);
        }
    }

    private void customizeLabels() {
        nombreLabel.setTextColor(AppStyle.getSecondaryTextColor());
        apellidosLabel.setTextColor(AppStyle.getSecondaryTextColor());
        fechaLabel.setTextColor(AppStyle.getSecondaryTextColor());
        telefonoLabel.setTextColor(AppStyle.getSecondaryTextColor());
        emailLabel.setTextColor(AppStyle.getSecondaryTextColor());
        direccionLabel.setTextColor(AppStyle.getSecondaryTextColor());
        cadenciaLabel.setTextColor(AppStyle.getSecondaryTextColor());
    }

    private void customizeFields() {
        nombreField.setTextColor(AppStyle.getPrimaryTextColor());
        apellidosField.setTextColor(AppStyle.getPrimaryTextColor());
        fechaField.setTextColor(AppStyle.getPrimaryTextColor());
        telefonoField.setTextColor(AppStyle.getPrimaryTextColor());
        emailField.setTextColor(AppStyle.getPrimaryTextColor());
        direccionField.setTextColor(AppStyle.getPrimaryTextColor());
        cadenciaField.setTextColor(AppStyle.getPrimaryTextColor());
        observacionLabel.setTextColor(AppStyle.getPrimaryTextColor());
    }

    private void customizeArrows() {
        nombreArrow.setColorFilter(AppStyle.getSecondaryTextColor());
        apellidosArrow.setColorFilter(AppStyle.getSecondaryTextColor());
        fechaArrow.setColorFilter(AppStyle.getSecondaryTextColor());
        telefonoArrow.setColorFilter(AppStyle.getSecondaryTextColor());
        emailArrow.setColorFilter(AppStyle.getSecondaryTextColor());
        direccionArrow.setColorFilter(AppStyle.getSecondaryTextColor());
        cadenciaArrow.setColorFilter(AppStyle.getSecondaryTextColor());
    }

    private void customizeDividers() {
        nombreDivider.setBackgroundColor(AppStyle.getSecondaryColor());
        apellidosDivider.setBackgroundColor(AppStyle.getSecondaryColor());
        fechaDivider.setBackgroundColor(AppStyle.getSecondaryColor());
        telefonoDivider.setBackgroundColor(AppStyle.getSecondaryColor());
        emailDivider.setBackgroundColor(AppStyle.getSecondaryColor());
        direccionDivider.setBackgroundColor(AppStyle.getSecondaryColor());
    }

    private void customizeBackground() {
        scrollContentView.setBackgroundColor(AppStyle.getBackgroundColor());
    }

    private void customizeClientImage() {
        clientImage.setColorFilter(AppStyle.getPrimaryTextColor());
    }

    private void customizeButtons() {
        CommonFunctions.customizeViewWithImage(getApplicationContext(), saveButton, saveImage, AppStyle.getPrimaryColor(), AppStyle.getPrimaryColor());
        CommonFunctions.customizeViewWithImage(getApplicationContext(), phoneButton, phoneImage, AppStyle.getPrimaryColor(), AppStyle.getPrimaryColor());
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
            ServiceView serviceView = new ServiceView(this, servicio, null, servicio.getClientId() != 0 ? "" : cliente.getNombre(), servicio.getClientId() != 0 ? "" : cliente.getApellidos());
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

            if (serviceView.service.getFecha() > Calendar.getInstance().getTimeInMillis() / 1000) {
                futurosServicios.add(serviceView);
            } else {
                antiguosServicios.add(serviceView);
            }
        }

        if (futurosServicios.size() > 0) {
            addServiceHeader("PRÓXIMOS SERVICIOS");
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
        
        if (isClientDetail) {
            ArrayList<CestaModel> cestas = Constants.databaseManager.cestaManager.getCestasForClientId(cliente.getClientId());
            if (cestas.size() > 0) {
                addServiceHeader("VENTA PRODUCTOS");
                for (int i = 0; i < cestas.size(); i++) {
                    ServiceView serviceView = new ServiceView(this, null, cestas.get(i), "", "");
                    int finalI = i;
                    serviceView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ArrayList<VentaModel> ventas = Constants.databaseManager.ventaManager.getVentas(cestas.get(finalI).getCestaId());
                            Intent intent = new Intent(ClientDetailActivity.this, CestaActivity.class);
                            intent.putExtra("ventas", ventas);
                            intent.putExtra("cesta", cestas.get(finalI));
                            ClientDetailActivity.this.startActivity(intent);
                        }
                    });
                    scrollContentView.addView(serviceView);
                }
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
        refreshLayout = findViewById(R.id.refreshLayout);
        nombreField = findViewById(R.id.nombreField);
        apellidosField = findViewById(R.id.apellidosField);
        fechaField = findViewById(R.id.fechaField);
        telefonoField = findViewById(R.id.telefonoField);
        emailField = findViewById(R.id.emailField);
        direccionField = findViewById(R.id.direccionField);
        cadenciaField = findViewById(R.id.cadenciaField);
        nombreArrow = findViewById(R.id.nombreArrow);
        apellidosArrow = findViewById(R.id.apellidosArrow);
        fechaArrow = findViewById(R.id.fechaArrow);
        telefonoArrow = findViewById(R.id.telefonoArrow);
        emailArrow = findViewById(R.id.emailArrow);
        direccionArrow = findViewById(R.id.direccionArrow);
        cadenciaArrow = findViewById(R.id.cadenciaArrow);
        nombreDivider = findViewById(R.id.nombreDivider);
        apellidosDivider = findViewById(R.id.apellidosDivider);
        fechaDivider = findViewById(R.id.fechaDivider);
        telefonoDivider = findViewById(R.id.telefonoDivider);
        emailDivider = findViewById(R.id.emailDivider);
        direccionDivider = findViewById(R.id.direccionDivider);
        saveButton = findViewById(R.id.save_button);
        saveImage = findViewById(R.id.saveImage);
        phoneImage = findViewById(R.id.phoneImage);

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
                intent.putExtra("contenido", cliente.getObservaciones());
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFields();
            }
        });

        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:" + cliente.getTelefono()));
                if (ActivityCompat.checkSelfPermission(ClientDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    CommonFunctions.showGenericAlertMessage(ClientDetailActivity.this, "No dispone de permisos para realizar llamadas");
                    return;
                }

                startActivity(phoneIntent);
            }
        });
    }

    private void setRefreshLayoutListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getServicesForClientId();
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
            clientImage.setImageResource(R.drawable.add_image);
            clientImage.setCornerRadius(0);
            clientImage.setColorFilter(AppStyle.getPrimaryTextColor());
        } else {
            clientImage.setImageBitmap(CommonFunctions.convertBase64StringToBitmap(cliente.getImagen()));
            clientImage.setCornerRadius(CommonFunctions.convertToPx(75, getApplicationContext()));
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
            CommonFunctions.customizeViewWithImage(getApplicationContext(), alarmButton, alarmImage, AppStyle.getSecondaryColor(), AppStyle.getSecondaryColor());
        } else {
            CommonFunctions.customizeViewWithImage(getApplicationContext(), alarmButton, alarmImage, AppStyle.getPrimaryColor(), AppStyle.getPrimaryColor());
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
                clientImage.setCornerRadius(CommonFunctions.convertToPx(75, getApplicationContext()));
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
        loadingState = CommonFunctions.createLoadingStateView(getApplicationContext(), "Guardando cliente");
        rootLayout.addView(loadingState);
        cliente.setComercioId(Preferencias.getComercioIdFromSharedPreferences(getApplicationContext()));
        ClientesMasServiciosModel model = new ClientesMasServiciosModel(cliente, serviceArray, Preferencias.getComercioIdFromSharedPreferences(getApplicationContext()));
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
                }  else if (response.code() == Constants.logoutResponseValue) {
                    CommonFunctions.logout(ClientDetailActivity.this);
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
        loadingState = CommonFunctions.createLoadingStateView(getApplicationContext(), "Actualizando cliente");
        rootLayout.addView(loadingState);
        Call<ClientesMasServiciosModel> call = Constants.webServices.updateCliente(cliente);
        call.enqueue(new Callback<ClientesMasServiciosModel>() {
            @Override
            public void onResponse(Call<ClientesMasServiciosModel> call, Response<ClientesMasServiciosModel> response) {
                rootLayout.removeView(loadingState);
                if (response.code() == 200) {
                    Constants.databaseManager.clientsManager.updateClientInDatabase(cliente);
                    ClientDetailActivity.super.onBackPressed();
                }  else if (response.code() == Constants.logoutResponseValue) {
                    CommonFunctions.logout(ClientDetailActivity.this);
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
                cliente.setFechaNotificacionPersonalizada(calendar.getTimeInMillis() / 1000);
                updateFechaNotificacionPersonalizada();
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void updateFechaNotificacionPersonalizada() {
        loadingState = CommonFunctions.createLoadingStateView(getApplicationContext(), "Actualizando cliente");
        rootLayout.addView(loadingState);
        Call<ClientModel> call = Constants.webServices.updateNotificacionPersonalizada(cliente);
        call.enqueue(new Callback<ClientModel>() {
            @Override
            public void onResponse(Call<ClientModel> call, Response<ClientModel> response) {
                rootLayout.removeView(loadingState);
                if (response.code() == 200) {
                    Constants.databaseManager.clientsManager.updateFechaNotificacionPersonalizada(cliente);
                    if (cliente.getFechaNotificacionPersonalizada() == 0) {
                        CommonFunctions.customizeViewWithImage(getApplicationContext(), alarmButton, alarmImage, AppStyle.getSecondaryColor(), AppStyle.getSecondaryColor());
                        deleteNotificacionPersonalizada();
                    } else {
                        CommonFunctions.customizeViewWithImage(getApplicationContext(), alarmButton, alarmImage, AppStyle.getPrimaryColor(), AppStyle.getPrimaryColor());
                    }
                }  else if (response.code() == Constants.logoutResponseValue) {
                    CommonFunctions.logout(ClientDetailActivity.this);
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

    private void getServicesForClientId() {
        Call <ArrayList<ServiceModel>> call = Constants.webServices.getServiciosForClientId(Preferencias.getComercioIdFromSharedPreferences(getApplicationContext()), cliente.getClientId());
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

                        SyncronizationManager.deleteLocalServicesIfNeeded(response.body(), Constants.databaseManager.servicesManager.getServicesForClient(cliente.getClientId()));

                        refreshLayout.setRefreshing(false);
                        onResume();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ServiceModel>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
            }
        });
    }
}

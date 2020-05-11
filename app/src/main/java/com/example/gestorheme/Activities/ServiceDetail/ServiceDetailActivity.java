package com.example.gestorheme.Activities.ServiceDetail;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gestorheme.Activities.ClientListSelector.ClientListSelectorActivity;
import com.example.gestorheme.Activities.DatePicker.DatePickerActivity;
import com.example.gestorheme.Activities.ItemSelector.ItemSelectorActivity;
import com.example.gestorheme.Activities.Main.Fragments.ListaClientes.Models.ListaClienteCellModel;
import com.example.gestorheme.Activities.TextInputField.TextInputFieldActivity;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.Models.TipoServicio.TipoServicioModel;
import com.example.gestorheme.R;
import java.util.ArrayList;

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

    private ServiceModel servicio;
    private ClientModel cliente;
    private boolean isEditingService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_detail_activity);
        getFields();
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
        if (servicio != null) {
            isEditingService = true;
            setServiceFields();
        } else {
            servicio = new ServiceModel();
            servicio.setNombre(cliente.getNombre());
            servicio.setApellidos(cliente.getApellidos());
            nombreLabel.setText(cliente.getNombre() + " " + cliente.getApellidos());
        }

        //TODO implementar el flujo en añadir servicio en la agenda
    }

    private void getFields() {
        nombreLabel = findViewById(R.id.nombreLabel);
        fechaLabel = findViewById(R.id.fechaLabel);
        profesionalLabel = findViewById(R.id.profesionalLabel);
        serviciosLabel = findViewById(R.id.serviciosLabel);
        precioLabel = findViewById(R.id.precioLabel);
        observacionesLabel = findViewById(R.id.observacionesLabel);
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
                intent.putExtra("contenido", String.valueOf(servicio.getPrecio()));
                intent.putExtra("keyboard", InputType.TYPE_NUMBER_FLAG_DECIMAL);
                startActivityForResult(intent, PRECIO_FIELD_REF);
            }
        });

        findViewById(R.id.observacionesLabel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", observacionesLabel.getText().toString());
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
    }

    private void setServiceFields() {
        nombreLabel.setText(servicio.getNombre() + " " + servicio.getApellidos());
        fechaLabel.setText(DateFunctions.convertTimestampToServiceDateString(servicio.getFecha()));
        profesionalLabel.setText(Constants.databaseManager.empleadosManager.getEmpleadoForEmpleadoId(servicio.getProfesional()).getNombre());
        serviciosLabel.setText(CommonFunctions.getServiciosForServicio(servicio.getServicios()));
        precioLabel.setText(Double.toString(servicio.getPrecio()) + " €");
        observacionesLabel.setText(servicio.getObservaciones().length() > 0 ? servicio.getObservaciones() : "Añade una observación");
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
            serviciosId.add(servicios.get(i).getServiceId());
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
            Constants.databaseManager.servicesManager.updateServiceInDatabase(servicio);
        } else {
            if (cliente.getClientId() == 0) {
                Intent intent = getIntent();
                intent.putExtra("Servicio", servicio);
                setResult(RESULT_OK, intent);
            } else {
                servicio.setClientId(cliente.getClientId());
                Constants.databaseManager.servicesManager.addServiceToDatabase(servicio);
                //TODO revisar notificaciones cadencia del cliente
            }
        }

        ServiceDetailActivity.super.onBackPressed();
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
                servicio.setNombre(model.getCliente().getNombre());
                servicio.setApellidos(model.getCliente().getApellidos());
                servicio.setClientId(model.getCliente().getClientId());
                servicio.setNombre(data.getExtras().getString("TEXTO"));
                break;
            case PROFESIONAL_FIELD_REF:
                servicio.setProfesional(data.getExtras().getLong("ITEM"));
                profesionalLabel.setText(Constants.databaseManager.empleadosManager.getEmpleadoForEmpleadoId(data.getExtras().getLong("ITEM")).getNombre());
                break;
            case SERVICIOS_FIELD_REF:
                serviciosLabel.setText(getServiciosString((ArrayList<TipoServicioModel>) data.getSerializableExtra("ITEM")));
                servicio.setServicios(getServiciosIdFromServicios((ArrayList<TipoServicioModel>) data.getSerializableExtra("ITEM")));
                break;
            case PRECIO_FIELD_REF:
                servicio.setPrecio(Double.valueOf(data.getStringExtra("TEXTO")));
                precioLabel.setText(data.getStringExtra("TEXTO") + " €");
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
}

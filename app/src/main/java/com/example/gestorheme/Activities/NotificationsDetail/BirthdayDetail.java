package com.example.gestorheme.Activities.NotificationsDetail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Interfaces.ClientSelectorActionSheetInterface;
import com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Views.ClientSelectorActionSheet;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Notification.NotificationModel;
import com.example.gestorheme.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BirthdayDetail extends AppCompatActivity implements ClientSelectorActionSheetInterface {

    private ArrayList<NotificationModel> notificaciones;
    private BottomSheetDialog filterDialog;
    private TextView nombresField;
    private TextView titulo;
    private ImageView backgroundImage;
    private ConstraintLayout rootLayout;
    private RelativeLayout loadingStateView;
    private RelativeLayout callButton;
    private RelativeLayout wassapButton;
    private RelativeLayout messageButton;
    private ImageView callImage;
    private ImageView wassapImage;
    private ImageView messageImage;
    private int comunicationOptionSelected = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birthday_layout);
        getBirthdayIntent();
        getFields();
        setOnClickListeners();
        setNotificationContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!notificaciones.get(0).isLeido()) {
            if (notificaciones.size() == 1) {
                markNotificationAsRead(notificaciones.get(0));
            } else {
                markNotificationsAsRead(notificaciones);
            }
        }
    }

    private void getBirthdayIntent() {
        this.notificaciones = (ArrayList<NotificationModel>) getIntent().getSerializableExtra("notificaciones");
    }

    private void getFields() {
        nombresField = findViewById(R.id.nombres);
        backgroundImage = findViewById(R.id.imagen);
        titulo = findViewById(R.id.titulo);
        rootLayout = findViewById(R.id.root);
        callButton = findViewById(R.id.call);
        wassapButton = findViewById(R.id.wassap);
        messageButton = findViewById(R.id.message);
        callImage = findViewById(R.id.callImage);
        wassapImage = findViewById(R.id.wassapImage);
        messageImage = findViewById(R.id.messageImage);
    }

    private void setNotificationContent() {
        String notificationType = notificaciones.get(0).getType();
        if (notificationType.equals(Constants.notificationCumpleañosType)) {
            backgroundImage.setImageResource(R.drawable.confetti);
            titulo.setText("¡Cumpleaños!");
            setNames();
        } else if (notificationType.equals(Constants.notificationpersonalizadaType)) {
            backgroundImage.setImageResource(R.drawable.personalizada);
            titulo.setText("¡Notificación personalizada!");
            nombresField.setText(notificaciones.get(0).getDescripcion());
        }

        CommonFunctions.customizeViewWithImage(getApplicationContext(), callButton, callImage, R.color.dividerColor, R.color.dividerColor);
        CommonFunctions.customizeViewWithImage(getApplicationContext(), wassapButton, wassapImage, R.color.dividerColor, R.color.dividerColor);
        CommonFunctions.customizeViewWithImage(getApplicationContext(), messageButton, messageImage, R.color.dividerColor, R.color.dividerColor);
    }

    private void setNames() {
        String nombres = "";
        for (int i = 0; i < notificaciones.size(); i++) {
            NotificationModel notification = notificaciones.get(i);
            ClientModel cliente = Constants.databaseManager.clientsManager.getClientForClientId(notification.getClientId());
            nombres += cliente.getNombre() + " " + cliente.getApellidos() + ", ";
        }

        nombresField.setText(nombres);
    }

    private void setOnClickListeners() {
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notificaciones.size() == 1) {
                    makeCall(Constants.databaseManager.clientsManager.getClientForClientId(notificaciones.get(0).getClientId()));
                } else {
                    comunicationOptionSelected = 1;
                    showFilterDialog();
                }
            }
        });

        wassapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notificaciones.size() == 1) {
                    openWassap(Constants.databaseManager.clientsManager.getClientForClientId(notificaciones.get(0).getClientId()));
                } else {
                    comunicationOptionSelected = 2;
                    showFilterDialog();
                }
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notificaciones.size() == 1) {
                    sendMessage(Constants.databaseManager.clientsManager.getClientForClientId(notificaciones.get(0).getClientId()));
                } else {
                    comunicationOptionSelected = 3;
                    showFilterDialog();
                }
            }
        });
    }

    private void showFilterDialog() {
        try {
            LinearLayout filterActionView = new ClientSelectorActionSheet(getApplicationContext(), getClientesFormNotifications(notificaciones), this);
            filterDialog = new BottomSheetDialog(this);
            filterDialog.setContentView(filterActionView);
            filterDialog.show();
            FrameLayout bottomSheet = filterDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            bottomSheet.setBackground(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<ClientModel> getClientesFormNotifications(ArrayList<NotificationModel> notifications) {
        ArrayList<ClientModel> clientes = new ArrayList<>();
        for (int i = 0; i < notifications.size(); i++) {
            ClientModel cliente = Constants.databaseManager.clientsManager.getClientForClientId(notifications.get(i).getClientId());
            clientes.add(cliente);
        }

        return clientes;
    }

    private void makeCall(ClientModel cliente) {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + cliente.getTelefono()));
        if (ActivityCompat.checkSelfPermission(BirthdayDetail.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            CommonFunctions.showGenericAlertMessage(BirthdayDetail.this, "No dispone de permisos para realizar llamadas");
            return;
        }

        startActivity(phoneIntent);
    }

    private void openWassap(ClientModel cliente) {
        String url = "https://api.whatsapp.com/send?phone="+ cliente.getTelefono();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void sendMessage(ClientModel cliente) {
        if (ActivityCompat.checkSelfPermission(BirthdayDetail.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            CommonFunctions.showGenericAlertMessage(BirthdayDetail.this, "No dispone de permisos para enviar mensajes");
            return;
        }

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(cliente.getTelefono(), null, "Enhorabuena", null, null);
    }

    @Override
    public void clienteSelcted(ClientModel cliente) {
        filterDialog.dismiss();
        switch (comunicationOptionSelected) {
            case 1:
                makeCall(cliente);
                break;
            case 2:
                openWassap(cliente);
                break;
            default:
                sendMessage(cliente);
                break;
        }
    }

    @Override
    public void cancelarClicked() {
        filterDialog.dismiss();
    }

    private void markNotificationsAsRead(ArrayList<NotificationModel> notifications) {
        for (int i = 0; i < notifications.size(); i++) {
            notifications.get(i).setLeido(true);
        }
        loadingStateView = CommonFunctions.createLoadingStateView(getApplicationContext(), "Actualizando notificación");
        rootLayout.addView(loadingStateView);
        Call<ArrayList<NotificationModel>> call = Constants.webServices.updateNotificaciones(notifications);
        call.enqueue(new Callback<ArrayList<NotificationModel>>() {
            @Override
            public void onResponse(Call<ArrayList<NotificationModel>> call, Response<ArrayList<NotificationModel>> response) {
                rootLayout.removeView(loadingStateView);
                if (response.code() == 200) {
                    for (int i = 0; i < notifications.size(); i++) {
                        Constants.databaseManager.notificationsManager.updateNotificationInDatabase(response.body().get(i));
                    }
                }  else if (response.code() == Constants.logoutResponseValue) {
                    CommonFunctions.logout(BirthdayDetail.this);
                } else {
                    CommonFunctions.showGenericAlertMessage(BirthdayDetail.this, "Error actualizando la notificación");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationModel>> call, Throwable t) {
                rootLayout.removeView(loadingStateView);
                CommonFunctions.showGenericAlertMessage(BirthdayDetail.this, "Error actualizando la notificación");
            }
        });
    }

    private void markNotificationAsRead(NotificationModel notificacion) {
        notificacion.setLeido(true);
        loadingStateView = CommonFunctions.createLoadingStateView(getApplicationContext(), "Actualizando notificación");
        rootLayout.addView(loadingStateView);
        Call<NotificationModel> call = Constants.webServices.updateNotificacion(notificacion);
        call.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                rootLayout.removeView(loadingStateView);
                if (response.code() == 200) {
                    Constants.databaseManager.notificationsManager.updateNotificationInDatabase(response.body());
                }  else if (response.code() == Constants.logoutResponseValue) {
                    CommonFunctions.logout(BirthdayDetail.this);
                } else {
                    CommonFunctions.showGenericAlertMessage(BirthdayDetail.this, "Error actualizando la notificación");
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                rootLayout.removeView(loadingStateView);
                CommonFunctions.showGenericAlertMessage(BirthdayDetail.this, "Error actualizando la notificación");
            }
        });
    }
}

package com.example.gestorheme.Activities.NotificationsDetail;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Adapter.CadenciaDetailListAdapter;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Notification.NotificationModel;
import com.example.gestorheme.R;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadenciaDetail extends AppCompatActivity {
    private TextView descripcionField;
    private ListView clientList;
    private RelativeLayout rootLayout;
    private RelativeLayout loadingState;
    private CadenciaDetailListAdapter adapter;

    private ArrayList<NotificationModel> notificaciones;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadencia_notification_layout);
        getCadenciaIntent();
        getFields();
        setFields();
        setClientList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!notificaciones.get(0).isLeido()) {
            markNotificationsAsRead(notificaciones);
        }
    }

    private void getCadenciaIntent(){
        notificaciones = (ArrayList<NotificationModel>)getIntent().getSerializableExtra("notificaciones");
    }

    private void getFields() {
        descripcionField = findViewById(R.id.descripcion);
        clientList = findViewById(R.id.clientList);
        rootLayout = findViewById(R.id.root);
    }

    private void setFields() {
        if (notificaciones.size() == 1) {
            descripcionField.setText("Hay " + notificaciones.size() + " cliente que lleva tiempo sin venir");
        } else {
            descripcionField.setText("Hay " + notificaciones.size() + " clientes que llevan tiempo sin venir");
        }
    }

    private void setClientList() {
        adapter = new CadenciaDetailListAdapter(getApplicationContext(), getClientesFromNotificaciones());
        clientList.setAdapter(adapter);
        clientList.setDivider(null);
    }

    private ArrayList<ClientModel> getClientesFromNotificaciones() {
        ArrayList<ClientModel> clientes = new ArrayList<>();
        for (int i = 0; i < notificaciones.size(); i++) {
            ClientModel cliente = Constants.databaseManager.clientsManager.getClientForClientId(notificaciones.get(i).getClientId());
            clientes.add(cliente);
        }

        return clientes;
    }

    private void markNotificationsAsRead(ArrayList<NotificationModel> notifications) {
        for (int i = 0; i < notifications.size(); i++) {
            notifications.get(i).setLeido(true);
        }

        loadingState = CommonFunctions.createLoadingStateView(getApplicationContext());
        rootLayout.addView(loadingState);
        Call<ArrayList<NotificationModel>> call = Constants.webServices.updateNotificaciones(notifications);
        call.enqueue(new Callback<ArrayList<NotificationModel>>() {
            @Override
            public void onResponse(Call<ArrayList<NotificationModel>> call, Response<ArrayList<NotificationModel>> response) {
                rootLayout.removeView(loadingState);
                if (response.code() == 200) {
                    for (int i = 0; i < notifications.size(); i++) {
                        Constants.databaseManager.notificationsManager.updateNotificationInDatabase(response.body().get(i));
                    }
                } else {
                    CommonFunctions.showGenericAlertMessage(CadenciaDetail.this, "Error actualizando la notificación");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationModel>> call, Throwable t) {
                rootLayout.removeView(loadingState);
                CommonFunctions.showGenericAlertMessage(CadenciaDetail.this, "Error actualizando la notificación");
            }
        });
    }
}

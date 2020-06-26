package com.example.gestorheme.Activities.Main.Fragments.Agenda.Views;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gestorheme.Activities.ClientDetail.ClientDetailActivity;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.R;

import java.util.ArrayList;
import java.util.Date;

public class ClientItemView extends RelativeLayout {
    private TextView nombreLabel;
    private TextView horariosLabel;
    private RelativeLayout background;
    private ImageView imagen;
    private RelativeLayout rootLayout;

    public ClientItemView(Context context, ClientModel cliente, Date presentDate) {
        super(context);
        View.inflate(context, R.layout.agenda_client_item_view, this);
        getFields();
        customizeFields();
        setOnClickListeners(context, cliente);
        setFields(cliente, presentDate);
    }

    private void getFields() {
        nombreLabel = findViewById(R.id.nombreLabel);
        horariosLabel = findViewById(R.id.horariosLabel);
        background = findViewById(R.id.background);
        imagen = findViewById(R.id.image);
        rootLayout = findViewById(R.id.root);
    }

    private void customizeFields() {
        nombreLabel.setTextColor(AppStyle.getPrimaryTextColor());
        horariosLabel.setTextColor(AppStyle.getSecondaryTextColor());
        imagen.setColorFilter(AppStyle.getPrimaryTextColor());
        CommonFunctions.customizeView(getContext(), background, AppStyle.getSecondaryColor());
        rootLayout.setBackgroundColor(AppStyle.getBackgroundColor());
    }
    private void setFields(ClientModel cliente, Date presentDate) {
        ArrayList<ServiceModel> servicios = Constants.databaseManager.servicesManager.getServicesForDate(presentDate);
        nombreLabel.setText(cliente.getNombre() + " " + cliente.getApellidos());
        horariosLabel.setText("Horarios: " +  getHorariosString(servicios, cliente.getClientId()));
    }

    private void setOnClickListeners(final Context context, final ClientModel cliente) {
        findViewById(R.id.background).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ClientDetailActivity.class);
                intent.putExtra("Cliente", cliente);
                context.startActivity(intent);
            }
        });
    }

    private String getHorariosString(ArrayList<ServiceModel> servicios, long clientId) {
        ArrayList<String> serviciosStrings = new ArrayList<>();
        for (int i = 0; i < servicios.size(); i++) {
            if (servicios.get(i).getClientId() == clientId && !serviciosStrings.contains(DateFunctions.getHoursAndMinutesFromDate(servicios.get(i).getFecha()))) {
                serviciosStrings.add(DateFunctions.getHoursAndMinutesFromDate(servicios.get(i).getFecha()));
            }
        }
        return String.join(",", serviciosStrings);
    }
}

package com.example.gestorheme.Activities.Main.Fragments.Agenda.Views;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gestorheme.Activities.Main.Fragments.Agenda.Interfaces.ServiceItemViewInterface;
import com.example.gestorheme.Activities.ServiceDetail.ServiceDetailActivity;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.R;
import java.util.ArrayList;
import java.util.Date;

public class AgendaItemView extends RelativeLayout {
    private TextView hora;
    private LinearLayout servicesContent;

    public AgendaItemView(Context context, Date date, ArrayList<ServiceModel> servicios, ServiceItemViewInterface delegate) {
        super(context);
        View.inflate(context, R.layout.agenda_item_view, this);
        getFields();
        setOnClickListeners(context, date);
        setFields(date);

        if (servicios.size() > 0) {
            buildServiceViews(servicios, context, delegate);
        }
    }

    private void getFields() {
        hora = findViewById(R.id.time);
        servicesContent = findViewById(R.id.services_content);
    }

    private void setOnClickListeners(final Context context, final Date date) {
        findViewById(R.id.timeContent).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ServiceDetailActivity.class);
                intent.putExtra("fecha", date.getTime());
                context.startActivity(intent);
            }
        });
    }

    private void setFields(Date date) {
        hora.setText(DateFunctions.getHoursAndMinutesFromDate(date.getTime()));
    }

    private void buildServiceViews(ArrayList<ServiceModel> servicios, Context contexto, ServiceItemViewInterface delegate) {
        for (int i = 0; i < servicios.size(); i++) {
            ServiceItemView serviceView = new ServiceItemView(contexto, servicios.get(i), delegate);
            serviceView.setClipChildren(false);
            servicesContent.addView(serviceView);
        }
    }
}

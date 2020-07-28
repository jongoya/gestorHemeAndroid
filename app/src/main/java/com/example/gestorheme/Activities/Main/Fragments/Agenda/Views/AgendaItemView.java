package com.example.gestorheme.Activities.Main.Fragments.Agenda.Views;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.Interfaces.ServiceItemViewInterface;
import com.example.gestorheme.Activities.ServiceDetail.ServiceDetailActivity;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Models.Cesta.CestaModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AgendaItemView extends RelativeLayout {
    private TextView hora;
    private LinearLayout servicesContent;
    private RelativeLayout divider;

    public AgendaItemView(Context context, Date date, ArrayList<ServiceModel> servicios, ArrayList<CestaModel> cestas, ServiceItemViewInterface delegate) {
        super(context);
        View.inflate(context, R.layout.agenda_item_view, this);
        getFields();
        customizeDivider();
        setOnClickListeners(context, date);
        setFields(date);

        if (servicios.size() + cestas.size() > 0) {
            buildServiceViews(servicios, cestas, context, delegate);
        }
    }

    private void getFields() {
        hora = findViewById(R.id.time);
        servicesContent = findViewById(R.id.services_content);
        divider = findViewById(R.id.divider2);
    }

    private void setOnClickListeners(final Context context, final Date date) {
        findViewById(R.id.timeContent).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ServiceDetailActivity.class);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                intent.putExtra("fecha", calendar.getTimeInMillis() / 1000);
                context.startActivity(intent);
            }
        });
    }

    private void setFields(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        hora.setTextColor(AppStyle.getPrimaryTextColor());
        hora.setText(DateFunctions.getHoursAndMinutesFromDate(calendar.getTimeInMillis() / 1000));
    }

    private void customizeDivider() {
        divider.setBackgroundColor(AppStyle.getSecondaryColor());
    }

    private void buildServiceViews(ArrayList<ServiceModel> servicios, ArrayList<CestaModel> cestas, Context contexto, ServiceItemViewInterface delegate) {
        for (int i = 0; i < servicios.size(); i++) {
            ServiceItemView serviceView = new ServiceItemView(contexto, servicios.get(i), null, delegate);
            serviceView.setClipChildren(false);
            servicesContent.addView(serviceView);
        }

        for (int i = 0; i < cestas.size(); i++) {
            ServiceItemView serviceView = new ServiceItemView(contexto, null, cestas.get(i), delegate);
            serviceView.setClipChildren(false);
            servicesContent.addView(serviceView);
        }
    }
}

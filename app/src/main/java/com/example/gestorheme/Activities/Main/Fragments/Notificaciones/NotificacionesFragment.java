package com.example.gestorheme.Activities.Main.Fragments.Notificaciones;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gestorheme.Activities.CierreCaja.CierreCajaActivity;
import com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Adapter.NotificationListAdapter;
import com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Models.NotificationDayModel;
import com.example.gestorheme.Activities.NotificationsDetail.BirthdayDetail;
import com.example.gestorheme.Activities.NotificationsDetail.CadenciaDetail;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Models.Notification.NotificationModel;
import com.example.gestorheme.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class NotificacionesFragment extends Fragment {
    private RelativeLayout cumpleañosButton;
    private RelativeLayout cadenciaButton;
    private RelativeLayout cajaButton;
    private RelativeLayout personalizadaButton;
    private TextView cumpleText;
    private TextView cadenciaText;
    private TextView cajaText;
    private TextView personalizadaText;
    private ListView notificationsList;
    private TextView emptyState;
    private NotificationListAdapter adapter;
    private int tabSelected = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notificaciones_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFields();
        setButtonsWidth();
        setOnClickListeners();
        initializeList();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    private void getFields() {
        cumpleañosButton = getView().findViewById(R.id.cumpleButton);
        cadenciaButton = getView().findViewById(R.id.cadenciaButton);
        cajaButton = getView().findViewById(R.id.cajaButton);
        personalizadaButton = getView().findViewById(R.id.personalizadaButton);
        cumpleText = getView().findViewById(R.id.cumpleText);
        cadenciaText = getView().findViewById(R.id.cadenciaText);
        cajaText = getView().findViewById(R.id.cajaText);
        personalizadaText = getView().findViewById(R.id.personalizadaText);
        notificationsList = getView().findViewById(R.id.notificationsList);
        emptyState = getView().findViewById(R.id.emptyState);
    }

    private void setButtonsWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels - CommonFunctions.convertToPx(40, getContext());

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cumpleañosButton.getLayoutParams();
        params.width = width / 4;
        cumpleañosButton.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams) cadenciaButton.getLayoutParams();
        params.width = width / 4;
        cadenciaButton.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams) cajaButton.getLayoutParams();
        params.width = width / 4;
        cajaButton.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams) personalizadaButton.getLayoutParams();
        params.width = width / 4;
        personalizadaButton.setLayoutParams(params);
    }

    private void setOnClickListeners() {
        cumpleañosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillButton(cumpleañosButton, cumpleText);
                unFillButton(cadenciaButton, cadenciaText);
                unFillButton(cajaButton, cajaText);
                unFillButton(personalizadaButton, personalizadaText);
                tabSelected = 0;
                updateList();
            }
        });

        cadenciaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unFillButton(cumpleañosButton, cumpleText);
                fillButton(cadenciaButton, cadenciaText);
                unFillButton(cajaButton, cajaText);
                unFillButton(personalizadaButton, personalizadaText);
                tabSelected = 1;
                updateList();
            }
        });

        cajaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unFillButton(cumpleañosButton, cumpleText);
                unFillButton(cadenciaButton, cadenciaText);
                fillButton(cajaButton, cajaText);
                unFillButton(personalizadaButton, personalizadaText);
                tabSelected = 2;
                updateList();
            }
        });

        personalizadaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unFillButton(cumpleañosButton, cumpleText);
                unFillButton(cadenciaButton, cadenciaText);
                unFillButton(cajaButton, cajaText);
                fillButton(personalizadaButton, personalizadaText);
                tabSelected = 3;
                updateList();
            }
        });
    }

    private void fillButton(RelativeLayout view, TextView textView) {
        view.setBackgroundTintMode(PorterDuff.Mode.SRC_IN);
        textView.setTextColor(getResources().getColor(R.color.white, null));
    }

    private void unFillButton(RelativeLayout view, TextView textView) {
        view.setBackgroundTintMode(PorterDuff.Mode.ADD);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary, null));
    }

    private void initializeList() {
        notificationsList.setDivider(null);
        notificationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NotificationDayModel dayModel = (NotificationDayModel) notificationsList.getItemAtPosition(i);
                if (dayModel.getDayType() == Constants.notificationCellHeaderType) {
                    return;
                }

                openNotificationDetail(dayModel);
            }
        });
    }

    private void updateList() {
        ArrayList<NotificationModel> notificaciones = Constants.databaseManager.notificationsManager.getNotificationsForType(getNotificationTypeForTab());
        emptyState.setVisibility(notificaciones.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        ArrayList<NotificationDayModel> notificacionesAgrupadas = agruparNotificaciones(notificaciones);
        adapter = new NotificationListAdapter(getContext(), notificacionesAgrupadas);
        notificationsList.setAdapter(adapter);
    }

    private String getNotificationTypeForTab() {
        switch (tabSelected) {
            case 0:
                return Constants.notificationCumpleañosType;
            case 1:
                return Constants.notificationCadenciaType;
            case 2:
                return Constants.notificationcajaType;
            default:
                return Constants.notificationpersonalizadaType;
        }
    }

    private ArrayList<NotificationDayModel> agruparNotificaciones(ArrayList<NotificationModel> notificaciones) {
        switch (getNotificationTypeForTab()) {
            case Constants.notificationCumpleañosType:
                return agruparNotificacionesPorDia(notificaciones);
            case Constants.notificationCadenciaType:
                return agruparNotificacionesPorDia(notificaciones);
            case Constants.notificationcajaType:
                return crearNotiticacionesIndividuales(notificaciones);
            default:
                return crearNotiticacionesIndividuales(notificaciones);
        }
    }

    private ArrayList<NotificationDayModel> agruparNotificacionesPorDia(ArrayList<NotificationModel> notificaciones) {
        ArrayList<Long> fechasInicioDelDiaAgrupadas = new ArrayList<>();
        ArrayList<NotificationDayModel> notificacionesAgrupadas = new ArrayList<>();
        for (int i = 0; i < notificaciones.size(); i++) {
            long beginingOfDay = DateFunctions.getBeginingOfDayFromDate(new Date(notificaciones.get(i).getFecha())).getTime();
            if (!fechasInicioDelDiaAgrupadas.contains(beginingOfDay)) {
                fechasInicioDelDiaAgrupadas.add(beginingOfDay);
                notificacionesAgrupadas.add(new NotificationDayModel(notificaciones.get(i).getFecha(), new ArrayList<>(), Constants.notificationCellRowType, ""));
            }
        }

        for (int i = 0; i < notificacionesAgrupadas.size(); i++) {
            long beginingOfDay = DateFunctions.getBeginingOfDayFromDate(new Date(notificacionesAgrupadas.get(i).getFecha())).getTime();
            long endOfDay = DateFunctions.getEndOfDayFromDate(new Date(notificacionesAgrupadas.get(i).getFecha())).getTime();
            for (int j = 0; j < notificaciones.size(); j++) {
                NotificationModel notificacion = notificaciones.get(j);
                if (notificacion.getFecha() > beginingOfDay && notificacion.getFecha() < endOfDay) {
                    notificacionesAgrupadas.get(i).getNotificaciones().add(notificacion);
                }
            }
        }

        return reagruparNotificaciones(notificacionesAgrupadas);
    }

    private ArrayList<NotificationDayModel> crearNotiticacionesIndividuales(ArrayList<NotificationModel> notificaciones) {
        ArrayList<NotificationDayModel> notificacionesIndividuales = new ArrayList<>();
        for (int i = 0; i < notificaciones.size(); i++) {
            NotificationDayModel model = new NotificationDayModel(notificaciones.get(i).getFecha(), new ArrayList<>(), Constants.notificationCellRowType, "");
            model.getNotificaciones().add(notificaciones.get(i));
            notificacionesIndividuales.add(model);
        }

        return reagruparNotificaciones(notificacionesIndividuales);
    }

    private ArrayList<NotificationDayModel> reagruparNotificaciones(ArrayList<NotificationDayModel> notificacionesAgrupadas) {
        ArrayList<NotificationDayModel> notificacionesFinales = new ArrayList<>();
        ArrayList<NotificationDayModel> notificacionesNuevas = new ArrayList<>();
        ArrayList<NotificationDayModel> notificacionesAntiguas = new ArrayList<>();
        long todayBeginingOfDay = DateFunctions.getBeginingOfDayFromDate(new Date()).getTime();
        for (int i = 0; i < notificacionesAgrupadas.size(); i++) {
            if (notificacionesAgrupadas.get(i).getFecha() < todayBeginingOfDay) {
                notificacionesAntiguas.add(notificacionesAgrupadas.get(i));
            } else {
                notificacionesNuevas.add(notificacionesAgrupadas.get(i));
            }
        }

        if (notificacionesNuevas.size() > 0) {
            notificacionesFinales.add(new NotificationDayModel(0, new ArrayList<>(), Constants.notificationCellHeaderType, "Hoy"));
            notificacionesFinales.addAll(notificacionesNuevas);
        }

        if (notificacionesAntiguas.size() > 0) {
            notificacionesFinales.add(new NotificationDayModel(0, new ArrayList<>(), Constants.notificationCellHeaderType, "Antiguas"));
            notificacionesFinales.addAll(notificacionesAntiguas);
        }

        return notificacionesFinales;
    }

    private void openNotificationDetail(NotificationDayModel model) {
        switch (tabSelected) {
            case 0:
                openBirthdayActivity(model);
                break;
            case 1:
                openCadenciaActivity(model);
                break;
            case 2:
                openCierreCajaActivity(model.getNotificaciones().get(0));
                break;
            default:
                openNotificacionPersonalizadaActivity(model.getNotificaciones().get(0));
                break;
        }
    }

    private void openBirthdayActivity(NotificationDayModel model) {
        Intent intent = new Intent(getContext(), BirthdayDetail.class);
        intent.putExtra("notificaciones", model.getNotificaciones());
        getContext().startActivity(intent);
    }

    private void openCadenciaActivity(NotificationDayModel model) {
        Intent intent = new Intent(getContext(), CadenciaDetail.class);
        intent.putExtra("notificaciones", model.getNotificaciones());
        getContext().startActivity(intent);
    }

    private void openCierreCajaActivity(NotificationModel model) {
        Intent intent = new Intent(getContext(), CierreCajaActivity.class);
        intent.putExtra("fecha", model.getFecha());
        intent.putExtra("notification",model);
        getContext().startActivity(intent);
    }

    private void openNotificacionPersonalizadaActivity(NotificationModel model) {
        Intent intent = new Intent(getContext(), BirthdayDetail.class);
        intent.putExtra("notificaciones", new ArrayList<>(Arrays.asList(model)));
        getContext().startActivity(intent);
    }
}

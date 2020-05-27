package com.example.gestorheme.Activities.Main.Fragments.Notificaciones;

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

import com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Adapter.NotificationListAdapter;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Notification.NotificationModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

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
        notificationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void updateList() {
        ArrayList<NotificationModel> notificaciones = Constants.databaseManager.notificationsManager.getNotificationsForType(getNotificationTypeForTab());
        emptyState.setVisibility(notificaciones.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        adapter = new NotificationListAdapter(getContext(), notificaciones);
        //notificationsList.setAdapter(adapter);
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
}

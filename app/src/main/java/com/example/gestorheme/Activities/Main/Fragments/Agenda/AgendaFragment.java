package com.example.gestorheme.Activities.Main.Fragments.Agenda;

import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.Interfaces.FilterActionSheetInterface;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.Interfaces.ServiceItemViewInterface;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.Views.AgendaItemView;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.Views.CerrarCajaButton;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.Views.ClientItemView;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.Views.FilterActionSheetView;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AgendaFragment extends Fragment implements ServiceItemViewInterface, FilterActionSheetInterface {
    private LinearLayout scrollContentView;
    private CalendarView calendarView;
    private ScrollView scrollView;
    private Date presentDate;
    private ConstraintLayout root;
    private RelativeLayout calendarButton;
    private TextView filterText;
    private boolean clientesVisible = false;
    private BottomSheetDialog filterDialog;
    private long empleadoFilteredId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.agenda_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presentDate = new Date();
        getFields();
        setOnClickListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (clientesVisible) {
            buildClientesDay();
        } else {
            buildAgendaDay();
        }
    }

    private void getFields() {
        scrollContentView = getView().findViewById(R.id.scrollContentView);
        calendarView = getView().findViewById(R.id.calendarView);
        scrollView = getView().findViewById(R.id.scrollView3);
        root = getView().findViewById(R.id.root);
        calendarButton = getView().findViewById(R.id.calendarButton);
        filterText = getView().findViewById(R.id.filterText);
    }

    private void setOnClickListeners() {
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeBounds transition = new ChangeBounds();
                transition.addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) { }
                    @Override
                    public void onTransitionEnd(Transition transition) {
                        if (clientesVisible) {
                            buildClientesDay();
                        } else {
                            buildAgendaDay();
                        }
                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {}
                    @Override
                    public void onTransitionPause(Transition transition) { }
                    @Override
                    public void onTransitionResume(Transition transition) { }
                });

                TransitionManager.go(new Scene(root), transition);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) scrollView.getLayoutParams();
                if (params.topMargin != 0) {
                    params.topMargin = 0;
                } else {
                    params.topMargin = CommonFunctions.convertToPx(350, getContext());
                }

                scrollView.setLayoutParams(params);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                presentDate = calendar.getTime();
                clientesVisible = false;
                calendarButton.performClick();
            }
        });

        getView().findViewById(R.id.clientButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clientesVisible) {
                    clientesVisible = false;
                    buildAgendaDay();
                } else {
                    clientesVisible = true;
                    buildClientesDay();
                }
            }
        });

        getView().findViewById(R.id.filterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog();
            }
        });
    }

    private void buildAgendaDay() {
        scrollContentView.removeAllViews();
        ArrayList<ServiceModel> servicios = Constants.databaseManager.servicesManager.getServicesForDate(presentDate);
        if (empleadoFilteredId != 0) {
            servicios = filterServiciosWithEmpleadoId(servicios);
        }

        Date fecha = DateFunctions.getBeginingOfWorkingDayFromDate(presentDate);
        for (int i = 0; i < 50; i++) {
            AgendaItemView agendaItem = new AgendaItemView(getContext(), fecha, getServicesForServiceView(servicios, fecha), this);
            scrollContentView.addView(agendaItem);
            fecha = DateFunctions.add15MinsToDate(fecha);
        }

        if (!Constants.databaseManager.cierreCajaManager.cierreCajaRealizadoEnFecha(presentDate)) {
            addCierreDeCajaButton();
        }
    }

    private ArrayList<ServiceModel> filterServiciosWithEmpleadoId(ArrayList<ServiceModel> servicios) {
        ArrayList<ServiceModel> serviciosFiltrados = new ArrayList<>();
        for (int i = 0; i < servicios.size(); i++) {
            if (servicios.get(i).getProfesional() == empleadoFilteredId) {
                serviciosFiltrados.add(servicios.get(i));
            }
        }

        return serviciosFiltrados;
    }

    private void buildClientesDay() {
        scrollContentView.removeAllViews();
        ArrayList clientsId = new ArrayList();
        ArrayList<ServiceModel> servicios = Constants.databaseManager.servicesManager.getServicesForDate(presentDate);
        for (int i = 0; i < servicios.size(); i++) {
            if (!clientsId.contains(servicios.get(i).getClientId())) {
                clientsId.add(servicios.get(i).getClientId());
            }
        }

        for (int i = 0; i < clientsId.size(); i++) {
            ClientModel cliente = Constants.databaseManager.clientsManager.getClientForClientId((Long) clientsId.get(i));
            ClientItemView item = new ClientItemView(getContext(), cliente, presentDate);
            scrollContentView.addView(item);
        }
    }

    private ArrayList<ServiceModel> getServicesForServiceView(ArrayList<ServiceModel> servicios, Date fecha) {
        ArrayList<ServiceModel> serviciosRango = new ArrayList<>();
        long plus15Mins = DateFunctions.add15MinsToDate(fecha).getTime();
        for (int i = 0; i < servicios.size(); i++) {
            ServiceModel servicio = servicios.get(i);
            if (servicio.getFecha() >= fecha.getTime() && servicio.getFecha() < plus15Mins) {
                serviciosRango.add(servicio);
            }
        }

        return serviciosRango;
    }

    private void addCierreDeCajaButton() {
        CerrarCajaButton button = new CerrarCajaButton(getContext(), presentDate);
        scrollContentView.addView(button);
    }

    private void showFilterDialog() {
        try {
            LinearLayout filterActionView = new FilterActionSheetView(getContext(), this);
            filterDialog = new BottomSheetDialog(getActivity());
            filterDialog.setContentView(filterActionView);
            filterDialog.show();
            FrameLayout bottomSheet = filterDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            bottomSheet.setBackground(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void serviceRemoved() {
        buildAgendaDay();
    }

    @Override
    public void empleadoSelected(EmpleadoModel empleado) {
        empleadoFilteredId = empleado.getEmpleadoId();
        filterText.setText(empleado.getNombre());
        filterDialog.dismiss();
        buildAgendaDay();
    }

    @Override
    public void cancelarClicked() {
        filterDialog.dismiss();
    }

    @Override
    public void todosButtonClicked() {
        empleadoFilteredId = 0;
        filterText.setText("Todos");
        filterDialog.dismiss();
        buildAgendaDay();
    }
}

package com.example.gestorheme.Activities.Main.Fragments.Agenda;

import android.media.Image;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.gestorheme.Activities.Main.Fragments.Agenda.Interfaces.FilterActionSheetInterface;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.Interfaces.ServiceItemViewInterface;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.Interfaces.ServicesRefreshInterface;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.Views.AgendaItemView;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.Views.CerrarCajaButton;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.Views.ClientItemView;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.Views.FilterActionSheetView;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Common.Preferencias;
import com.example.gestorheme.Common.SyncronizationManager;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendaFragment extends Fragment implements ServiceItemViewInterface, FilterActionSheetInterface {
    private LinearLayout scrollContentView;
    private CalendarView calendarView;
    private SwipeRefreshLayout refreshLayout;
    private Date presentDate;
    private ConstraintLayout root;
    private RelativeLayout calendarButton;
    private BottomSheetDialog filterDialog;
    private TextView filterText;
    private HorizontalCalendar horizontalCalendar;
    private RelativeLayout loadingStateView;
    private RelativeLayout filterButton;
    private RelativeLayout clientButton;
    private ImageView clientImage;
    private ImageView calendarImage;
    private NestedScrollView scrollView;
    private RelativeLayout calendarBackground;

    private boolean clientesVisible = false;
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
        setHorizontalCalendarView();
        setRefreshListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (clientesVisible) {
            buildClientesDay();
        } else {
            buildAgendaDay();
        }

        customizeButtons();
        customizeBackground();
    }

    private void getFields() {
        scrollContentView = getView().findViewById(R.id.agendaContentView);
        calendarView = getView().findViewById(R.id.calendarView);
        root = getView().findViewById(R.id.root);
        calendarButton = getView().findViewById(R.id.calendarButton);
        filterText = getView().findViewById(R.id.filterText);
        refreshLayout = getView().findViewById(R.id.refreshLayout);
        filterButton = getView().findViewById(R.id.filterButton);
        clientButton = getView().findViewById(R.id.clientButton);
        clientImage = getView().findViewById(R.id.clientImage);
        calendarImage = getView().findViewById(R.id.calendarImage);
        scrollView = getView().findViewById(R.id.scrollView);
        calendarBackground = getView().findViewById(R.id.calendarBackground);
    }

    private void customizeButtons() {
        CommonFunctions.customizeView(getActivity().getApplicationContext(), filterButton, AppStyle.getPrimaryColor());
        filterText.setTextColor(AppStyle.getPrimaryColor());
        CommonFunctions.customizeViewWithImage(getActivity().getApplicationContext(), calendarButton, calendarImage, AppStyle.getPrimaryColor(), AppStyle.getPrimaryColor());
        CommonFunctions.customizeViewWithImage(getActivity().getApplicationContext(), clientButton, clientImage, AppStyle.getPrimaryColor(), AppStyle.getPrimaryColor());
        calendarBackground.setBackgroundColor(AppStyle.getBackgroundColor());
        root.setBackgroundColor(AppStyle.getBackgroundColor());
    }

    private void customizeBackground() {
        scrollView.setBackgroundColor(AppStyle.getBackgroundColor());
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
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) refreshLayout.getLayoutParams();
                if (params.topMargin != 0) {
                    params.topMargin = 0;
                } else {
                    params.topMargin = CommonFunctions.convertToPx(350, getContext());
                }

                refreshLayout.setLayoutParams(params);
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
                updateCalendarView();
                calendarButton.performClick();
            }
        });

        clientButton.setOnClickListener(new View.OnClickListener() {
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

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog();
            }
        });
    }

    private void setRefreshListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getServicesForRange();
            }
        });
    }

    private void setHorizontalCalendarView() {
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(DateFunctions.remove2MonthsToDate(presentDate));
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(DateFunctions.add2MonthsToDate(presentDate));
        horizontalCalendar = new HorizontalCalendar.Builder(root, R.id.horizontalCalendarView).datesNumberOnScreen(5).range(startDate, endDate).configure().textColor(AppStyle.getPrimaryTextColor(), AppStyle.getPrimaryColor()).textSize(14, 18, 14).end().build();
        horizontalCalendar.goToday(true);
        horizontalCalendar.getCalendarView().setBackgroundColor(AppStyle.getBackgroundColor());
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                Calendar calendar = date;
                calendar.set(Calendar.HOUR, 14);
                presentDate = calendar.getTime();
                if (clientesVisible) {
                    buildClientesDay();
                } else {
                    buildAgendaDay();
                }
            }
        });
    }

    private void updateCalendarView() {
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(DateFunctions.remove2MonthsToDate(presentDate));
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(DateFunctions.add2MonthsToDate(presentDate));
        Calendar presentDate = Calendar.getInstance();
        presentDate.setTime(this.presentDate);
        horizontalCalendar.setRange(startDate, endDate);
        horizontalCalendar.selectDate(presentDate, true);
        horizontalCalendar.refresh();
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

        if (!Constants.databaseManager.cierreCajaManager.cierreCajaRealizadoEnFecha(presentDate) && servicios.size() > 0) {
            addCierreDeCajaButton();
        }
    }

    private ArrayList<ServiceModel> filterServiciosWithEmpleadoId(ArrayList<ServiceModel> servicios) {
        ArrayList<ServiceModel> serviciosFiltrados = new ArrayList<>();
        for (int i = 0; i < servicios.size(); i++) {
            if (servicios.get(i).getEmpleadoId() == empleadoFilteredId) {
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateFunctions.add15MinsToDate(fecha));
        long plus15Mins = calendar.getTimeInMillis() / 1000;
        calendar.setTime(fecha);
        long actualTime = calendar.getTimeInMillis() / 1000;
        for (int i = 0; i < servicios.size(); i++) {
            ServiceModel servicio = servicios.get(i);
            if (servicio.getFecha() >= actualTime && servicio.getFecha() < plus15Mins) {
                serviciosRango.add(servicio);
            }
        }

        return serviciosRango;
    }

    private void addCierreDeCajaButton() {
        CerrarCajaButton button = new CerrarCajaButton(getContext(), presentDate, getActivity());
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
    public void showLoadingState(String description) {
        loadingStateView = CommonFunctions.createLoadingStateView(getContext(), description);
        root.addView(loadingStateView);
    }

    @Override
    public void hideLoadingState() {
        root.removeView(loadingStateView);
    }

    @Override
    public void showErrorMessage(String message) {
        CommonFunctions.showGenericAlertMessage(getActivity(), message);
    }

    @Override
    public void logout() {
        CommonFunctions.logout(getActivity());
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

    private void getServicesForRange() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateFunctions.getBeginingOfWorkingDayFromDate(presentDate));
        long beginingOfDay = calendar.getTimeInMillis() / 1000;
        calendar.setTime(DateFunctions.getEndOfWorkingDayFromDate(presentDate));
        long endOfDay = calendar.getTimeInMillis() / 1000;
        Call<ArrayList<ServiceModel>> call = Constants.webServices.getServiciosPorRango(Preferencias.getComercioIdFromSharedPreferences(getContext()), beginingOfDay, endOfDay);
        call.enqueue(new Callback<ArrayList<ServiceModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ServiceModel>> call, Response<ArrayList<ServiceModel>> response) {
                refreshLayout.setRefreshing(false);
                if (response.code() == 200 && response.body().size() > 0) {
                    for (int i = 0; i < response.body().size(); i++) {
                        ServiceModel service = response.body().get(i);
                        if (Constants.databaseManager.servicesManager.getServiceForServiceId(service.getServiceId()) != null) {
                            Constants.databaseManager.servicesManager.updateServiceInDatabase(service);
                        } else {
                            Constants.databaseManager.servicesManager.addServiceToDatabase(service);
                        }

                        SyncronizationManager.deleteLocalServicesIfNeeded(response.body());
                        if (clientesVisible) {
                            buildClientesDay();
                        } else {
                            buildAgendaDay();
                        }
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

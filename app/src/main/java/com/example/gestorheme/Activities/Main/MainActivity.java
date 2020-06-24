package com.example.gestorheme.Activities.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.AgendaFragment;
import com.example.gestorheme.Activities.Main.Fragments.Heme.HemeFragment;
import com.example.gestorheme.Activities.Main.Fragments.ListaClientes.ClientesFragment;
import com.example.gestorheme.Activities.Main.Fragments.Notificaciones.NotificacionesFragment;
import com.example.gestorheme.ApiServices.WebServices;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.Preferencias;
import com.example.gestorheme.Common.SyncronizationManager;
import com.example.gestorheme.Models.EstiloApp.EstiloAppModel;
import com.example.gestorheme.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public String listaClientesTag = "listaClientes";
    public String agendaTag = "agenda";
    public String notificacionesTag = "notificaciones";
    public String hemeTag = "heme";
    private BottomNavigationView bottomNavigationBar;
    private boolean isLoginFlow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLoginIntent();
        setViews();
        customizeNavigationBar();
        setNavigationTabClick();
        SyncronizationManager.syncAllDataFromServer(this, getApplicationContext());
        changeFragment(new ClientesFragment(), listaClientesTag);
        if (!isLoginFlow) {
            getEstiloPrivado();
        }
    }

    private void getLoginIntent() {
        isLoginFlow = getIntent().getBooleanExtra("login", false);
    }

    private void setViews() {
        bottomNavigationBar = findViewById(R.id.navigation_bar);
    }

    private void customizeNavigationBar() {
        bottomNavigationBar.setBackgroundColor(AppStyle.getNavigationColor());
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_selected}, // enabled
                new int[] {-android.R.attr.state_selected}, // disabled
        };
        int[] colors = new int[] {AppStyle.getPrimaryColor(), AppStyle.getPrimaryTextColor(),};

        ColorStateList stateList = new ColorStateList(states, colors);
        bottomNavigationBar.setItemIconTintList(stateList);
        bottomNavigationBar.setItemTextColor(stateList);
        MenuItem item = bottomNavigationBar.getMenu().getItem(3);
        item.setTitle(AppStyle.getAppName());

        AppStyle.setStatusBarColor(this);
    }

    private void setNavigationTabClick() {
        bottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_clientes:
                        changeFragment(new ClientesFragment(), listaClientesTag);
                        break;
                    case R.id.nav_agenda:
                        changeFragment(new AgendaFragment(), agendaTag);
                        break;
                    case  R.id.nav_notificaciones:
                        changeFragment(new NotificacionesFragment(), notificacionesTag);
                        break;
                    default:
                        changeFragment(new HemeFragment(), hemeTag);
                        break;
                }
                return true;
            }
        });
    }

    private void changeFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment, tag).commit();
    }

    private void getEstiloPrivado() {
        Call<EstiloAppModel> call = Constants.webServices.getEstiloPrivado(Preferencias.getComercioIdFromSharedPreferences(getApplicationContext()));
        call.enqueue(new Callback<EstiloAppModel>() {
            @Override
            public void onResponse(Call<EstiloAppModel> call, Response<EstiloAppModel> response) {
                if (response.code() == 200) {
                    Constants.databaseManager.estiloAppManager.updateEstiloAppInDatabase(response.body());
                    getVisibleFragmentAndUpdateStyle();
                }
            }

            @Override
            public void onFailure(Call<EstiloAppModel> call, Throwable t) {

            }
        });
    }

    private void getVisibleFragmentAndUpdateStyle() {
        ClientesFragment clientesFragment = (ClientesFragment) getSupportFragmentManager().findFragmentByTag(listaClientesTag);
        AgendaFragment agendaFragment = (AgendaFragment) getSupportFragmentManager().findFragmentByTag(agendaTag);
        NotificacionesFragment notificacionesFragment = (NotificacionesFragment) getSupportFragmentManager().findFragmentByTag(notificacionesTag);
        HemeFragment hemeFragment = (HemeFragment) getSupportFragmentManager().findFragmentByTag(hemeTag);
        if (clientesFragment != null && clientesFragment.isVisible()) {
            clientesFragment.customizeLayout();
        }

        if (agendaFragment != null && agendaFragment.isVisible()) {
            //TODO
        }

        if (notificacionesFragment != null && notificacionesFragment.isVisible()) {
            //TODO
        }

        if (hemeFragment != null && hemeFragment.isVisible()) {
            //TODO
        }
    }
}

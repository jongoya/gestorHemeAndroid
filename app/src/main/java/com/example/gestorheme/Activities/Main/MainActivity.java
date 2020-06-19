package com.example.gestorheme.Activities.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.gestorheme.Activities.Main.Fragments.Agenda.AgendaFragment;
import com.example.gestorheme.Activities.Main.Fragments.Heme.HemeFragment;
import com.example.gestorheme.Activities.Main.Fragments.ListaClientes.ClientesFragment;
import com.example.gestorheme.Activities.Main.Fragments.Notificaciones.NotificacionesFragment;
import com.example.gestorheme.Common.SyncronizationManager;
import com.example.gestorheme.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public String listaClientesTag = "listaClientes";
    public String agendaTag = "agenda";
    public String notificacionesTag = "notificaciones";
    public String hemeTag = "heme";
    private BottomNavigationView bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setNavigationTabClick();
        SyncronizationManager.syncAllDataFromServer(this, getApplicationContext());
        changeFragment(new ClientesFragment(), listaClientesTag);
    }

    private void setViews() {
        bottomNavigationBar = findViewById(R.id.navigation_bar);
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
}

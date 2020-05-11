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
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.LocalDatabase.DatabaseManager;
import com.example.gestorheme.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Constants.databaseManager = new DatabaseManager(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setNavigationTabClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                        changeFragment(new ClientesFragment());
                        break;
                    case R.id.nav_agenda:
                        changeFragment(new AgendaFragment());
                        break;
                    case  R.id.nav_notificaciones:
                        changeFragment(new NotificacionesFragment());
                        break;
                    default:
                        changeFragment(new HemeFragment());
                        break;
                }
                return true;
            }
        });
    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment).addToBackStack(null).commit();
    }

}

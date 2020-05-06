package com.example.gestorheme.Activities.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gestorheme.Activities.Main.Fragments.Agenda.AgendaFragment;
import com.example.gestorheme.Activities.Main.Fragments.Heme.HemeFragment;
import com.example.gestorheme.Activities.Main.Fragments.ListaClientes.ClientesFragment;
import com.example.gestorheme.Activities.Main.Fragments.Notificaciones.NotificacionesFragment;
import com.example.gestorheme.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationBar;
    private TextView title;
    private ImageButton firstRightButton;
    private ImageButton secondRightButton;

    private int selectedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setNavigationTabClick();
        setTitle();
        setNavigationButtonClicks();
        manageNavigationButtonImages();
    }

    private void setViews() {
        bottomNavigationBar = findViewById(R.id.navigation_bar);
        title = findViewById(R.id.title);
        firstRightButton = findViewById(R.id.first_right_button);
        secondRightButton = findViewById(R.id.second_right_button);
    }

    private void setNavigationTabClick() {
        bottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_clientes:
                        selectedItem = 0;
                        changeFragment(new ClientesFragment());
                        break;
                    case R.id.nav_agenda:
                        selectedItem = 1;
                        changeFragment(new AgendaFragment());
                        break;
                    case  R.id.nav_notificaciones:
                        selectedItem = 2;
                        changeFragment(new NotificacionesFragment());
                        break;
                    default:
                        selectedItem = 3;
                        changeFragment(new HemeFragment());
                        break;
                }
                return true;
            }
        });
    }

    private void setNavigationButtonClicks() {
        firstRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItem == 0) {
                    //TODO añadir cliente
                } else if (selectedItem == 1) {
                    //TODO mostrar calendario mensual
                } else if (selectedItem == 3) {
                    //TODO seccion ajustes
                }
            }
        });

        secondRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItem == 1) {
                    //TODO mostrar clientes del dia
                }
            }
        });
    }

    private void manageNavigationButtonImages() {
        switch (selectedItem) {
            case 0:
                secondRightButton.setVisibility(View.INVISIBLE);
                firstRightButton.setVisibility(View.VISIBLE);
                firstRightButton.setImageResource(R.drawable.plus_image);
                break;
            case 1:
                secondRightButton.setVisibility(View.VISIBLE);
                firstRightButton.setVisibility(View.VISIBLE);
                firstRightButton.setImageResource(R.drawable.calendar_image);
                secondRightButton.setImageResource(R.drawable.agenda_client_image);
                break;
            case 2:
                secondRightButton.setVisibility(View.INVISIBLE);
                firstRightButton.setVisibility(View.INVISIBLE);
                break;
            default:
                secondRightButton.setVisibility(View.INVISIBLE);
                firstRightButton.setVisibility(View.VISIBLE);
                firstRightButton.setImageResource(R.drawable.settings_image);
                break;
        }
    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment).addToBackStack(null).commit();
        setTitle();
        manageNavigationButtonImages();
    }

    private void setTitle() {
        switch (selectedItem) {
            case 0:
                title.setText("Clientes");
                break;
            case 1:
                title.setText("Agenda");
                break;
            case 2:
                title.setText("Notificaciones");
                break;
            default:
                title.setText("Heme");
                break;
        }
    }
}

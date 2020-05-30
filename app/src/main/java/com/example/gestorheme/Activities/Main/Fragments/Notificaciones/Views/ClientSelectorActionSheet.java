package com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.gestorheme.Activities.Main.Fragments.Agenda.Views.FilterEmpleadoView;
import com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Interfaces.ClientSelectorActionSheetInterface;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

public class ClientSelectorActionSheet extends LinearLayout {

    private LinearLayout clientesLayout;
    private ClientSelectorActionSheetInterface delegate;

    public ClientSelectorActionSheet(Context context, ArrayList<ClientModel> clientes, ClientSelectorActionSheetInterface delegate) {
        super(context);
        this.delegate = delegate;
        View.inflate(context, R.layout.filter_actionsheet_layout, this);
        getFields();
        setOnClickListeners();
        addClientesViews(context, clientes);
    }

    private void getFields() {
        clientesLayout = findViewById(R.id.empleados_layout);
    }

    private void setOnClickListeners() {
        findViewById(R.id.cancelarButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.cancelarClicked();
            }
        });
    }

    private void addClientesViews(Context contexto, ArrayList<ClientModel> clientes) {

        for (int i = 0; i < clientes.size(); i++) {
            final ClientModel cliente = clientes.get(i);
            addButtonWithCliente(contexto, cliente);

            if (i < clientes.size() - 1) {
                addDivisory(contexto);
            }
        }
    }

    private void addButtonWithCliente(Context contexto, final ClientModel cliente) {
        FilterEmpleadoView view = new FilterEmpleadoView(contexto, cliente.getNombre() + " " + cliente.getApellidos());
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.clienteSelcted(cliente);
            }
        });

        clientesLayout.addView(view);
    }

    private void addDivisory(Context contexto) {
        View divisory = new View(contexto);
        divisory.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        divisory.setBackgroundResource(R.color.dividerColor);
        clientesLayout.addView(divisory);
    }
}

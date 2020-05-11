package com.example.gestorheme.Activities.ClientListSelector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorheme.Activities.Main.Fragments.ListaClientes.Adapter.ClientListAdapter;
import com.example.gestorheme.Activities.Main.Fragments.ListaClientes.Models.ListaClienteCellModel;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

public class ClientListSelectorActivity extends AppCompatActivity {
    private static final int TYPE_CLIENT = 0;
    private static final int TYPE_HEADER = 1;
    private ListView clientList;

    private ClientListAdapter clientListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_list_selector_layout);
        getFields();

        setListProperties();
        setClientList();
    }

    private void getFields() {
        clientList = findViewById(R.id.clientList);
    }

    private void setListProperties() {
        clientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = getIntent();
                ListaClienteCellModel model = clientListAdapter.clientes.get(i);
                intent.putExtra("cliente", model);
                setResult(RESULT_OK, intent);
                ClientListSelectorActivity.super.onBackPressed();
            }
        });
    }

    private void setClientList() {
        clientListAdapter = new ClientListAdapter(this);
        ArrayList<ClientModel> clientes = Constants.databaseManager.clientsManager.getClientsFromDatabase();

        sortClients(clientes);
    }

    private void sortClients(ArrayList<ClientModel> clientes) {
        ArrayList<String> letras = CommonFunctions.getClientListIndex();
        for (int i = 0; i < letras.size(); i++) {
            String letra = letras.get(i).toLowerCase();
            ArrayList<ListaClienteCellModel> clientesPorLetra = new ArrayList<>();
            if (letra.equals("vacio")) {
                clientesPorLetra = getClientsWithoutSurname(clientes);
            } else {
                for (int j = 0; j < clientes.size(); j++) {
                    ClientModel cliente = clientes.get(j);
                    if (cliente.getApellidos().toLowerCase().startsWith(letra)) {
                        clientesPorLetra.add(new ListaClienteCellModel(TYPE_CLIENT, cliente));
                    }
                }
            }

            if (clientesPorLetra.size() > 0) {
                clientListAdapter.addItem(new ListaClienteCellModel(TYPE_HEADER, letras.get(i)));
                for (int j = 0; j < clientesPorLetra.size(); j++) {
                    clientListAdapter.addItem(clientesPorLetra.get(j));
                }
            }
        }

        clientList.setAdapter(clientListAdapter);
    }

    private ArrayList<ListaClienteCellModel> getClientsWithoutSurname(ArrayList<ClientModel> clientes) {
        ArrayList<ListaClienteCellModel> clients = new ArrayList<>();
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getApellidos().length() == 0) {
                clients.add(new ListaClienteCellModel(TYPE_CLIENT, clientes.get(i)));
            }
        }

        return clients;
    }
}

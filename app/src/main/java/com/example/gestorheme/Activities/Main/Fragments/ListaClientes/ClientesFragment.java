package com.example.gestorheme.Activities.Main.Fragments.ListaClientes;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.gestorheme.Activities.ClientDetail.ClientDetailActivity;
import com.example.gestorheme.Activities.Main.Fragments.ListaClientes.Adapter.ClientListAdapter;
import com.example.gestorheme.Activities.Main.Fragments.ListaClientes.Models.ListaClienteCellModel;
import com.example.gestorheme.Activities.Main.MainActivity;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.SyncronizationManager;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

public class ClientesFragment extends Fragment {
    private EditText clientFilterField;
    private ListView clientList;
    private ClientListAdapter clientListAdapter;
    public SwipeRefreshLayout refreshLayout;

    private static final int TYPE_CLIENT = 0;
    private static final int TYPE_HEADER = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clientes_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setViews();
        addClickListeners();
        setClientsTextListener();
        setListProperties();
        setCLientsFieldCleanClick();
        setRefreshLayoutListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        setClientList();
    }

    private void setViews() {
        clientFilterField = getView().findViewById(R.id.client_filter_field);
        clientList = getView().findViewById(R.id.client_list);
        refreshLayout = getView().findViewById(R.id.refreshLayout);
    }

    private void addClickListeners() {
        getView().findViewById(R.id.add_client_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ClientDetailActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void setRefreshLayoutListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SyncronizationManager.getAllClients((MainActivity) getActivity());
            }
        });
    }

    private void setListProperties() {
        clientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListaClienteCellModel model = clientListAdapter.clientes.get(i);
                if (model.getCellType() == TYPE_CLIENT) {
                    Intent intent = new Intent(getActivity(), ClientDetailActivity.class);
                    intent.putExtra("Cliente", model.getCliente());
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    private void setCLientsFieldCleanClick(){
        getView().findViewById(R.id.clean_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientFilterField.setText("");
            }
        });
    }

    private void setClientsTextListener() {
        clientFilterField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                setClientList();
            }
        });
    }

    public void setClientList() {
        clientListAdapter = new ClientListAdapter(getContext());
        ArrayList<ClientModel> clientes = Constants.databaseManager.clientsManager.getClientsFromDatabase();
        if (clientFilterField.getText().length() > 0) {
            clientes = filterClientsWithText(clientes, clientFilterField.getText().toString());
        }

        sortClients(clientes);
    }

    private ArrayList<ClientModel> filterClientsWithText(ArrayList<ClientModel> clientes, String text) {
        ArrayList<ClientModel> clientesFiltrados = new ArrayList<>();
        for (int i = 0; i < clientes.size(); i++) {
            String nombreCompleto = clientes.get(i).getNombre() + " " + clientes.get(i).getApellidos();
            if (nombreCompleto.toLowerCase().contains(text.toLowerCase())) {
                clientesFiltrados.add(clientes.get(i));
            }
        }

        return clientesFiltrados;
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

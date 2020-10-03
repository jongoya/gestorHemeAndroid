package com.example.gestorheme.Activities.ClientListSelector;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.gestorheme.Activities.Main.Fragments.ListaClientes.Adapter.ClientListAdapter;
import com.example.gestorheme.Activities.Main.Fragments.ListaClientes.Models.ListaClienteCellModel;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

public class ClientListSelectorActivity extends AppCompatActivity {
    private static final int TYPE_CLIENT = 0;
    private static final int TYPE_HEADER = 1;
    private ListView clientList;
    private ConstraintLayout root;
    private ImageButton cleanButton;
    private RelativeLayout filterView;
    private EditText clientFilterField;

    private ClientListAdapter clientListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_list_selector_layout);
        AppStyle.setStatusBarColor(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getFields();
        customizeFields();
        customizeEditText();
        customizeFilterView();
        customizeButtons();

        setListProperties();
        setClientList();
        setClientsTextListener();
        setCLientsFieldCleanClick();
    }

    private void getFields() {
        clientList = findViewById(R.id.clientList);
        root = findViewById(R.id.root);
        cleanButton = findViewById(R.id.clean_button);
        filterView = findViewById(R.id.filter_view);
        clientFilterField = findViewById(R.id.client_filter_field);
    }

    private void customizeFields() {
        root.setBackgroundColor(AppStyle.getBackgroundColor());
    }

    private void customizeEditText() {
        CommonFunctions.customizeTextField(getApplicationContext(), clientFilterField, AppStyle.getSecondaryTextColor(), AppStyle.getPrimaryTextColor(), AppStyle.getSecondaryTextColor());
    }

    private void customizeButtons() {
        cleanButton.setColorFilter(AppStyle.getPrimaryColor());
    }

    private void customizeFilterView() {
        filterView.setBackgroundColor(AppStyle.getBackgroundColor());
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

    private void setCLientsFieldCleanClick(){
        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientFilterField.setText("");
            }
        });
    }

    private void setClientList() {
        clientList.setBackgroundColor(AppStyle.getBackgroundColor());
        clientListAdapter = new ClientListAdapter(this);
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
}

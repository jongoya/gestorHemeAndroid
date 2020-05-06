package com.example.gestorheme.Activities.Main.Fragments.ListaClientes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

public class ClientesFragment extends Fragment {
    private EditText clientFilterField;
    private ListView clientList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clientes_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        setClientList();
    }

    void setViews() {
        clientFilterField = getView().findViewById(R.id.client_filter_field);
        clientList = getView().findViewById(R.id.client_list);
    }

    void setClientList() {
        ArrayList<ClientModel> clientes = Constants.databaseManager.clientsManager.getClientsFromDatabase();
        System.out.println();
    }
}

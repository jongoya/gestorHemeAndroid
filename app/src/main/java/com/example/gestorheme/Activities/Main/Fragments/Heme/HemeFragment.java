package com.example.gestorheme.Activities.Main.Fragments.Heme;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HemeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.heme_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.load_clients).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String clientJson = clientsJSONFile("clientesHeme");
                    parseJsonString(clientJson);
                } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        });
    }

    private String clientsJSONFile (String filename) throws IOException {
        AssetManager manager = getContext().getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    private void parseJsonString(String json) {
        try {
            JSONArray clientsJsonArray = new JSONArray(json);
            Date date = new Date();
            for (int i = 0; i < clientsJsonArray.length(); i++) {
                parseJsonObject(clientsJsonArray.getJSONObject(i), date);
                date = addOneHourToDate(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJsonObject(JSONObject jsonClient, Date date) throws Exception {
        ClientModel cliente = new ClientModel();
        cliente.setClientId(date.getTime());
        cliente.setNombre(jsonClient.getString("Nombre"));
        cliente.setApellidos(jsonClient.getString("Apellidos"));
        cliente.setDireccion(jsonClient.has("Direcion") ? jsonClient.getString("Direcion") : "");
        cliente.setTelefono(jsonClient.has("Telefono") ? jsonClient.getString("Telefono") : "");
        cliente.setFecha(jsonClient.has("Cumpleaños") ? convertFechaToTimestamp(jsonClient.getString("Cumpleaños")) : 0);
        cliente.setCadenciaVisita(Constants.cadaDosSemanas);
        cliente.setObservaciones(jsonClient.has("Comentarios") ? jsonClient.getString("Comentarios") : "");

        Constants.databaseManager.clientsManager.addClientToDatabase(cliente);

        date = addOneHourToDate(date);

        ServiceModel servicio = new ServiceModel();
        servicio.setServiceId(date.getTime());
        servicio.setClientId(cliente.getClientId());
        servicio.setNombre(cliente.getNombre());
        servicio.setApellidos(cliente.getApellidos());
        servicio.setProfesional(651154318);
        servicio.setFecha(jsonClient.has("Fecha") ? convertFechaToTimestamp(jsonClient.getString("Fecha")) : 0);
        servicio.setServicios(new ArrayList());
        servicio.setObservaciones(jsonClient.has("Servicio") ? jsonClient.getString("Servicio") : "");

        Constants.databaseManager.servicesManager.addServiceToDatabase(servicio);

        if (jsonClient.has("Servicio2")) {
            date = addOneHourToDate(date);

            ServiceModel servicio2 = new ServiceModel();
            servicio2.setServiceId(date.getTime());
            servicio2.setClientId(cliente.getClientId());
            servicio2.setNombre(cliente.getNombre());
            servicio2.setApellidos(cliente.getApellidos());
            servicio2.setProfesional(651154318);
            servicio2.setFecha(jsonClient.has("Fecha2") ? convertFechaToTimestamp(jsonClient.getString("Fecha2")) : 0);
            servicio2.setServicios(new ArrayList());
            servicio2.setObservaciones(jsonClient.has("Servicio2") ? jsonClient.getString("Servicio2") : "");

            Constants.databaseManager.servicesManager.addServiceToDatabase(servicio2);
        }
    }

    private long convertFechaToTimestamp(String fecha) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date date = formatter.parse(fecha);
        return date.getTime();
    }

    private Date addOneHourToDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, 1);
        return c.getTime();
    }
}

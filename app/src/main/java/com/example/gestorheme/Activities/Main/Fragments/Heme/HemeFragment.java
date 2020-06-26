package com.example.gestorheme.Activities.Main.Fragments.Heme;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.gestorheme.Activities.Main.Fragments.Heme.Adapter.ComercioListAdapter;
import com.example.gestorheme.Activities.Settings.SettingsActivity;
import com.example.gestorheme.Activities.Stadisticas.StadisticasActivity;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Preferencias;
import com.example.gestorheme.Models.Comercio.ComercioModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

public class HemeFragment extends Fragment {
    private ListView comercioList;
    private RelativeLayout settingButton;
    private ImageView settingsImage;

    private ArrayList<ComercioModel> opciones = new ArrayList<>();
    private ComercioListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.heme_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFields();
        customizeLayout();
        setList();
        setOnClickListeners();
    }

    private void getFields() {
        comercioList = getView().findViewById(R.id.comercioList);
        settingButton = getView().findViewById(R.id.settingsButton);
        settingsImage = getView().findViewById(R.id.settingsImage);
    }

    public void customizeLayout() {
        customizeButton();
        cuatomizeListBackground();
    }

    private void customizeButton() {
        CommonFunctions.customizeViewWithImage(getContext(), settingButton, settingsImage, AppStyle.getPrimaryColor(), AppStyle.getPrimaryColor());
    }

    private void cuatomizeListBackground() {
        comercioList.setBackgroundColor(AppStyle.getBackgroundColor());
    }

    private void setList() {
        opciones.add(new ComercioModel(R.drawable.cash_image, "CAJA", "Las estadististicas de los cierres de caja de la peluqueria Heme"));
        adapter = new ComercioListAdapter(getContext(), opciones);
        comercioList.setAdapter(adapter);
        comercioList.setDivider(null);
        comercioList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showPasswordDialog();
            }
        });
    }

    private void setOnClickListeners() {
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void showPasswordDialog() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.password_dialog_layout, null);

        EditText passwordInput = dialogView.findViewById(R.id.passwordInput);
        RelativeLayout aceptarButton = dialogView.findViewById(R.id.aceptarButton);
        RelativeLayout cancelarButton = dialogView.findViewById(R.id.cancelarButton);
        ConstraintLayout rootLayout = dialogView.findViewById(R.id.root);
        TextView titulo = dialogView.findViewById(R.id.titulo);
        TextView aceptarText = dialogView.findViewById(R.id.aceptarText);
        TextView cancelarText = dialogView.findViewById(R.id.cancelarText);

        titulo.setTextColor(AppStyle.getPrimaryTextColor());
        aceptarText.setTextColor(AppStyle.getPrimaryTextColor());
        cancelarText.setTextColor(AppStyle.getPrimaryTextColor());
        CommonFunctions.customizeView(getContext(), aceptarButton, AppStyle.getSecondaryColor());
        CommonFunctions.customizeView(getContext(), cancelarButton, AppStyle.getSecondaryColor());
        CommonFunctions.customizeView(getContext(), rootLayout, AppStyle.getSecondaryColor());
        CommonFunctions.customizeTextField(getContext(), passwordInput, AppStyle.getSecondaryColor(), AppStyle.getPrimaryTextColor(), AppStyle.getSecondaryTextColor());

        aceptarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordInput.getText().length() == 0) {
                    CommonFunctions.showGenericAlertMessage(getActivity(), "Debe incluir una contraseña válida");
                    return;
                }

                if (passwordInput.getText().toString().compareTo(Preferencias.getPasswordFromSharedPreferences(getActivity().getApplicationContext())) != 0) {
                    CommonFunctions.showGenericAlertMessage(getActivity(), "La contraseña es erronea, inténtelo de nuevo");
                    return;
                }

                dialogBuilder.dismiss();
                Intent intent = new Intent(getActivity(), StadisticasActivity.class);
                getActivity().startActivity(intent);
            }
        });

        cancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }
}

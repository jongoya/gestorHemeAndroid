package com.example.gestorheme.Common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.example.gestorheme.Activities.Login.LoginActivity;
import com.example.gestorheme.BuildConfig;
import com.example.gestorheme.Common.Views.LoadingStateView;
import com.example.gestorheme.Models.Cadencia.CadenciaModel;
import com.example.gestorheme.Models.TipoServicio.TipoServicioModel;
import com.example.gestorheme.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class CommonFunctions {

    public static ArrayList<String> getClientListIndex() {
        return new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "Vacio"));
    }

    public static ArrayList<CadenciaModel> getCadenciasArray() {
        return new ArrayList<>(Arrays.asList(new CadenciaModel(Constants.unaSemana), new CadenciaModel(Constants.dosSemanas), new CadenciaModel(Constants.tresSemanas), new CadenciaModel(Constants.unMes), new CadenciaModel(Constants.unMesUnaSemana), new CadenciaModel(Constants.unMesDosSemanas), new CadenciaModel(Constants.unMesTresSemanas), new CadenciaModel(Constants.dosMeses), new CadenciaModel(Constants.dosMesesYUnaSemana), new CadenciaModel(Constants.dosMesesYDosSemanas), new CadenciaModel(Constants.dosMesesYTresSemanas), new CadenciaModel(Constants.tresMeses), new CadenciaModel(Constants.masDeTresMeses)));
    }

    public static String convertBitmapToBase64String(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public static Bitmap convertBase64StringToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static void showGenericAlertMessage(Activity activity, String mensaje) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(mensaje);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }

    public static String getServiciosForServicio(ArrayList servicios) {
        String servicioString = "";
        for (int i = 0; i < servicios.size(); i++) {
            long servicioId = (long) servicios.get(i);
            TipoServicioModel tipoServicioModel = Constants.databaseManager.tipoServiciosManager.getServicioForServicioId(servicioId);
            servicioString += tipoServicioModel.getNombre() + ", ";
        }
        return servicioString;
    }

    public static int convertToPx(int dp, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static RelativeLayout createLoadingStateView(Context context, String descripcion) {
        LoadingStateView loadingState = new LoadingStateView(context, descripcion);

        return loadingState;
    }

    public static void customizeTextField(Context contexto, TextView layout, int viewColor, int primaryColor, int secondaryColor) {
        GradientDrawable unwrappedDrawable = (GradientDrawable) AppCompatResources.getDrawable(contexto, R.drawable.heme_cell_rounded_view);
        unwrappedDrawable.setStroke(CommonFunctions.convertToPx(1, contexto), viewColor);
        layout.setBackground(unwrappedDrawable);

        layout.setTextColor(primaryColor);
        layout.setHintTextColor(secondaryColor);
    }

    public static void customizeView(Context contexto, View layout, int viewColor) {
        GradientDrawable unwrappedDrawable = (GradientDrawable) AppCompatResources.getDrawable(contexto, R.drawable.heme_cell_rounded_view);
        unwrappedDrawable.setStroke(CommonFunctions.convertToPx(1, contexto), viewColor);
        layout.setBackground(unwrappedDrawable);
    }

    public static void customizeViewWithImage(Context contexto, View layout, ImageView imagen, int viewColor, int imageColor) {
        GradientDrawable unwrappedDrawable = (GradientDrawable) AppCompatResources.getDrawable(contexto, R.drawable.rounded_view);
        unwrappedDrawable.setStroke(CommonFunctions.convertToPx(1, contexto), viewColor);
        layout.setBackground(unwrappedDrawable);
        imagen.setColorFilter(imageColor, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public static String getDeviceModel() {
        String fabricante = Build.MANUFACTURER;
        String modelo = Build.MODEL;
        return modelo + " " + fabricante;
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getBundleId() {
        return BuildConfig.APPLICATION_ID.replace(".", "_");
    }

    public static void logout(Activity activity) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Alerta");
        alertDialog.setMessage("Has sido deslogueado de la aplicación, inicie sesió de nuevo");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Constants.databaseManager.deleteAllRecordsFromDatabase();
                        Preferencias.eliminarTodasLasPreferencias(activity.getApplicationContext());
                        Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                });
        alertDialog.show();

    }
}

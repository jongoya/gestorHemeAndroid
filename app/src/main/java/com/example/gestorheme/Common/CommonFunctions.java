package com.example.gestorheme.Common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

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

    public static void selectLayout(Context contexto, View layout, ImageView imagen) {
        GradientDrawable unwrappedDrawable = (GradientDrawable) AppCompatResources.getDrawable(contexto, R.drawable.rounded_view);
        unwrappedDrawable.setStroke(CommonFunctions.convertToPx(1, contexto), contexto.getResources().getColor(R.color.colorPrimary));
        layout.setBackground(unwrappedDrawable);
        if (imagen != null) {
            imagen.setColorFilter(ContextCompat.getColor(contexto, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }

    public static void unSelectLayout(Context contexto, View layout, ImageView imagen) {
        GradientDrawable unwrappedDrawable = (GradientDrawable)AppCompatResources.getDrawable(contexto, R.drawable.rounded_view);
        unwrappedDrawable.setStroke(CommonFunctions.convertToPx(1, contexto), contexto.getResources().getColor(R.color.dividerColor));
        layout.setBackground(unwrappedDrawable);
        if (imagen != null) {
            imagen.setColorFilter(ContextCompat.getColor(contexto, R.color.dividerColor), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }
}

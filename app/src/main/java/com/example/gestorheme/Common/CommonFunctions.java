package com.example.gestorheme.Common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.gestorheme.Models.Cadencia.CadenciaModel;
import com.example.gestorheme.Models.TipoServicio.TipoServicioModel;

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
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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
}

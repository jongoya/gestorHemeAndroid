package com.example.gestorheme.Common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import com.example.gestorheme.Models.EstiloApp.EstiloAppModel;

public class AppStyle {

    public static int getLoginPrimaryTextColor(Context contexto) {
        String primaryTextColor = "#000000";
        if (Preferencias.checkPrimaryTextColor(contexto)) {
            primaryTextColor = Preferencias.getPrimaryTextColor(contexto);
        }

        return Color.parseColor(primaryTextColor);
    }


    public static int getLoginSecondaryTextColor(Context context) {
        String secondaryTextColor = "#D1D1D6";
        if (Preferencias.checkSecondaryTextColor(context)) {
            secondaryTextColor = Preferencias.getSecondaryTextColor(context);
        }

        return Color.parseColor(secondaryTextColor);
    }

    public static int getLoginPrimaryColor(Context context) {
        String primaryColor = "#000000";
        if (Preferencias.checkPrimaryColor(context)) {
            primaryColor = Preferencias.getPrimaryColor(context);
        }

        return Color.parseColor(primaryColor);
    }

    public static int getPrimaryTextColor() {
        EstiloAppModel model = Constants.databaseManager.estiloAppManager.getEstiloAppFromDatabase();
        if (model.getPrimaryTextColor().length() == 0) {
            return Color.parseColor("#000000");
        }

        return Color.parseColor(model.getPrimaryTextColor());
    }

    public static int getSecondaryTextColor() {
        EstiloAppModel model = Constants.databaseManager.estiloAppManager.getEstiloAppFromDatabase();
        if (model.getSecondaryTextColor().length() == 0) {
            return Color.parseColor("#8E8E93");
        }

        return Color.parseColor(model.getSecondaryTextColor());
    }

    public static int getBackgroundColor() {
        EstiloAppModel model = Constants.databaseManager.estiloAppManager.getEstiloAppFromDatabase();
        if (model.getBackgroundColor().length() == 0) {
            return Color.parseColor("#F2F2F7");
        }

        return Color.parseColor(model.getBackgroundColor());
    }

    public static int getPrimaryColor() {
        EstiloAppModel model = Constants.databaseManager.estiloAppManager.getEstiloAppFromDatabase();
        if (model.getPrimaryColor().length() == 0) {
            return Color.parseColor("#000000");
        }

        return Color.parseColor(model.getPrimaryColor());
    }

    public static int getSecondaryColor() {
        EstiloAppModel model = Constants.databaseManager.estiloAppManager.getEstiloAppFromDatabase();
        if (model.getSecondaryColor().length() == 0) {
            return Color.parseColor("#8E8E93");
        }

        return Color.parseColor(model.getSecondaryColor());
    }

    public static int getNavigationColor() {
        EstiloAppModel model = Constants.databaseManager.estiloAppManager.getEstiloAppFromDatabase();
        if (model.getNavigationColor().length() == 0) {
            return Color.parseColor("#F2F2F7");
        }

        return Color.parseColor(model.getNavigationColor());
    }

    public static String getAppName() {
        EstiloAppModel model = Constants.databaseManager.estiloAppManager.getEstiloAppFromDatabase();
        if (model.getAppName().length() == 0) {
            return "Cuenta";
        }

        return model.getAppName();
    }

    public static String getAppSmallIcon() {
        EstiloAppModel model = Constants.databaseManager.estiloAppManager.getEstiloAppFromDatabase();
        if (model.getAppSmallIcon().length() == 0) {
            return "";
        }
        //TODO
        //return model.getAppSmallIcon();
        return "";
    }

    public static void setStatusBarColor(Activity activity) {
        activity.getWindow().setStatusBarColor(getNavigationColor());
    }
}

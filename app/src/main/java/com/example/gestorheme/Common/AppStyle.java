package com.example.gestorheme.Common;

import android.content.Context;
import android.graphics.Color;

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
}

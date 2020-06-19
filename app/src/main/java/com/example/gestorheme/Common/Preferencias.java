package com.example.gestorheme.Common;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {
    private static String myPreferencesName = "preferencias";

    public static void savePasswordInSharedPreferences(Context contexto, String password) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.preferencesPasswordKey, password);
        editor.commit();
    }

    public static String getPasswordFromSharedPreferences(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.getString(Constants.preferencesPasswordKey, null);
    }

    public static boolean checkPasswordInSharedPreferences(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.contains(Constants.preferencesPasswordKey);
    }

    public static void saveTokenInSharedPreferences(Context contexto, String token) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.preferencesTokenKey, token);
        editor.commit();
    }

    public static String getTokenFromSharedPreferences(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.getString(Constants.preferencesTokenKey, null);
    }

    public static boolean checkTokenInSharedPreferences(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.contains(Constants.preferencesTokenKey);
    }

    public static void saveComercioIdInSharedPreferences(Context contexto, long comercioId) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(Constants.preferencesComercioIdKey, comercioId);
        editor.commit();
    }

    public static long getComercioIdFromSharedPreferences(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.getLong(Constants.preferencesComercioIdKey, 0);
    }

    public static boolean checkComercioIdInSharedPreferences(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.contains(Constants.preferencesComercioIdKey);
    }

    public static void saveFondoLogin(Context contexto, String fondoLogin) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.preferencesFondoLoginKey, fondoLogin);
        editor.commit();
    }

    public static void savePrimaryColorLogin(Context contexto, String primaryColor) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.preferencesPrimaryColorKey, primaryColor);
        editor.commit();
    }

    public static void savePrimaryTextColorLogin(Context contexto, String primaryTextColor) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.preferencesPrimaryTextColorKey, primaryTextColor);
        editor.commit();
    }

    public static void saveSecondaryTextColorLogin(Context contexto, String secondaryTextColor) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.preferencesSecondaryTextColorKey, secondaryTextColor);
        editor.commit();
    }

    public static void saveIconoAppLogin(Context contexto, String iconoApp) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.preferencesIconoAppKey, iconoApp);
        editor.commit();
    }

    public static void saveNombreAppLogin(Context contexto, String nombreApp) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.preferencesNombreAppKey, nombreApp);
        editor.commit();
    }

    public static boolean checkFondoLogin(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.contains(Constants.preferencesFondoLoginKey);
    }

    public static boolean checkPrimaryColor(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.contains(Constants.preferencesPrimaryColorKey);
    }

    public static boolean checkPrimaryTextColor(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.contains(Constants.preferencesPrimaryTextColorKey);
    }

    public static boolean checkSecondaryTextColor(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.contains(Constants.preferencesSecondaryTextColorKey);
    }

    public static boolean checkIconoApp(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.contains(Constants.preferencesIconoAppKey);
    }

    public static boolean checkNombreApp(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.contains(Constants.preferencesNombreAppKey);
    }

    public static String getFondoLogin(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.getString(Constants.preferencesFondoLoginKey, "");
    }

    public static String getPrimaryColor(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.getString(Constants.preferencesPrimaryColorKey, "");
    }

    public static String getPrimaryTextColor(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.getString(Constants.preferencesPrimaryTextColorKey, "");
    }

    public static String getSecondaryTextColor(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.getString(Constants.preferencesSecondaryTextColorKey, "");
    }

    public static String getIconoApp(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.getString(Constants.preferencesIconoAppKey, "");
    }

    public static String getNombreApp(Context contexto) {
        SharedPreferences pref = contexto.getSharedPreferences(myPreferencesName, 0);
        return pref.getString(Constants.preferencesNombreAppKey, "");
    }

    public static void eliminarTodasLasPreferencias(Context contexto) {
        SharedPreferences settings = contexto.getSharedPreferences(myPreferencesName, 0);
        settings.edit().clear().commit();
    }

}

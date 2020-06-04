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

}

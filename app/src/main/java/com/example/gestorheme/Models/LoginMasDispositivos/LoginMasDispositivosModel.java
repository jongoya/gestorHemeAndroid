package com.example.gestorheme.Models.LoginMasDispositivos;

import com.example.gestorheme.Models.Dispositivo.DispositivoModel;
import com.example.gestorheme.Models.Login.LoginModel;

import java.io.Serializable;
import java.util.ArrayList;

public class LoginMasDispositivosModel implements Serializable {
    private LoginModel login;
    private ArrayList<DispositivoModel> dispositivos;

    public LoginMasDispositivosModel(LoginModel login, ArrayList<DispositivoModel> dispositivos) {
        this.login = login;
        this.dispositivos = dispositivos;
    }

    public LoginModel getLogin() {
        return login;
    }

    public void setLogin(LoginModel login) {
        this.login = login;
    }

    public ArrayList<DispositivoModel> getDispositivos() {
        return dispositivos;
    }

    public void setDispositivos(ArrayList<DispositivoModel> dispositivos) {
        this.dispositivos = dispositivos;
    }
}

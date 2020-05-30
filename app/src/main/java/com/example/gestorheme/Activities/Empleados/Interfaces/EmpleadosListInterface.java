package com.example.gestorheme.Activities.Empleados.Interfaces;

public interface EmpleadosListInterface {
    void showErrorMessage(String message);
    void showLoadingState();
    void hideLoadingState();
    void reloadList();
}

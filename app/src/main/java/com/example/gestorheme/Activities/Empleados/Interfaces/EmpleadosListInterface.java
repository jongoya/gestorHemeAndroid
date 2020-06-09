package com.example.gestorheme.Activities.Empleados.Interfaces;

public interface EmpleadosListInterface {
    void showErrorMessage(String message);
    void showLoadingState(String description);
    void hideLoadingState();
    void reloadList();
    void logout();
}

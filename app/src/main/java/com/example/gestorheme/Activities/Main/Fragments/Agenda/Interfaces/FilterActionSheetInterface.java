package com.example.gestorheme.Activities.Main.Fragments.Agenda.Interfaces;

import com.example.gestorheme.Models.Empleados.EmpleadoModel;

public interface FilterActionSheetInterface {
    void empleadoSelected(EmpleadoModel empleado);
    void cancelarClicked();
    void todosButtonClicked();
}

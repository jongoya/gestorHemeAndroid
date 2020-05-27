package com.example.gestorheme.Activities.Main.Fragments.Agenda.Interfaces;

public interface ServiceItemViewInterface {
    void serviceRemoved();
    void showLoadingState();
    void hideLoadingState();
    void showErrorMessage(String message);
}

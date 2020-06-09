package com.example.gestorheme.Activities.Main.Fragments.Agenda.Interfaces;

public interface ServiceItemViewInterface {
    void serviceRemoved();
    void showLoadingState(String description);
    void hideLoadingState();
    void showErrorMessage(String message);
    void logout();
}

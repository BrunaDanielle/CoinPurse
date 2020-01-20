package com.example.coincash.mvp_create_account.presenter;

import android.content.Context;

import com.example.coincash.mvp_create_account.view.ViewImpl;

public interface PresenterImpl {
    void validationForm(String name, String email, String password);
    Context getContext();
    void setView( ViewImpl view );
    void insertUser();
}

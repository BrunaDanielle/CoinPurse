package com.example.coincash.mvp_create_account.presenter;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.coincash.config.FirebaseConfig;
import com.example.coincash.helper.Base64Custom;
import com.example.coincash.model.UserModel;
import com.example.coincash.mvp_create_account.view.ViewImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class Presenter implements PresenterImpl {
    private UserModel user;
    private ViewImpl view;
    private FirebaseAuth firebaseAuth;

    public Presenter() {
    }

    @Override
    public void validationForm(String name, String email, String password) {
        String msg = "";
        if (!name.isEmpty()) {
            if (!email.isEmpty()) {
                if (!password.isEmpty()) {
                    user = new UserModel();
                    user.setName(name);
                    user.setEmail(email);
                    user.setPassword(password);
                    insertUser();
                } else {
                    msg = "Senha não pode ser em branco!";
                }
            } else {
                msg = "Email não pode ser em branco!";
            }
        } else {
            msg = "Nome não pode ser em branco!";
        }
        view.validationForm(msg);
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void setView(ViewImpl view) {
        this.view = view;
    }

    @Override
    public void insertUser() {
        firebaseAuth = FirebaseConfig.getFirebaseAuth();

        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String idUser = Base64Custom.encodeBase64(user.getEmail());
                            user.setIdUser(idUser);
                            user.saveUser();
                            view.closeView();
                        } else {
                            String error = "";

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                error = "Senha precisa ser maior que seis dígitos!";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                error = "Digite um e-mail válido!";
                            } catch (FirebaseAuthUserCollisionException e) {
                                error = "E-mail já cadastrado!";
                            } catch (Exception e) {
                                error = "Erro ao cadastrar usuário" + e.getMessage();
                                e.printStackTrace();
                            }

                            view.insertUser(error);
                        }
                    }
                });
    }
}

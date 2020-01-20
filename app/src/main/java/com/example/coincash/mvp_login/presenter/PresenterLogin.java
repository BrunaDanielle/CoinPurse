package com.example.coincash.mvp_login.presenter;

import androidx.annotation.NonNull;
import com.example.coincash.config.FirebaseConfig;
import com.example.coincash.model.UserModel;
import com.example.coincash.mvp_login.view.ViewLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class PresenterLogin implements PresenterImplLogin {
    private UserModel user;
    private FirebaseAuth firebaseAuth;
    private ViewLogin view;

    public PresenterLogin(ViewLogin view) {
        this.view = view;
    }

    @Override
    public void validationForm(String email, String password) {
        String msg = "";
        if (!email.isEmpty()) {
            if (!password.isEmpty()) {
                user = new UserModel();
                user.setEmail(email);
                user.setPassword(password);
                userLogin();
            } else {
               msg = "Senha não pode ser em branco!";
            }
        } else {
            msg = "Email não pode ser em branco!";
        }

        view.validationForm(msg);
    }

    @Override
    public void userLogin() {
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                  view.openPrincipalView();
                }else{
                    String error = "";

                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        error = "E-mail não cadastrado!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        error = "E-mail e senha não correspondem!";
                    }catch (Exception e){
                        error = "Erro ao Logar usuário" + e.getMessage();
                        e.printStackTrace();
                    }
                   view.validationForm(error);
                }
            }
        });
    }
}

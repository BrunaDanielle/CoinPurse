package com.example.coincash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.coincash.R;
import com.example.coincash.config.FirebaseConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_orange_dark)
                .fragment(R.layout.intro_1)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_orange_dark)
                .fragment(R.layout.intro_2)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_orange_dark)
                .fragment(R.layout.intro_3)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_orange_dark)
                .fragment(R.layout.intro_4)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_orange_dark)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .build());
    }

    public void btnCreateAccount(View view) {
        startActivity(new Intent(this, CreateAccountActivity.class));
    }

    public void btnLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void verifyUserLogin(){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        if(firebaseAuth.getCurrentUser() != null){
            openPrincipalView();
        }
    }

    public void openPrincipalView(){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        verifyUserLogin();
    }
}

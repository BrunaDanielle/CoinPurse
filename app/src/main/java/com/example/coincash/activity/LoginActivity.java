package com.example.coincash.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.coincash.R;
import com.example.coincash.mvp_login.presenter.PresenterImplLogin;
import com.example.coincash.mvp_login.presenter.PresenterLogin;
import com.example.coincash.mvp_login.view.ViewLogin;

public class LoginActivity extends AppCompatActivity implements ViewLogin {

    private EditText email, senha;
    private Button btnLogin;
    private PresenterImplLogin presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editEmailLogin);
        senha = findViewById(R.id.editSenhaLogin);
        btnLogin = findViewById(R.id.btnLogin);

        if(presenter == null){
            presenter = new PresenterLogin(this);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUser = email.getText().toString();
                String password = senha.getText().toString();

                presenter.validationForm(emailUser, password);
            }
        });
    }

    @Override
    public void validationForm(String msg) {
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openPrincipalView(){
        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
        startActivity(intent);
        finish();
    }
}

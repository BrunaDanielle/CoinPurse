package com.example.coincash.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coincash.R;
import com.example.coincash.mvp_create_account.presenter.Presenter;
import com.example.coincash.mvp_create_account.presenter.PresenterImpl;
import com.example.coincash.mvp_create_account.view.ViewImpl;

public class CreateAccountActivity extends AppCompatActivity implements ViewImpl {
    private EditText nameUser, emailUser, passwordUser;
    private Button btnInsertUser;
    private static PresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        nameUser = findViewById(R.id.editName);
        emailUser = findViewById(R.id.editEmail);
        passwordUser = findViewById(R.id.editPassword);
        btnInsertUser = findViewById(R.id.btnInsertUser);

        if (presenter == null) {
            presenter = new Presenter();
        }
        presenter.setView(this);

        btnInsertUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameUser.getText().toString();
                String email = emailUser.getText().toString();
                String password = passwordUser.getText().toString();

                presenter.validationForm(name, email, password);
            }
        });
    }

    @Override
    public void validationForm(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void insertUser(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeView() {
        finish();
    }
}

package com.example.coincash.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.coincash.R;
import com.example.coincash.helper.DateCustom;
import com.example.coincash.mvp_receitas.presenter.PresenterImplReceita;
import com.example.coincash.mvp_receitas.presenter.PresenterReceita;
import com.example.coincash.mvp_receitas.view.ViewReceitas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

public class ReceitasActivity extends AppCompatActivity implements ViewReceitas {
    private EditText balance;
    private TextInputLayout date, category, description;
    private PresenterImplReceita presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (presenter == null) {
            presenter = new PresenterReceita();
        }

        balance = findViewById(R.id.editBalance);
        date = findViewById(R.id.editDate);
        category = findViewById(R.id.editCategory);
        description = findViewById(R.id.editDescription);
        FloatingActionButton fab = findViewById(R.id.fabSaveReceita);
        date.getEditText().setText(DateCustom.dateAtual());

        presenter.recuperarReceitaTotal();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReceita(v);
            }
        });
    }

    @Override
    public void saveReceita(View view) {
        if (validarCampos()) {
            Double valorRecuperado = Double.parseDouble(balance.getText().toString());
            String date1 = date.getEditText().getText().toString();
            String category1 = category.getEditText().getText().toString();
            String desc = description.getEditText().getText().toString();

            presenter.saveReceita(category1, date1, desc, valorRecuperado);

            cleanCampos();
        } else {
            Toast.makeText(this, "Todos os campos precisam ser preenchidos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public Boolean validarCampos() {
        String balanceCampo = balance.getText().toString();
        String dateCmapo = date.getEditText().getText().toString();
        String categoryCampo = category.getEditText().getText().toString();
        String descriptionCampo = description.getEditText().getText().toString();

        return !balanceCampo.isEmpty() && !dateCmapo.isEmpty() && !categoryCampo.isEmpty() && !descriptionCampo.isEmpty();
    }

    @Override
    public void cleanCampos() {
        balance.setText("");
        category.getEditText().setText("");
        date.getEditText().setText("");
        description.getEditText().setText("");
    }
}


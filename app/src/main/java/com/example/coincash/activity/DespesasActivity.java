package com.example.coincash.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.example.coincash.R;
import com.example.coincash.helper.DateCustom;
import com.example.coincash.mvp_despesas.presenter.PresenterDespesas;
import com.example.coincash.mvp_despesas.presenter.PresenterImplDespesa;
import com.example.coincash.mvp_despesas.view.ViewImplDespesa;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;

public class DespesasActivity extends AppCompatActivity implements ViewImplDespesa {

    private EditText balance;
    private TextInputLayout date, category, description;
    private FloatingActionButton fab;
    private Double valorRecuperado;
    private PresenterImplDespesa presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(presenter == null){
            presenter = new PresenterDespesas();
        }

        balance = findViewById(R.id.editBalance);
        date = findViewById(R.id.editDate);
        category = findViewById(R.id.editCategory);
        description = findViewById(R.id.editDescription);
        fab = findViewById(R.id.fabSaveDespesa);
        date.getEditText().setText(DateCustom.dateAtual());

        presenter.recuperarDespesaTotal();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDespesa(v);
            }
        });
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
    public void saveDespesa(View view) {
        if (validarCampos()) {
            valorRecuperado = Double.parseDouble(balance.getText().toString());
            String date1 = date.getEditText().getText().toString();
            String category1 = category.getEditText().getText().toString();
            String desc = description.getEditText().getText().toString();

            presenter.saveDespesa(category1, date1, desc, valorRecuperado);

            cleanCampos();
        } else {
            Toast.makeText(this, "Todos os campos precisam ser preenchidos", Toast.LENGTH_SHORT).show();
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(category.getEditText()).setText("");
        }
        date.getEditText().setText("");
        description.getEditText().setText("");
    }

}

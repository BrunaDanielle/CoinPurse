package com.example.coincash.mvp_receitas.view;

import android.view.View;

public interface ViewReceitas {
    void saveReceita(View view);
    Boolean validarCampos();
    void cleanCampos();
}

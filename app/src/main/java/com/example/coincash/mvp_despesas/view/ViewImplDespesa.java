package com.example.coincash.mvp_despesas.view;

import android.view.View;

public interface ViewImplDespesa {
    void saveDespesa(View view);
    Boolean validarCampos();
    void cleanCampos();
}

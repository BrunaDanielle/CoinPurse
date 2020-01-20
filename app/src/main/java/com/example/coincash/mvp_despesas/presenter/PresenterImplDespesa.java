package com.example.coincash.mvp_despesas.presenter;

public interface PresenterImplDespesa {
    void saveDespesa(String category, String date, String description, Double value);
    void recuperarDespesaTotal();
    void atualizarDespesa(Double despesa);
}

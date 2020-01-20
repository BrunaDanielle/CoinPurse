package com.example.coincash.mvp_receitas.presenter;

public interface PresenterImplReceita {
    void saveReceita(String category, String date, String description, Double value);
    void recuperarReceitaTotal();
    void atualizarReceita(Double receita);
}

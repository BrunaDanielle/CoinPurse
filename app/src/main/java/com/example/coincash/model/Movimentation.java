package com.example.coincash.model;

import com.example.coincash.config.FirebaseConfig;
import com.example.coincash.helper.Base64Custom;
import com.example.coincash.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentation {
    private String category;
    private String data;
    private String descricao;
    private String type;
    private Double value;
    private String key;

    public Movimentation() {
    }

    public void saveMovimentation(String date) {
        FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
        String idUser = Base64Custom.encodeBase64(firebaseAuth.getCurrentUser().getEmail());
        String monthYear = DateCustom.monthAndYear(date);
        DatabaseReference reference = FirebaseConfig.getFirebaseDatabase();
        reference.child("movimentacao")
                .child(idUser)
                .child(monthYear)
                .push()
                .setValue(this);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}

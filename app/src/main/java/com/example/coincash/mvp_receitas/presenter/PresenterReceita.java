package com.example.coincash.mvp_receitas.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.coincash.config.FirebaseConfig;
import com.example.coincash.helper.Base64Custom;
import com.example.coincash.model.Movimentation;
import com.example.coincash.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PresenterReceita implements PresenterImplReceita {
    private Movimentation movimentation;
    private DatabaseReference firebaseRef = FirebaseConfig.getFirebaseDatabase();
    private FirebaseAuth auth = FirebaseConfig.getFirebaseAuth();
    private Double receitaTotal;


    @Override
    public void saveReceita(String category, String date, String description, Double value) {
        movimentation = new Movimentation();
        movimentation.setValue(value);
        movimentation.setCategory(category);
        movimentation.setData(date);
        movimentation.setDescricao(description);
        movimentation.setType("r");

        Double receitaAtualizada = receitaTotal + value;
        atualizarReceita(receitaAtualizada);

        movimentation.saveMovimentation(date);
    }

    @Override
    public void recuperarReceitaTotal() {

        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = null;
        if (emailUser != null) {
            idUser = Base64Custom.encodeBase64(emailUser);
        }

        DatabaseReference users = null;
        if (idUser != null) {
            users = firebaseRef.child("usuarios").child(idUser);
        }

        if (users != null) {
            users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    if (user != null) {
                        receitaTotal = user.getReceitaTotal();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("DatabaseError", "recuperar receita: " + databaseError);
                }
            });
        }
    }

    @Override
    public void atualizarReceita(Double receita) {

        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = null;
        if (emailUser != null) {
            idUser = Base64Custom.encodeBase64(emailUser);
        }
        DatabaseReference users = null;
        if (idUser != null) {
            users = firebaseRef.child("usuarios").child(idUser);
        }

        if (users != null) {
            users.child("receitaTotal").setValue(receita);
        }
    }
}

package com.example.coincash.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.coincash.R;
import com.example.coincash.adapter.AdapterMovimentacao;
import com.example.coincash.config.FirebaseConfig;
import com.example.coincash.helper.Base64Custom;
import com.example.coincash.model.Movimentation;
import com.example.coincash.model.UserModel;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView saudacao, saldo;
    private DatabaseReference firebaseRef = FirebaseConfig.getFirebaseDatabase();
    private DatabaseReference movimentationRef;
    private FirebaseAuth auth = FirebaseConfig.getFirebaseAuth();
    private DatabaseReference users;
    private ValueEventListener valueEventListener;
    private ValueEventListener valueEventListenerMovimentation;
    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentation> movimentationList = new ArrayList<>();
    private String monthYear;
    private Movimentation movimentation;
    private Double receitaTotal;
    private Double despesaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        calendarView = findViewById(R.id.calendarView);
        saudacao = findViewById(R.id.textSaudacao);
        saldo = findViewById(R.id.textSaldo);
        recyclerView = findViewById(R.id.recyclerMovimentos);

        configuraCalendario();
        swipe();

        adapterMovimentacao = new AdapterMovimentacao(movimentationList, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);
    }

    public void swipe() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir movimentação da conta");
        alertDialog.setMessage("Você tem certeza que deseja excluir esse item?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                movimentation = movimentationList.get(position);
                String idUser = Base64Custom.encodeBase64(auth.getCurrentUser().getEmail());
                movimentationRef = firebaseRef.child("movimentacao")
                        .child(idUser)
                        .child(monthYear);
                movimentationRef.child(movimentation.getKey()).removeValue();
                adapterMovimentacao.notifyItemRemoved(position);
                atualizarSaldo();

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                adapterMovimentacao.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void atualizarSaldo(){
        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = null;
        if (emailUser != null) {
            idUser = Base64Custom.encodeBase64(emailUser);
        }

        if (idUser != null) {
            users = firebaseRef.child("usuarios").child(idUser);
        }

        if(movimentation.getType().equals("r")){
            receitaTotal = receitaTotal - movimentation.getValue();
            users.child("receitaTotal").setValue(receitaTotal);
        }

        if(movimentation.getType().equals("d")){
            despesaTotal = despesaTotal - movimentation.getValue();
            receitaTotal = receitaTotal + movimentation.getValue();
            users.child("despesaTotal").setValue(despesaTotal);
            users.child("receitaTotal").setValue(receitaTotal);
        }
    }

    public void popularLista() {
        String idUser = Base64Custom.encodeBase64(auth.getCurrentUser().getEmail());
        movimentationRef = firebaseRef.child("movimentacao")
                .child(idUser)
                .child(monthYear);

        valueEventListenerMovimentation = movimentationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movimentationList.clear();

                //percorre por todos os nós
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Movimentation movi = dados.getValue(Movimentation.class);
                    movi.setKey(dados.getKey());
                    movimentationList.add(movi);
                }
                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        popularLista();
    }

    @Override
    protected void onStop() {
        super.onStop();
        users.removeEventListener(valueEventListener);
        movimentationRef.removeEventListener(valueEventListenerMovimentation);
    }

    public void recuperarResumo() {
        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = null;
        if (emailUser != null) {
            idUser = Base64Custom.encodeBase64(emailUser);
        }

        if (idUser != null) {
            users = firebaseRef.child("usuarios").child(idUser);
        }

        valueEventListener = users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel user = dataSnapshot.getValue(UserModel.class);
                if (user != null) {
                    despesaTotal = user.getDespesaTotal();
                    receitaTotal = user.getReceitaTotal();
                    saldo.setText(String.valueOf(receitaTotal));
                }
                if (user != null) {
                    saudacao.setText("Bem-vindo(a) " + user.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSair:
                FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
                firebaseAuth.signOut();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void configuraCalendario() {
        CharSequence meses[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        calendarView.setTitleMonths(meses);

        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(2018, 1, 1))
                .commit();

        CalendarDay dataAtual = calendarView.getCurrentDate();
        String mesModificado = String.format("%02d", dataAtual.getMonth());
        monthYear = (mesModificado + "" + dataAtual.getYear());

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesModificado = String.format("%02d", date.getMonth());
                monthYear = (mesModificado + "" + date.getYear());
                movimentationRef.removeEventListener(valueEventListenerMovimentation);
                popularLista();
            }
        });
    }

    public void addReceita(View view) {
        startActivity(new Intent(this, ReceitasActivity.class));
    }

    public void addDespesa(View view) {
        startActivity(new Intent(this, DespesasActivity.class));
    }
}


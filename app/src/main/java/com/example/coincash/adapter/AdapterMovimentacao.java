package com.example.coincash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coincash.R;
import com.example.coincash.model.Movimentation;

import java.util.List;

public class AdapterMovimentacao extends RecyclerView.Adapter<AdapterMovimentacao.MyViewHolder> {

    List<Movimentation> movimentations;
    Context context;

    public AdapterMovimentacao(List<Movimentation> movimentations, Context context) {
        this.movimentations = movimentations;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movimentacao, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMovimentacao.MyViewHolder holder, int position) {
        Movimentation movimentation = movimentations.get(position);

        holder.titulo.setText(movimentation.getDescricao());
        holder.valor.setText(String.valueOf(movimentation.getValue()));
        holder.categoria.setText(movimentation.getCategory());

        if (movimentation.getType().equals("d")) {
            holder.valor.setTextColor(context.getResources().getColor(R.color.colorAccentDespesa));
            holder.valor.setText("-" + movimentation.getValue());
        } else {
            holder.valor.setTextColor(context.getResources().getColor(R.color.colorAccentReceita));
            holder.valor.setText("+" + movimentation.getValue());
        }
    }

    @Override
    public int getItemCount() {
        return movimentations.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, valor, categoria;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textAdapterTitulo);
            valor = itemView.findViewById(R.id.textAdapterValor);
            categoria = itemView.findViewById(R.id.textAdapterCategoria);
        }
    }
}

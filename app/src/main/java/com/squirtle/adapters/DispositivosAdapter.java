package com.squirtle.adapters;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squirtle.R;
import com.squirtle.activities.DispositivoDetailsActivity;
import com.squirtle.model.Dispositivo;
import com.squirtle.model.UsuarioLogado;
import com.squirtle.utils.GeneralUtils;
import com.squirtle.viewholders.DispositivosViewHolder;

import java.util.ArrayList;

public class DispositivosAdapter extends RecyclerView.Adapter<DispositivosViewHolder>{

    ArrayList<Dispositivo> dispositivoFullList;
    UsuarioLogado usuarioLogado;


    public DispositivosAdapter(ArrayList<Dispositivo> dispositivoList, UsuarioLogado usuarioLogado) {
        dispositivoFullList = new ArrayList<>(dispositivoList);
        this.usuarioLogado = usuarioLogado;
    }

    public void updateList(Dispositivo dispositivo) {
        insertItem(dispositivo);
    }

    private void insertItem(Dispositivo dispositivo) {
        dispositivoFullList.add(dispositivo);
        notifyItemInserted(getItemCount());
    }

    public void removeItem(int position) {
        notifyItemRemoved(position);
    }


    @NonNull
    @Override
    public DispositivosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DispositivosViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_dispositivos, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DispositivosViewHolder holder, int position) {
        Dispositivo dispositivoItem = dispositivoFullList.get(position);
        holder.dispositivo_nome.setText(String.format("%s", dispositivoItem.getNome()));
        holder.dispositivo_status.setText(String.format("%s", dispositivoItem.getStatus()));
        holder.dispositivo_descricao.setText(String.format("%s", dispositivoItem.getDescricao()));
        int icon = GeneralUtils.get_icon(dispositivoItem.getIcone());
        holder.dispositivo_icon.setImageResource(icon);

        holder.dispositivo_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Intent i = new Intent(v.getContext(), DispositivoDetailsActivity.class);
                i.putExtra("dispositivo", dispositivoItem);
                i.putExtra("usuarioLogado", usuarioLogado);
                v.getContext().startActivity(i);
            }
        });


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return dispositivoFullList.size();
    }

}
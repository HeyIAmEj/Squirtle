package com.squirtle.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squirtle.R;

public class DispositivosViewHolder extends RecyclerView.ViewHolder {

    public TextView dispositivo_nome, dispositivo_status, dispositivo_descricao;
    public CardView dispositivo_view;
    public ImageView dispositivo_icon;

    public DispositivosViewHolder(@NonNull View itemView) {
        super(itemView);
        dispositivo_view = itemView.findViewById(R.id.dispositivo_view);
        dispositivo_icon = itemView.findViewById(R.id.dispositivo_icon);
        dispositivo_nome = itemView.findViewById(R.id.dispositivo_nome);
        dispositivo_descricao = itemView.findViewById(R.id.dispositivo_descricao);
        dispositivo_status = itemView.findViewById(R.id.dispositivo_status);
    }


}
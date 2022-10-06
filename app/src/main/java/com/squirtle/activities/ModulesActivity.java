package com.squirtle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squirtle.R;
import com.squirtle.model.Usuario;
import com.squirtle.model.UsuarioLogado;

public class ModulesActivity extends AppCompatActivity {
    private CardView account_card;
    private CardView devices_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);


        Bundle extras = getIntent().getExtras();
        UsuarioLogado usuarioLogado = (UsuarioLogado) extras.get("usuarioLogado");

        account_card = findViewById(R.id.account_card);
        devices_card = findViewById(R.id.devices_card);

        account_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Abrir activity de conta", Toast.LENGTH_SHORT).show();
            }
        });

        devices_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DispositivosActivity.class);
                intent.putExtra("usuarioLogado", usuarioLogado);
                startActivity(intent);
            }
        });




    }
}
package com.squirtle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squirtle.R;
import com.squirtle.adapters.DispositivosAdapter;
import com.squirtle.databinding.ActivityDispositivosBinding;
import com.squirtle.databinding.ActivityMainBinding;
import com.squirtle.model.Dispositivo;
import com.squirtle.model.UsuarioLogado;
import com.squirtle.utils.JWTUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import mobi.stos.httplib.HttpAsync;
import mobi.stos.httplib.inter.FutureCallback;

public class DispositivosActivity  extends AppCompatActivity {

    private ActivityDispositivosBinding binding;
    RecyclerView mRecyclerView;
    LinearLayoutManager layoutManager;
    ArrayList<Dispositivo> dispositivoList;
    DispositivosAdapter dispositivosAdapter;
    UsuarioLogado usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        usuarioLogado = (UsuarioLogado) extras.get("usuarioLogado");

        binding = ActivityDispositivosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getMeusDispositivos();

    }

    private void setupRecycler() {
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.lista_dispositivos);
        mRecyclerView.setLayoutManager(layoutManager);
        dispositivosAdapter = new DispositivosAdapter(dispositivoList);

        mRecyclerView.setAdapter(dispositivosAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    public void getMeusDispositivos(){
        ArrayList<Dispositivo> listaDispositivos = new ArrayList();
        try {
            HttpAsync httpAsync = new HttpAsync(new URL(
                    getString(R.string.squirtle_api) + getString(R.string.squirtle_api_endpoint) +
                            getString(R.string.squirtle_api_endpoint_usuario, usuarioLogado.getId())));
            httpAsync.setDebug(true);
            httpAsync.addHeader("Authorization", "Bearer " + usuarioLogado.getJwt());
            httpAsync.get(new FutureCallback() {
                @Override
                public void onBeforeExecute() {

                }

                @Override
                public void onAfterExecute() {
                    dispositivoList = listaDispositivos;
                    setupRecycler();
                }

                @Override
                public void onSuccess(int responseCode, Object object) {
                    switch (responseCode) {
                        case 200:
                            JSONObject jsonObject = (JSONObject) object;
                            try {
                                JSONArray jsonArray = (jsonObject.getJSONArray("dispositivos"));
                                for (int i = 0; i < jsonArray.length(); i++){
                                    Dispositivo d = new Dispositivo();
                                    d.setId(jsonArray.getJSONObject(i).getInt("id"));
                                    d.setNome(jsonArray.getJSONObject(i).getString("nome"));
                                    d.setStatus(jsonArray.getJSONObject(i).getString("status"));
                                    d.setIcone(jsonArray.getJSONObject(i).getString("icone"));
                                    d.setWifi_ssid(jsonArray.getJSONObject(i).getString("wifi_ssid"));
                                    d.setWifi_pass(jsonArray.getJSONObject(i).getString("wifi_pass"));
                                    d.setSensor1(jsonArray.getJSONObject(i).getString("sensor1"));
                                    d.setSensor2(jsonArray.getJSONObject(i).getString("sensor2"));
                                    d.setSensor3(jsonArray.getJSONObject(i).getString("sensor3"));
                                    d.setSensor4(jsonArray.getJSONObject(i).getString("sensor4"));
                                    d.setSensor5(jsonArray.getJSONObject(i).getString("sensor5"));
                                    listaDispositivos.add(d);
                                }

                                System.out.println(listaDispositivos);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println();
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "Response code não tratado", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(getApplicationContext(), "Falha no request", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}

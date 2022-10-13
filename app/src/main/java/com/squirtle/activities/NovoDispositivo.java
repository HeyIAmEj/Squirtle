package com.squirtle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.squirtle.R;
import com.squirtle.model.UsuarioLogado;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import mobi.stos.httplib.HttpAsync;
import mobi.stos.httplib.inter.FutureCallback;

public class NovoDispositivo extends AppCompatActivity {
    UsuarioLogado usuarioLogado;

    private AutoCompleteTextView autoCompleteTextView;
    private Button submitDevice;
    private EditText nomeText, descricaoText, wifi_ssid_text, wifi_pass_text;
    private SwitchMaterial switch_status;
    private Slider time_slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        usuarioLogado = (UsuarioLogado) extras.get("usuarioLogado");

        setContentView(R.layout.activity_novo_dispositivo);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        String[] tipos_solo = getResources().getStringArray(R.array.novodispositivo_tipo_solo_menu);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, R.layout.item_view, R.id.itemTextView, tipos_solo);
        autoCompleteTextView.setAdapter(arrayAdapter);

        submitForm();
    }

    public void submitForm(){
        nomeText = findViewById(R.id.nomeText);
        descricaoText = findViewById(R.id.descricaoText);
        switch_status = findViewById(R.id.switch_status);
        wifi_ssid_text = findViewById(R.id.wifi_ssid_text);
        wifi_pass_text = findViewById(R.id.wifi_pass_text);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        time_slider = findViewById(R.id.time_slider);
        
        
        submitDevice = findViewById(R.id.submitDevice);
        submitDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDevice();
            }
        });

    }


    public void postDevice(){
            try {
                HttpAsync httpAsync = new HttpAsync(new URL(
                        getString(R.string.squirtle_api) + getString(R.string.squirtle_api_endpoint) +
                                getString(R.string.squirtle_api_endpoint_dispositivo)));
                httpAsync.setDebug(true);
                httpAsync.addHeader("Authorization", "Bearer " + usuarioLogado.getJwt());

                httpAsync.addParam("nome", nomeText.getText());
                httpAsync.addParam("descricao", descricaoText.getText());
                httpAsync.addParam("wifi_ssid", wifi_ssid_text.getText());
                httpAsync.addParam("wifi_pass", wifi_pass_text.getText());
                httpAsync.addParam("icone", "general");
                httpAsync.addParam("status", switch_status.isChecked());
                httpAsync.addParam("tempo_bomba", time_slider.getValue());
                httpAsync.addParam("tipo_solo", autoCompleteTextView.getText());

                httpAsync.post(new FutureCallback() {
                    @Override
                    public void onBeforeExecute() {
//                        disableForm();
                    }

                    @Override
                    public void onAfterExecute() {

                        Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        startActivity(intent);
//                        enableForm();
                    }

                    @Override
                    public void onSuccess(int responseCode, Object object) {
                        switch (responseCode) {
                            case 200:
                                JSONObject jsonObject = (JSONObject) object;
                                try {
                                    System.out.println("chegou");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                System.out.println();
                                break;
                            case 500:
                                // usuario com id já cadastrado
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


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
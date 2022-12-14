package com.squirtle.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.squirtle.R;
import com.squirtle.model.Dispositivo;
import com.squirtle.model.UsuarioLogado;
import com.squirtle.utils.LogoutUtils;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import mobi.stos.httplib.HttpAsync;
import mobi.stos.httplib.inter.FutureCallback;

public class NovoDispositivo extends AppCompatActivity {
    UsuarioLogado usuarioLogado;

    private AutoCompleteTextView autoCompleteTextView;
    private Button submitDevice;
    private TextView wifi_alert;
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

        findViews();

        setTestContent();

        String[] tipos_solo = getResources().getStringArray(R.array.novodispositivo_tipo_solo_menu);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, R.layout.item_view, R.id.itemTextView, tipos_solo);
        autoCompleteTextView.setAdapter(arrayAdapter);

    }

    public void setTestContent(){
        nomeText.setText("nometeste");
        descricaoText.setText("descricaoteste");
        wifi_ssid_text.setText("wifiteste");
        wifi_pass_text.setText("senhateste");
        time_slider.setValue(50);
    }

    public void findViews(){
        nomeText = findViewById(R.id.nomeText);
        descricaoText = findViewById(R.id.descricaoText);
        switch_status = findViewById(R.id.switch_status);
        wifi_ssid_text = findViewById(R.id.wifi_ssid_text);
        wifi_pass_text = findViewById(R.id.wifi_pass_text);
        time_slider = findViewById(R.id.time_slider);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        submitDevice = findViewById(R.id.submitDevice);

        submitDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDevice();
            }
        });
    }

    public void postDevice(){
        Dispositivo dispositivoCriado = new Dispositivo();
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
                httpAsync.addParam("tipo_solo", autoCompleteTextView.getText());
                httpAsync.addParam("sensor1", "mp:0,pp:0,pt:0,value:0");
                httpAsync.addParam("sensor2", "mp:0,pp:0,pt:0,value:0");
                httpAsync.addParam("sensor3", "mp:0,pp:0,pt:0,value:0");
                httpAsync.addParam("sensor4", "mp:0,pp:0,pt:0,value:0");
                httpAsync.addParam("sensor5", "mp:0,pp:0,pt:0,value:0");

                httpAsync.post(new FutureCallback() {
                    @Override
                    public void onBeforeExecute() {
                        disableForm();
                    }

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onAfterExecute() {
                        enableForm();

                        if(dispositivoCriado != null){
                            Toast.makeText(getApplicationContext(), "Dispositivo criado com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), IotLinkActivity.class);
                            intent.putExtra("usuarioLogado", usuarioLogado);
                            intent.putExtra("dispositivo", dispositivoCriado);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "N??o foi poss??vel criar seu dispositivo! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("usuarioLogado", usuarioLogado);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onSuccess(int responseCode, Object object) {
                        switch (responseCode) {
                            case 200:
                                JSONObject jsonObject = (JSONObject) object;
                                try {
                                    dispositivoCriado.setId(((JSONObject) object).getInt("id"));
                                    dispositivoCriado.setNome(((JSONObject) object).getString("nome"));
                                    dispositivoCriado.setDescricao(((JSONObject) object).getString("descricao"));
                                    dispositivoCriado.setStatus(((JSONObject) object).getString("status"));
                                    dispositivoCriado.setIcone(((JSONObject) object).getString("icone"));
                                    dispositivoCriado.setWifi_ssid(((JSONObject) object).getString("wifi_ssid"));
                                    dispositivoCriado.setWifi_pass(((JSONObject) object).getString("wifi_pass"));
                                    dispositivoCriado.setTipo_solo(((JSONObject) object).getString("tipo_solo"));
                                    dispositivoCriado.setSensor1(((JSONObject) object).getString("sensor1"));
                                    dispositivoCriado.setSensor2(((JSONObject) object).getString("sensor2"));
                                    dispositivoCriado.setSensor3(((JSONObject) object).getString("sensor3"));
                                    dispositivoCriado.setSensor4(((JSONObject) object).getString("sensor4"));
                                    dispositivoCriado.setSensor5(((JSONObject) object).getString("sensor5"));
                                    System.out.println("cadastrado");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                System.out.println();
                                break;
                            case 500:
                                LogoutUtils.logout(getApplicationContext());
                                // usuario com id j?? cadastrado
//                                Toast.makeText(getApplicationContext(), "Erro 500!", Toast.LENGTH_SHORT).show();
                                System.out.println(object);
                                break;
                            default:
                                LogoutUtils.logout(getApplicationContext());
//                                Toast.makeText(getApplicationContext(), "Response code n??o tratado", Toast.LENGTH_SHORT).show();
                                System.out.println(object);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        LogoutUtils.logout(getApplicationContext());
//                        Toast.makeText(getApplicationContext(), "Falha no request", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

    }

    public void disableForm(){
        nomeText.setEnabled(false);
        descricaoText.setEnabled(false);
        switch_status.setEnabled(false);
        wifi_ssid_text.setEnabled(false);
        wifi_pass_text.setEnabled(false);
        autoCompleteTextView.setEnabled(false);
        time_slider.setEnabled(false);
        submitDevice.setEnabled(false);
    }

    public void enableForm(){
        nomeText.setEnabled(true);
        descricaoText.setEnabled(true);
        switch_status.setEnabled(true);
        wifi_ssid_text.setEnabled(true);
        wifi_pass_text.setEnabled(true);
        autoCompleteTextView.setEnabled(true);
        time_slider.setEnabled(true);
        submitDevice.setEnabled(true);
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
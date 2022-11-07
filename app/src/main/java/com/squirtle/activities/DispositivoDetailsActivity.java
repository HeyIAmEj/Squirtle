package com.squirtle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squirtle.R;
import com.squirtle.databinding.ActivityDispositivoDetailsBinding;
import com.squirtle.databinding.ActivityDispositivosBinding;
import com.squirtle.model.Dispositivo;
import com.squirtle.model.UsuarioLogado;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import mobi.stos.httplib.HttpAsync;
import mobi.stos.httplib.inter.FutureCallback;

public class DispositivoDetailsActivity extends AppCompatActivity {

    Dispositivo dispositivoSelecionado;
    UsuarioLogado usuarioLogado;

    TextView sensor1, sensor2, sensor3, sensor4, sensor5;
    TextView name, status, ssid, pass;
    ProgressBar sensor1_progress, sensor2_progress, sensor3_progress, sensor4_progress, sensor5_progress;

    Timer myTimer;
    TimerTask myTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        dispositivoSelecionado = (Dispositivo) extras.get("dispositivo");
        usuarioLogado = (UsuarioLogado) extras.get("usuarioLogado");


        setContentView(R.layout.activity_dispositivo_details);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.device_detail_toolbar_title, dispositivoSelecionado.getNome()));


        findView();
        setupContent();


    }

    public void runTimer(){
        myTimer = new Timer();
        myTask = new TimerTask() {
            @Override
            public void run () {
                getSensorInfo();
            }
        };
        myTimer.scheduleAtFixedRate(myTask , 0l, 5000);
    }

    public void getSensorInfo(){
        Dispositivo dispositivo = new Dispositivo();
        try {
                HttpAsync httpAsync = new HttpAsync(new URL(
                        getString(R.string.squirtle_api) + getString(R.string.squirtle_api_endpoint) +
                                getString(R.string.squirtle_api_endpoint_dispositivo_id, dispositivoSelecionado.getId())));
                httpAsync.setDebug(true);
                httpAsync.addHeader("Authorization", "Bearer " + usuarioLogado.getJwt());
                httpAsync.get(new FutureCallback() {
                    @Override
                    public void onBeforeExecute() {

                    }

                    @Override
                    public void onAfterExecute() {
                        if(dispositivo != null){
                            Toast.makeText(getApplicationContext(), "Sensores Atualizados!", Toast.LENGTH_SHORT).show();
                            setupContent(dispositivo);
                        }
                    }

                    @Override
                    public void onSuccess(int responseCode, Object object) {
                        switch (responseCode) {
                            case 200:
                                JSONObject jsonObject = (JSONObject) object;
                                try {
                                        dispositivo.setId(jsonObject.getInt("id"));
                                        dispositivo.setNome(jsonObject.getString("nome"));
                                        dispositivo.setDescricao(jsonObject.getString("descricao"));
                                        dispositivo.setStatus(jsonObject.getString("status"));
                                        dispositivo.setIcone(jsonObject.getString("icone"));
                                        dispositivo.setWifi_ssid(jsonObject.getString("wifi_ssid"));
                                        dispositivo.setWifi_pass(jsonObject.getString("wifi_pass"));
                                        dispositivo.settempo_bomba(jsonObject.getString("tempo_bomba"));
                                        dispositivo.setTipo_solo(jsonObject.getString("tipo_solo"));
                                        dispositivo.setSensor1(jsonObject.getString("sensor1").equals("null") ? "0.0" : jsonObject.getString("sensor1"));
                                        dispositivo.setSensor2(jsonObject.getString("sensor2").equals("null") ? "0.0" : jsonObject.getString("sensor2"));
                                        dispositivo.setSensor3(jsonObject.getString("sensor3").equals("null") ? "0.0" : jsonObject.getString("sensor3"));
                                        dispositivo.setSensor4(jsonObject.getString("sensor4").equals("null") ? "0.0" : jsonObject.getString("sensor4"));
                                        dispositivo.setSensor5(jsonObject.getString("sensor5").equals("null") ? "0.0" : jsonObject.getString("sensor5"));

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


    public void findView(){
        // DEVICE VIEW
        name = (TextView) findViewById(R.id.device_name);
        status = (TextView) findViewById(R.id.device_status);
        ssid = (TextView) findViewById(R.id.device_ssid);
        pass = (TextView) findViewById(R.id.device_pass);


        // SENSOR VIEW
        sensor1 = (TextView) findViewById(R.id.sensor1_value);
        sensor1_progress = findViewById(R.id.sensor1_progress);
        sensor2 = (TextView) findViewById(R.id.sensor2_value);
        sensor2_progress = findViewById(R.id.sensor2_progress);
        sensor3 = (TextView) findViewById(R.id.sensor3_value);
        sensor3_progress = findViewById(R.id.sensor3_progress);
        sensor4 = (TextView) findViewById(R.id.sensor4_value);
        sensor4_progress = findViewById(R.id.sensor4_progress);
        sensor5 = (TextView) findViewById(R.id.sensor5_value);
        sensor5_progress = findViewById(R.id.sensor5_progress);
    }


    public void setupContent(){
        // DEVICE INFO
        name.setText(dispositivoSelecionado.getNome());
        status.setText(dispositivoSelecionado.getStatus());
        ssid.setText(dispositivoSelecionado.getWifi_ssid());
        pass.setText(dispositivoSelecionado.getWifi_pass());


        // SENSOR INFO
        sensor1.setText(dispositivoSelecionado.getSensor1());
        sensor1_progress.setProgress((int) (Double.parseDouble(dispositivoSelecionado.getSensor1()) / 1024*100));

        sensor2.setText(dispositivoSelecionado.getSensor2());
        sensor2_progress.setProgress((int) (Double.parseDouble(dispositivoSelecionado.getSensor2()) / 1024*100));

        sensor3.setText(dispositivoSelecionado.getSensor3());
        sensor3_progress.setProgress((int) (Double.parseDouble(dispositivoSelecionado.getSensor3()) / 1024*100));

        sensor4.setText(dispositivoSelecionado.getSensor4());
        sensor4_progress.setProgress((int) (Double.parseDouble(dispositivoSelecionado.getSensor4()) / 1024*100));

        sensor5.setText(dispositivoSelecionado.getSensor5());
        sensor5_progress.setProgress((int) (Double.parseDouble(dispositivoSelecionado.getSensor5()) / 1024*100));
    }

    public void setupContent(Dispositivo dispositivoSelecionado){
        // DEVICE INFO
        name.setText(dispositivoSelecionado.getNome());
        status.setText(dispositivoSelecionado.getStatus());
        ssid.setText(dispositivoSelecionado.getWifi_ssid());
        pass.setText(dispositivoSelecionado.getWifi_pass());


        // SENSOR INFO
        sensor1.setText(dispositivoSelecionado.getSensor1());
        sensor1_progress.setProgress((int) (Double.parseDouble(dispositivoSelecionado.getSensor1()) / 1024*100));

        sensor2.setText(dispositivoSelecionado.getSensor2());
        sensor2_progress.setProgress((int) (Double.parseDouble(dispositivoSelecionado.getSensor2()) / 1024*100));

        sensor3.setText(dispositivoSelecionado.getSensor3());
        sensor3_progress.setProgress((int) (Double.parseDouble(dispositivoSelecionado.getSensor3()) / 1024*100));

        sensor4.setText(dispositivoSelecionado.getSensor4());
        sensor4_progress.setProgress((int) (Double.parseDouble(dispositivoSelecionado.getSensor4()) / 1024*100));

        sensor5.setText(dispositivoSelecionado.getSensor5());
        sensor5_progress.setProgress((int) (Double.parseDouble(dispositivoSelecionado.getSensor5()) / 1024*100));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            System.out.println("Pausando timer de consultas");

            myTimer.cancel();
            myTask.cancel();
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        System.out.println("Pausando timer de consultas");

        myTimer.cancel();
        myTask.cancel();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        System.out.println("Pausando timer de consultas");

        myTimer.cancel();
        myTask.cancel();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        System.out.println("Iniciando Timer de consultas");
        runTimer();

        super.onResume();
    }
}
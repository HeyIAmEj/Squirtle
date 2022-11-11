package com.squirtle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.squirtle.R;
import com.squirtle.databinding.ActivityDispositivoDetailsBinding;
import com.squirtle.databinding.ActivityDispositivosBinding;
import com.squirtle.model.Dispositivo;
import com.squirtle.model.UsuarioLogado;
import com.squirtle.utils.CustomCallback;
import com.squirtle.utils.GeneralUtils;
import com.squirtle.utils.LogoutUtils;

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
    TextView name, status, ssid, pass, descricao, tipo_solo;
    ImageView device_icon;
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
        setupContent(dispositivoSelecionado);

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
                            dispositivoSelecionado = dispositivo;
                            Toast.makeText(getApplicationContext(), "Sensores Atualizados!", Toast.LENGTH_SHORT).show();
                            setupContent(dispositivoSelecionado);
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
                                        dispositivo.setTipo_solo(jsonObject.getString("tipo_solo"));
                                        dispositivo.setSensor1(jsonObject.getString("sensor1").equals("null") ? "0.0" : jsonObject.getString("sensor1"));
                                        dispositivo.setSensor2(jsonObject.getString("sensor2").equals("null") ? "0.0" : jsonObject.getString("sensor2"));
                                        dispositivo.setSensor3(jsonObject.getString("sensor3").equals("null") ? "0.0" : jsonObject.getString("sensor3"));
                                        dispositivo.setSensor4(jsonObject.getString("sensor4").equals("null") ? "0.0" : jsonObject.getString("sensor4"));
                                        dispositivo.setSensor5(jsonObject.getString("sensor5").equals("null") ? "0.0" : jsonObject.getString("sensor5"));

                                } catch (Exception e) {
                                    LogoutUtils.logout(getApplicationContext());
                                    e.printStackTrace();
                                }
                                System.out.println();
                                break;
                            default:
                                LogoutUtils.logout(getApplicationContext());
//                                Toast.makeText(getApplicationContext(), "Response code não tratado", Toast.LENGTH_SHORT).show();
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

    public void alertRemovingDevice(){
        GeneralUtils.alert(DispositivoDetailsActivity.this,
                String.format("Deletar dispositivo %s",
                        dispositivoSelecionado.getNome()),
                "Os usuários que possuem esse dispositivo vinculado, também não terão mais acesso!",
                "Deletar", "Cancelar", new CustomCallback() {
                    @Override
                    public void onCallback() {
                        removeDevice();
                    }
                });
    }

    public void removeDevice(){
        try {
            HttpAsync httpAsync = new HttpAsync(new URL(
                    getString(R.string.squirtle_api) + getString(R.string.squirtle_api_endpoint) +
                            getString(R.string.squirtle_api_endpoint_dispositivo)));
            httpAsync.setDebug(true);
            httpAsync.addHeader("Authorization", "Bearer " + usuarioLogado.getJwt());
            httpAsync.addParam("id", String.valueOf(dispositivoSelecionado.getId()));
            httpAsync.delete(new FutureCallback() {
                @Override
                public void onBeforeExecute() {
                    findViewById(R.id.device_detail_full).setVisibility(View.GONE);

                }

                @Override
                public void onAfterExecute() {
                    Toast.makeText(DispositivoDetailsActivity.this, "Deletado com sucesso!", Toast.LENGTH_SHORT).show();
                    myTimer.cancel();
                    myTask.cancel();
                    backToDeviceList();
                }

                @Override
                public void onSuccess(int responseCode, Object object) {
                    switch (responseCode) {
                        case 200:
                            System.out.println("Deletado");
                            break;
                        default:
                            LogoutUtils.logout(getApplicationContext());
                            break;
                    }
                }

                @Override
                public void onFailure(Exception exception) {
                    LogoutUtils.logout(getApplicationContext());
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void backToDeviceList(){
        Intent intent = new Intent(getApplicationContext(), DispositivosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("usuarioLogado", usuarioLogado);
        startActivity(intent);
    }

    public void findView(){
        // DEVICE VIEW
        name = (TextView) findViewById(R.id.device_name);
        status = (TextView) findViewById(R.id.device_status);
        ssid = (TextView) findViewById(R.id.device_ssid);
        pass = (TextView) findViewById(R.id.device_pass);
        descricao = (TextView) findViewById(R.id.device_desc);
        tipo_solo = (TextView) findViewById(R.id.device_tipo);
        device_icon = (ImageView) findViewById(R.id.device_icon);


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


    public void setupContent(Dispositivo dispositivoSelecionado){
        // DEVICE INFO
        name.setText(dispositivoSelecionado.getNome());
        status.setText(dispositivoSelecionado.getStatus());
        ssid.setText(dispositivoSelecionado.getWifi_ssid());
        pass.setText(dispositivoSelecionado.getWifi_pass());
        descricao.setText(dispositivoSelecionado.getDescricao());
        tipo_solo.setText(dispositivoSelecionado.getTipo_solo());
        device_icon.setImageResource(GeneralUtils.get_icon(dispositivoSelecionado.getIcone()));

        // SENSOR INFO
        sensor1.setText(getSensor1Text(dispositivoSelecionado));
        sensor1_progress.setProgress((int) (Double.parseDouble(dispositivoSelecionado.getValue1()) / Integer.parseInt(dispositivoSelecionado.getMp1()) *100));

        sensor2.setText(getSensor2Text(dispositivoSelecionado));
        sensor2_progress.setProgress((int) (Double.parseDouble(dispositivoSelecionado.getValue2()) / Integer.parseInt(dispositivoSelecionado.getMp2())*100));

        sensor3.setText(getSensor3Text(dispositivoSelecionado));
        sensor3_progress.setProgress((int) (Double.parseDouble(dispositivoSelecionado.getValue3()) / Integer.parseInt(dispositivoSelecionado.getMp3())*100));

        sensor4.setText(getSensor4Text(dispositivoSelecionado));
        sensor4_progress.setProgress((int) (Double.parseDouble(dispositivoSelecionado.getValue4()) / Integer.parseInt(dispositivoSelecionado.getMp4())*100));

        sensor5.setText(getSensor5Text(dispositivoSelecionado));
        sensor5_progress.setProgress((int) (Double.parseDouble(dispositivoSelecionado.getValue5()) / Integer.parseInt(dispositivoSelecionado.getMp5())*100));
    }




    public String getSensor1Text(Dispositivo dispositivo){
        return String.format("Valor: %s - MP: %s - PP: %s %% - PT: %s segundos", dispositivo.getValue1(), dispositivo.getMp1(), dispositivo.getPp1(), dispositivo.getPt1());
    }

    public String getSensor2Text(Dispositivo dispositivo){
        return String.format("Valor: %s - MP: %s - PP: %s %% - PT: %s segundos", dispositivo.getValue2(), dispositivo.getMp2(), dispositivo.getPp2(), dispositivo.getPt2());
    }

    public String getSensor3Text(Dispositivo dispositivo){
        return String.format("Valor: %s - MP: %s - PP: %s %% - PT: %s segundos", dispositivo.getValue3(), dispositivo.getMp3(), dispositivo.getPp3(), dispositivo.getPt3());
    }

    public String getSensor4Text(Dispositivo dispositivo){
        return String.format("Valor: %s - MP: %s - PP: %s %% - PT: %s segundos", dispositivo.getValue4(), dispositivo.getMp4(), dispositivo.getPp4(), dispositivo.getPt4());
    }
    public String getSensor5Text(Dispositivo dispositivo){
        return String.format("Valor: %s - MP: %s - PP: %s %% - PT: %s segundos", dispositivo.getValue5(), dispositivo.getMp5(), dispositivo.getPp5(), dispositivo.getPt5());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            System.out.println("Pausando timer de consultas");
            myTimer.cancel();
            myTask.cancel();
            onBackPressed();
        } else if (item.getItemId() == R.id.action_update_device) {
            myTimer.cancel();
            myTask.cancel();
            System.out.println("Update device");
//            Toast.makeText(this, "Update device", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), AtualizarDispositivo.class);
            intent.putExtra("usuarioLogado", usuarioLogado);
            intent.putExtra("dispositivo", dispositivoSelecionado);
            startActivity(intent);
        }else if (item.getItemId() == R.id.action_remove_device) {
            alertRemovingDevice();
        }else if (item.getItemId() == R.id.action_send_device) {
            Toast.makeText(this, "Remove device", Toast.LENGTH_SHORT).show();
            myTimer.cancel();
            myTask.cancel();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
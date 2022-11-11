package com.squirtle.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.squirtle.R;
import com.squirtle.model.Dispositivo;
import com.squirtle.model.UsuarioLogado;
import com.squirtle.utils.LogoutUtils;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import mobi.stos.httplib.HttpAsync;
import mobi.stos.httplib.inter.FutureCallback;

@RequiresApi(api = Build.VERSION_CODES.O)

public class IotLinkActivity extends AppCompatActivity {

    UsuarioLogado usuarioLogado;
    Dispositivo dispositivo;
    String currentWifi = "";
    String ssid, key = "";
    boolean saved;

    ProgressBar progressbar;
    TextView iotlink_info;
    MaterialButton connect_wifi_btn, btn_success, start_post;

    Group success_group;

    IntentFilter intentFilter;

    private WifiManager wifiManager;


    private BroadcastReceiver wifi_receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("com.squirtle.receivers.SOME_ACTION"))
            {}
            else {

                ConnectivityManager cm =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                currentWifi = getCurrentSsid();

                    disableAllViews();
                    progressbar.setVisibility(View.VISIBLE);
                    currentWifi = getCurrentSsid();
                    if(currentWifi.contains(ssid)){
                        progressbar.setVisibility(View.GONE);
                        start_post.setVisibility(View.VISIBLE);
                    }else{
                        progressbar.setVisibility(View.GONE);
                        connect_wifi_btn.setVisibility(View.VISIBLE);
                        iotlink_info.setText("Acesse o ponto de acesso do seu Dispositivo!");
                        iotlink_info.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        usuarioLogado = (UsuarioLogado) extras.get("usuarioLogado");
        dispositivo = (Dispositivo) extras.get("dispositivo");


        intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(wifi_receiver, intentFilter);

        setContentView(R.layout.activity_iot_link);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        views();
        setOnclickEvents();
        disableAllViews();
        progressbar.setVisibility(View.VISIBLE);

        ssid = "SquirtleAP";
        key = "squirtleap";
        viewVisibilityWifiCheck();
    }

    public void views(){
        success_group = findViewById(R.id.success_group);
        progressbar = findViewById(R.id.progressBar);
        iotlink_info = findViewById(R.id.iotlink_info);
        btn_success = findViewById(R.id.btn_success);
        connect_wifi_btn = findViewById(R.id.connect_wifi_btn);
        start_post = findViewById(R.id.start_post);
    }

    public void setOnclickEvents(){
        connect_wifi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToAp(ssid, key);
            }
        });
        btn_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dispositivo.getSensor1().equals("null")) dispositivo.setSensor1("0.0");
                if (dispositivo.getSensor2().equals("null")) dispositivo.setSensor2("0.0");
                if (dispositivo.getSensor3().equals("null")) dispositivo.setSensor3("0.0");
                if (dispositivo.getSensor4().equals("null")) dispositivo.setSensor4("0.0");
                if (dispositivo.getSensor5().equals("null")) dispositivo.setSensor5("0.0");

                Intent i = new Intent(getApplicationContext(), DispositivoDetailsActivity.class);
                i.putExtra("dispositivo", dispositivo);
                i.putExtra("usuarioLogado", usuarioLogado);
                startActivity(i);
            }
        });
        start_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //disconnectAP(ssid, key);
                post_to_device(dispositivo);

            }
        });
    }

    public void disableAllViews(){
        success_group.setVisibility(View.GONE);
        progressbar.setVisibility(View.GONE);
        iotlink_info.setVisibility(View.GONE);
        btn_success.setVisibility(View.GONE);
        connect_wifi_btn.setVisibility(View.GONE);
        start_post.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(wifi_receiver, intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(wifi_receiver);
    }

    public void viewVisibilityWifiCheck(){
        currentWifi = getCurrentSsid();
        progressbar.setVisibility(View.GONE);

        if(currentWifi.contains(ssid)){
            start_post.setVisibility(View.VISIBLE);
        }else{
            connect_wifi_btn.setVisibility(View.VISIBLE);
            iotlink_info.setText("Acesse o ponto de acesso do seu Dispositivo!");
            iotlink_info.setVisibility(View.VISIBLE);
        }
    }


    public void post_to_device(Dispositivo dispositivo){
        saved = false;

        String device_setup = "http://192.168.0.17/setup_device";
        try {

            HttpAsync httpAsync = new HttpAsync(new URL(device_setup));
            httpAsync.setDebug(true);

            httpAsync.addParam("device_id", dispositivo.getId());
            httpAsync.addParam("client_id", usuarioLogado.getId());
            httpAsync.addParam("wifi_ssid", dispositivo.getWifi_ssid());
            httpAsync.addParam("wifi_pass", dispositivo.getWifi_pass());
            httpAsync.addParam("status", dispositivo.getStatus());

            httpAsync.post(new FutureCallback() {
                @Override
                public void onBeforeExecute() {
                    disableAllViews();
                    progressbar.setVisibility(View.VISIBLE);
                    iotlink_info.setText("Enviando dados bases ao dispositivo...");
                    iotlink_info.setVisibility(View.VISIBLE);
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onAfterExecute() {
                    disableAllViews();
                    if(saved){
                        Toast.makeText(IotLinkActivity.this, "Configurações bases salvas!", Toast.LENGTH_SHORT).show();
                        // chama segundo post
                        post_to_device_sensors();
                    }else{
                        Toast.makeText(IotLinkActivity.this, "Erro ao salvar informações no dispositivo!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), ModulesActivity.class);
//                        intent.putExtra("usuarioLogado", usuarioLogado);
//                        startActivity(intent);
                        start_post.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onSuccess(int responseCode, Object object) {
                    switch (responseCode) {
                        case 200:
                            JSONObject jsonObject = (JSONObject) object;
                            saved = true;
                            System.out.println();
                            break;
                        case 409:
                            LogoutUtils.logout(getApplicationContext());
                            // usuario com id já cadastrado
//                            Toast.makeText(getApplicationContext(), "Erro conflito", Toast.LENGTH_SHORT).show();
                            System.out.println(object);
                            break;
                        default:
                            LogoutUtils.logout(getApplicationContext());
//                            Toast.makeText(getApplicationContext(), "Response code não tratado", Toast.LENGTH_SHORT).show();
                            System.out.println(object);
                            break;
                    }
                }

                @Override
                public void onFailure(Exception exception) {
                    LogoutUtils.logout(getApplicationContext());
//                    Toast.makeText(getApplicationContext(), "Falha no request", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void post_to_device_sensors(){
        saved = false;

        String device_setup = "http://192.168.0.17/setup_sensor";
        try {

            HttpAsync httpAsync = new HttpAsync(new URL(device_setup));
            httpAsync.setDebug(true);

            httpAsync.addParam("sensor1", "mp:530,pp:30,pt:20");
            httpAsync.addParam("sensor2", "mp:530,pp:30,pt:20");
            httpAsync.addParam("sensor3", "mp:530,pp:30,pt:20");
            httpAsync.addParam("sensor4", "mp:530,pp:30,pt:20");
            httpAsync.addParam("sensor5", "mp:530,pp:30,pt:20");


            httpAsync.post(new FutureCallback() {
                @Override
                public void onBeforeExecute() {
                    disableAllViews();
                    progressbar.setVisibility(View.VISIBLE);
                    iotlink_info.setText("Enviando dados sensorais ao dispositivo...");
                    iotlink_info.setVisibility(View.VISIBLE);
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onAfterExecute() {
                    disableAllViews();
                    if(saved){
                        Toast.makeText(IotLinkActivity.this, "Configuração padrão dos Sensores salvas!", Toast.LENGTH_SHORT).show();
                        success_group.setVisibility(View.VISIBLE);

                    }else{
                        Toast.makeText(IotLinkActivity.this, "Erro ao salvar informações no dispositivo!", Toast.LENGTH_SHORT).show();
                        start_post.setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onSuccess(int responseCode, Object object) {
                    switch (responseCode) {
                        case 200:
                            JSONObject jsonObject = (JSONObject) object;
                            Toast.makeText(getApplicationContext(), "SUCCESS!", Toast.LENGTH_SHORT).show();
                            saved = true;
                            System.out.println();
                            break;
                        case 409:
                            // usuario com id já cadastrado
//                            Toast.makeText(getApplicationContext(), "Erro conflito", Toast.LENGTH_SHORT).show();
                            LogoutUtils.logout(getApplicationContext());
                            System.out.println(object);
                            break;
                        default:
                            LogoutUtils.logout(getApplicationContext());
//                            Toast.makeText(getApplicationContext(), "Response code não tratado", Toast.LENGTH_SHORT).show();
                            System.out.println(object);
                            break;
                    }
                }

                @Override
                public void onFailure(Exception exception) {
                    LogoutUtils.logout(getApplicationContext());
//                    Toast.makeText(getApplicationContext(), "Falha no request", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


    public void disconnectAP(String ssid, String key){
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                try {
                    System.out.println("connection wifi pre Q");
        //                    Toast.makeText(this, "Conectando ao Wifi", Toast.LENGTH_SHORT).show();

                    WifiConfiguration wifiConfig = new WifiConfiguration();
                    wifiConfig.SSID = ssid;
                    wifiConfig.preSharedKey = key;
                    int netId = wifiManager.addNetwork(wifiConfig);
                    wifiManager.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    public void connectToAp(String ssid, String key) {
        {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                try {
                    System.out.println("connection wifi pre Q");
//                    Toast.makeText(this, "Conectando ao Wifi", Toast.LENGTH_SHORT).show();

                    WifiConfiguration wifiConfig = new WifiConfiguration();
                    wifiConfig.SSID = ssid;
                    wifiConfig.preSharedKey = key;
                    int netId = wifiManager.addNetwork(wifiConfig);
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(netId, true);
                    wifiManager.reconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("connection wifi  Q");
//                Toast.makeText(this, "Conectando ao Wifi", Toast.LENGTH_SHORT).show();

                WifiNetworkSpecifier wifiNetworkSpecifier = new WifiNetworkSpecifier.Builder()
                        .setSsid(ssid)
                        .setWpa2Passphrase(key)
                        .build();

                NetworkRequest networkRequest = new NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .setNetworkSpecifier(wifiNetworkSpecifier)
                        .build();

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


                ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {

                    @Override
                    public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                        super.onCapabilitiesChanged(network, networkCapabilities);
//                        Toast.makeText(IotLinkActivity.this, "onCapabilitiesChanged", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties) {
                        super.onLinkPropertiesChanged(network, linkProperties);
//                        Toast.makeText(IotLinkActivity.this, "onLinkPropertiesChanged", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onBlockedStatusChanged(@NonNull Network network, boolean blocked) {
                        super.onBlockedStatusChanged(network, blocked);
//                        Toast.makeText(IotLinkActivity.this, "onBlockedStatusChanged", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);

                        connectivityManager.bindProcessToNetwork(network);
                        System.out.println("onAvailable");
//                        Toast.makeText(IotLinkActivity.this, "Disponível", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLosing(@NonNull Network network, int maxMsToLive) {
                        super.onLosing(network, maxMsToLive);
                        System.out.println("onLosing");
//                        Toast.makeText(IotLinkActivity.this, "Perdendo", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);
                        System.out.println("losing active connection");
//                        Toast.makeText(IotLinkActivity.this, "Perdendo Conexão Ativa", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUnavailable() {
                        super.onUnavailable();
                        System.out.println("onUnavailable");
//                        Toast.makeText(IotLinkActivity.this, "Indisponível", Toast.LENGTH_SHORT).show();
                    }
                };
                connectivityManager.requestNetwork(networkRequest, networkCallback);


            }
        }
    }

    public String getCurrentSsid() {
        String ssid = "";
        try {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifiManager.getConnectionInfo();
            ssid = info.getSSID();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return ssid;
    }
}

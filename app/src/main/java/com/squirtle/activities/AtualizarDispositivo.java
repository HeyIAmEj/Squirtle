package com.squirtle.activities;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.squirtle.R;
import com.squirtle.model.Dispositivo;
import com.squirtle.model.UsuarioLogado;
import com.squirtle.utils.CustomCallback;
import com.squirtle.utils.GeneralUtils;
import com.squirtle.utils.LogoutUtils;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import mobi.stos.httplib.HttpAsync;
import mobi.stos.httplib.inter.FutureCallback;

public class AtualizarDispositivo extends AppCompatActivity {
    UsuarioLogado usuarioLogado;
    Dispositivo dispositivo;

    private AutoCompleteTextView autoCompleteTextView;
    private Button submitDevice;
    private TextView wifi_alert;
    private EditText nomeText, descricaoText, wifi_ssid_text, wifi_pass_text;
    private SwitchMaterial switch_status;
    private Slider time_slider;

    Slider mp1, mp2, mp3, mp4, mp5;
    Slider pp1, pp2, pp3, pp4, pp5;
    Slider pt1, pt2, pt3, pt4, pt5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        usuarioLogado = (UsuarioLogado) extras.get("usuarioLogado");
        dispositivo = (Dispositivo) extras.get("dispositivo");

        setContentView(R.layout.activity_atualizar_dispositivo);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        findViews();

        setContent();

        String[] tipos_solo = getResources().getStringArray(R.array.novodispositivo_tipo_solo_menu);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, R.layout.item_view, R.id.itemTextView, tipos_solo);
        autoCompleteTextView.setAdapter(arrayAdapter);


    }

    public void setContent(){
        nomeText.setText(dispositivo.getNome());
        descricaoText.setText(dispositivo.getDescricao());
        switch_status.setChecked(dispositivo.getStatus().equals("Ativado")?true:false);
        wifi_ssid_text.setText(dispositivo.getWifi_ssid());
        wifi_pass_text.setText(dispositivo.getWifi_pass());
        autoCompleteTextView.setText(dispositivo.getTipo_solo());

        setSensorsContent();

    }

    public void setSensorsContent(){
        mp1.setValue(Float.valueOf(dispositivo.getMp1()));
        pp1.setValue(Float.valueOf(dispositivo.getPp1()));
        pt1.setValue(Float.valueOf(dispositivo.getPt1()));

        mp2.setValue(Float.valueOf(dispositivo.getMp2()));
        pp2.setValue(Float.valueOf(dispositivo.getPp2()));
        pt2.setValue(Float.valueOf(dispositivo.getPt2()));
    }


    public void findViews(){
        nomeText = findViewById(R.id.nomeText);
        descricaoText = findViewById(R.id.descricaoText);
        switch_status = findViewById(R.id.switch_status);
        wifi_ssid_text = findViewById(R.id.wifi_ssid_text);
        wifi_pass_text = findViewById(R.id.wifi_pass_text);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        mp1 = findViewById(R.id.sensor1_mp);
        pp1 = findViewById(R.id.sensor1_pp);
        pt1 = findViewById(R.id.sensor1_pt);

        mp2 = findViewById(R.id.sensor2_mp);
        pp2 = findViewById(R.id.sensor2_pp);
        pt2 = findViewById(R.id.sensor2_pt);

    }

    public void disableForm(){
        nomeText.setEnabled(false);
        descricaoText.setEnabled(false);
        switch_status.setEnabled(false);
        wifi_ssid_text.setEnabled(false);
        wifi_pass_text.setEnabled(false);
        autoCompleteTextView.setEnabled(false);
        findViewById(R.id.sensor_1_include).setVisibility(GONE);
        findViewById(R.id.sensor_2_include).setVisibility(GONE);
        findViewById(R.id.update_device).setEnabled(false);

    }

    public void enableForm(){
        nomeText.setEnabled(true);
        descricaoText.setEnabled(true);
        switch_status.setEnabled(true);
        wifi_ssid_text.setEnabled(true);
        wifi_pass_text.setEnabled(true);
        autoCompleteTextView.setEnabled(true);
        findViewById(R.id.sensor_1_include).setVisibility(View.VISIBLE);
        findViewById(R.id.sensor_2_include).setVisibility(View.VISIBLE);
        findViewById(R.id.update_device).setEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }else if (item.getItemId() == R.id.update_device) {
            putDevice();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void alertLinkDevice(){
        GeneralUtils.alert(AtualizarDispositivo.this,
                String.format("Vínculo com o dispositivo"),
                "Deseja enviar a nova calibragem para o dispositivo?",
                "Sim", "Não", new CustomCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTrueCallback() {
                        Intent intent = new Intent(getApplicationContext(), IotLinkActivity.class);
                        intent.putExtra("usuarioLogado", usuarioLogado);
                        intent.putExtra("dispositivo", dispositivo);
                        startActivity(intent);
                    }
                    @Override
                    public void onFalseCallback() {
                        backToDeviceList();
                    }
                });
    }


    public void putDevice(){
        Dispositivo dispositivoCriado = new Dispositivo();
        try {
            HttpAsync httpAsync = new HttpAsync(new URL(
                    getString(R.string.squirtle_api) + getString(R.string.squirtle_api_endpoint) +
                            getString(R.string.squirtle_api_endpoint_dispositivo)));
            httpAsync.setDebug(true);
            httpAsync.addHeader("Authorization", "Bearer " + usuarioLogado.getJwt());

            httpAsync.addParam("id", dispositivo.getId());
            httpAsync.addParam("nome", nomeText.getText());
            httpAsync.addParam("descricao", descricaoText.getText());
            httpAsync.addParam("icone", dispositivo.getIcone());
            httpAsync.addParam("wifi_ssid", wifi_ssid_text.getText());
            httpAsync.addParam("wifi_pass", wifi_pass_text.getText());
            httpAsync.addParam("status", switch_status.isChecked());
            httpAsync.addParam("tipo_solo", autoCompleteTextView.getText());
            httpAsync.addParam("sensor1", getSensor1Values());
            httpAsync.addParam("sensor2", getSensor2Values());
            httpAsync.addParam("sensor3", getSensor3Values());
            httpAsync.addParam("sensor4", getSensor4Values());
            httpAsync.addParam("sensor5", getSensor5Values());



            httpAsync.put(new FutureCallback() {
                @Override
                public void onBeforeExecute() {
                    disableForm();
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onAfterExecute() {
                    enableForm();

                    if(dispositivoCriado != null){
                        dispositivo = dispositivoCriado;
                        Toast.makeText(getApplicationContext(), "Dispositivo atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Não foi possível atualizar seu dispositivo! ", Toast.LENGTH_SHORT).show();
                    }
                    alertLinkDevice();
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
                            // usuario com id já cadastrado
                            LogoutUtils.logout(getApplicationContext());
//                            Toast.makeText(getApplicationContext(), "Erro 500!", Toast.LENGTH_SHORT).show();
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

    public void backToDeviceList(){
        Intent intent = new Intent(getApplicationContext(), DispositivosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("usuarioLogado", usuarioLogado);
        intent.putExtra("dispositivo", dispositivo);
        startActivity(intent);
    }
    public void backToDeviceDetail(Dispositivo dispositivoCriado){
        Intent intent = new Intent(getApplicationContext(), DispositivoDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("usuarioLogado", usuarioLogado);
        intent.putExtra("dispositivo", dispositivoCriado);
        startActivity(intent);
    }

    public String getSensor1Values(){
        mp1 = findViewById(R.id.sensor1_mp);
        String mp = String.valueOf(Math.round(mp1.getValue()));
        pp1 = findViewById(R.id.sensor1_pp);
        String pp = String.valueOf(Math.round(pp1.getValue()));
        pt1 = findViewById(R.id.sensor1_pt);
        String pt = String.valueOf(Math.round(pt1.getValue()));
//        Toast.makeText(this, String.format("mp:%s,pp:%s,pt:%s,value:%s", mp, pp, pt, dispositivo.getValue1()), Toast.LENGTH_SHORT).show();
        return String.format("mp:%s,pp:%s,pt:%s,value:%s", mp, pp, pt, dispositivo.getValue1());
    }
    public String getSensor2Values(){
        mp2 = findViewById(R.id.sensor2_mp);
        String mp = String.valueOf(Math.round(mp2.getValue()));
        pp2 = findViewById(R.id.sensor2_pp);
        String pp = String.valueOf(Math.round(pp2.getValue()));
        pt2 = findViewById(R.id.sensor2_pt);
        String pt = String.valueOf(Math.round(pt2.getValue()));
//        Toast.makeText(this, String.format("mp:%s,pp:%s,pt:%s,value:%s", mp, pp, pt, dispositivo.getValue2()), Toast.LENGTH_SHORT).show();
        return String.format("mp:%s,pp:%s,pt:%s,value:%s", mp, pp, pt, dispositivo.getValue2());
    }

    public String getSensor3Values(){
        mp3 = findViewById(R.id.sensor2_mp);
        String mp = String.valueOf(Math.round(mp3.getValue()));
        pp3 = findViewById(R.id.sensor2_pp);
        String pp = String.valueOf(Math.round(pp3.getValue()));
        pt3 = findViewById(R.id.sensor2_pp);
        String pt = String.valueOf(Math.round(pt3.getValue()));
//        Toast.makeText(this, String.format("mp:%s,pp:%s,pt:%s,value:%s", mp, pp, pt, dispositivo.getValue3()), Toast.LENGTH_SHORT).show();
        return String.format("mp:%s,pp:%s,pt:%s,value:%s", mp, pp, pt, dispositivo.getValue3());
    }

    public String getSensor4Values(){
        mp4 = findViewById(R.id.sensor2_mp);
        String mp = String.valueOf(Math.round(mp4.getValue()));
        pp4 = findViewById(R.id.sensor2_pp);
        String pp = String.valueOf(Math.round(pp4.getValue()));
        pt4 = findViewById(R.id.sensor2_pp);
        String pt = String.valueOf(Math.round(pt4.getValue()));
//        Toast.makeText(this, String.format("mp:%s,pp:%s,pt:%s,value:%s", mp, pp, pt, dispositivo.getValue4()), Toast.LENGTH_SHORT).show();
        return String.format("mp:%s,pp:%s,pt:%s,value:%s", mp, pp, pt, dispositivo.getValue4());
    }

    public String getSensor5Values(){
        mp5 = findViewById(R.id.sensor2_mp);
        String mp = String.valueOf(Math.round(mp5.getValue()));
        pp5 = findViewById(R.id.sensor2_pp);
        String pp = String.valueOf(Math.round(pp5.getValue()));
        pt5 = findViewById(R.id.sensor2_pp);
        String pt = String.valueOf(Math.round(pt5.getValue()));
//        Toast.makeText(this, String.format("mp:%s,pp:%s,pt:%s,value:%s", mp, pp, pt, dispositivo.getValue5()), Toast.LENGTH_SHORT).show();
        return String.format("mp:%s,pp:%s,pt:%s,value:%s", mp, pp, pt, dispositivo.getValue5());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }


}
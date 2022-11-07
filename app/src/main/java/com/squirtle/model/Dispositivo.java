package com.squirtle.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Dispositivo implements Serializable {
    private int id;

    private String nome;
    private String descricao;

    private String status;

    private String icone;

    private String wifi_ssid;

    private String wifi_pass;


    private String tipo_solo;
    private String tempo_bomba;

    private String sensor1;
    private String sensor2;
    private String sensor3;
    private String sensor4;
    private String sensor5;

    public Dispositivo() {
    }

    public Dispositivo(String nome, String status, String icone) {
        this.nome = nome;
        this.status = status;
        this.icone = icone;
    }

    public Dispositivo(int id, String nome, String status, String icone) {
        this.id = id;
        this.nome = nome;
        this.status = status;
        this.icone = icone;
    }

    public Dispositivo(int id, String nome, String status, String icone, String wifi_ssid, String wifi_pass, String sensor1, String sensor2, String sensor3, String sensor4, String sensor5) {
        this.id = id;
        this.nome = nome;
        this.status = status;
        this.icone = icone;
        this.wifi_ssid = wifi_ssid;
        this.wifi_pass = wifi_pass;
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.sensor4 = sensor4;
        this.sensor5 = sensor5;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        if(status.equals("true"))
            return "Ativado";
        else
            return "Desativado";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getWifi_ssid() {
        return wifi_ssid;
    }

    public void setWifi_ssid(String wifi_ssid) {
        this.wifi_ssid = wifi_ssid;
    }

    public String getWifi_pass() {
        return wifi_pass;
    }

    public void setWifi_pass(String wifi_pass) {
        this.wifi_pass = wifi_pass;
    }

    public String getSensor1() {
        return sensor1;
    }

    public void setSensor1(String sensor1) {
        this.sensor1 = sensor1;
    }

    public String getSensor2() {
        return sensor2;
    }

    public void setSensor2(String sensor2) {
        this.sensor2 = sensor2;
    }

    public String getSensor3() {
        return sensor3;
    }

    public void setSensor3(String sensor3) {
        this.sensor3 = sensor3;
    }

    public String getSensor4() {
        return sensor4;
    }

    public void setSensor4(String sensor4) {
        this.sensor4 = sensor4;
    }

    public String getSensor5() {
        return sensor5;
    }

    public void setSensor5(String sensor5) {
        this.sensor5 = sensor5;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if(descricao == "null"){
            descricao = "Sem descrição";
        }
        this.descricao = descricao;

    }

    public String getTipo_solo() {
        return tipo_solo;
    }

    public void setTipo_solo(String tipo_solo) {
        this.tipo_solo = tipo_solo;
    }

    public String gettempo_bomba() {
        return tempo_bomba;
    }

    public void settempo_bomba(String tempo_bomba) {
        this.tempo_bomba = tempo_bomba;
    }

    public Dispositivo(int id, String nome, String descricao, String status, String icone, String wifi_ssid, String wifi_pass, String tipo_solo, String tempo_bomba, String sensor1, String sensor2, String sensor3, String sensor4, String sensor5) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.status = status;
        this.icone = icone;
        this.wifi_ssid = wifi_ssid;
        this.wifi_pass = wifi_pass;
        this.tipo_solo = tipo_solo;
        this.tempo_bomba = tempo_bomba;
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.sensor4 = sensor4;
        this.sensor5 = sensor5;
    }
}

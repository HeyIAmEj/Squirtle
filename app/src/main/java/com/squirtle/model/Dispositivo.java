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

    private String sensor1, sensor2, sensor3, sensor4, sensor5;

    private String mp1, mp2, mp3, mp4, mp5;
    private String pp1, pp2, pp3, pp4, pp5;
    private String pt1, pt2, pt3, pt4, pt5;
    private String value1, value2, value3, value4, value5;


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
        if(sensor1.equals("null") || sensor1.equals(null) || sensor1.equals("")){
            this.mp1 = "0.0";
            this.pp1 = "0.0";
            this.pt1 = "0.0";
            this.value1 = "0.0";
            this.sensor1 = "";
        }else{
        String[] splittedSensor = sensor1.split(",");
        this.mp1 = splittedSensor[0].split(":")[1];
        this.pp1 = splittedSensor[1].split(":")[1];
        this.pt1 = splittedSensor[2].split(":")[1];
        this.value1 = splittedSensor[3].split(":")[1];
        this.sensor1 = sensor1;
    }}

    public String getSensor2() {
        return sensor2;
    }

    public void setSensor2(String sensor2) {
        if(sensor2.equals("null") || sensor2.equals(null) || sensor2.equals("")){
            this.mp2 = "0.0";
            this.pp2 = "0.0";
            this.pt2 = "0.0";
            this.value2 = "0.0";
            this.sensor2 = "";
        }else{
            String[] splittedSensor = sensor2.split(",");
            this.mp2 = splittedSensor[0].split(":")[1];
            this.pp2 = splittedSensor[1].split(":")[1];
            this.pt2 = splittedSensor[2].split(":")[1];
            this.value2 = splittedSensor[3].split(":")[1];
            this.sensor2 = sensor2;
        }
    }

    public String getSensor3() {
        return sensor3;
    }

    public void setSensor3(String sensor3) {
        if(sensor3.equals("null") || sensor3.equals(null) || sensor3.equals("")){
            this.mp3 = "0.0";
            this.pp3 = "0.0";
            this.pt3 = "0.0";
            this.value3 = "0.0";
            this.sensor3 = "";
        }else{
            String[] splittedSensor = sensor3.split(",");
            this.mp3 = splittedSensor[0].split(":")[1];
            this.pp3 = splittedSensor[1].split(":")[1];
            this.pt3 = splittedSensor[2].split(":")[1];
            this.value3 = splittedSensor[3].split(":")[1];
            this.sensor3 = sensor3;
        }
    }

    public String getSensor4() {
        return sensor4;
    }

    public void setSensor4(String sensor4) {
        if(sensor4.equals("null") || sensor4.equals(null) || sensor4.equals("")){
            this.mp4 = "0.0";
            this.pp4 = "0.0";
            this.pt4 = "0.0";
            this.value4 = "0.0";
            this.sensor4 = "";
        }else{
            String[] splittedSensor = sensor4.split(",");
            this.mp4 = splittedSensor[0].split(":")[1];
            this.pp4 = splittedSensor[1].split(":")[1];
            this.pt4 = splittedSensor[2].split(":")[1];
            this.value4 = splittedSensor[3].split(":")[1];
            this.sensor4 = sensor4;
        }
    }


    public String getSensor5() {
        return sensor5;
    }

    public void setSensor5(String sensor5) {
        if(sensor5.equals("null") || sensor5.equals(null) || sensor5.equals("")){
            this.mp5 = "0.0";
            this.pp5 = "0.0";
            this.pt5 = "0.0";
            this.value5 = "0.0";
            this.sensor5 = "";
        }else{
            String[] splittedSensor = sensor5.split(",");
            this.mp5 = splittedSensor[0].split(":")[1];
            this.pp5 = splittedSensor[1].split(":")[1];
            this.pt5 = splittedSensor[2].split(":")[1];
            this.value5 = splittedSensor[3].split(":")[1];
            this.sensor5 = sensor5;
        }
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

    public String getMp1() {
        return mp1;
    }

    public String getMp2() {
        return mp2;
    }

    public String getMp3() {
        return mp3;
    }

    public String getMp4() {
        return mp4;
    }

    public String getMp5() {
        return mp5;
    }

    public String getPp1() {
        return pp1;
    }

    public String getPp2() {
        return pp2;
    }

    public String getPp3() {
        return pp3;
    }

    public String getPp4() {
        return pp4;
    }

    public String getPp5() {
        return pp5;
    }

    public String getPt1() {
        return pt1;
    }

    public String getPt2() {
        return pt2;
    }

    public String getPt3() {
        return pt3;
    }

    public String getPt4() {
        return pt4;
    }

    public String getPt5() {
        return pt5;
    }

    public String getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }

    public String getValue3() {
        return value3;
    }

    public String getValue4() {
        return value4;
    }

    public String getValue5() {
        return value5;
    }

    public Dispositivo(int id, String nome, String descricao, String status, String icone, String wifi_ssid, String wifi_pass, String tipo_solo, String sensor1, String sensor2, String sensor3, String sensor4, String sensor5) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.status = status;
        this.icone = icone;
        this.wifi_ssid = wifi_ssid;
        this.wifi_pass = wifi_pass;
        this.tipo_solo = tipo_solo;
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.sensor4 = sensor4;
        this.sensor5 = sensor5;
    }

    public Dispositivo(int id, String nome, String descricao, String status, String icone, String wifi_ssid, String wifi_pass, String tipo_solo, String sensor1, String sensor2, String sensor3, String sensor4, String sensor5, String mp1, String mp2, String mp3, String mp4, String mp5, String pp1, String pp2, String pp3, String pp4, String pp5, String pt1, String pt2, String pt3, String pt4, String pt5, String value1, String value2, String value3, String value4, String value5) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.status = status;
        this.icone = icone;
        this.wifi_ssid = wifi_ssid;
        this.wifi_pass = wifi_pass;
        this.tipo_solo = tipo_solo;
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.sensor4 = sensor4;
        this.sensor5 = sensor5;
        this.mp1 = mp1;
        this.mp2 = mp2;
        this.mp3 = mp3;
        this.mp4 = mp4;
        this.mp5 = mp5;
        this.pp1 = pp1;
        this.pp2 = pp2;
        this.pp3 = pp3;
        this.pp4 = pp4;
        this.pp5 = pp5;
        this.pt1 = pt1;
        this.pt2 = pt2;
        this.pt3 = pt3;
        this.pt4 = pt4;
        this.pt5 = pt5;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
    }
}

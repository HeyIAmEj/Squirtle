package com.squirtle.model;

import java.util.ArrayList;

public class Usuario {
    private int id;

    private String nome;

    private String sobrenome;

    private String email;

    private ArrayList<Object> dispositivos;

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

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Object> getDispositivos() {
        return dispositivos;
    }

    public void setDispositivos(ArrayList<Object> dispositivos) {
        this.dispositivos = dispositivos;
    }

    public Usuario() {
    }

    public Usuario(int id, String nome, String sobrenome, String email, ArrayList<Object> dispositivos) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.dispositivos = dispositivos;
    }
}

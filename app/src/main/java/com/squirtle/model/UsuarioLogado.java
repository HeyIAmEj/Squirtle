package com.squirtle.model;

import java.io.Serializable;

public class UsuarioLogado implements Serializable {
    private int id;
    private String email;
    private String jwt;
    private Boolean isExpired;


    public UsuarioLogado() {
    }

    public UsuarioLogado(int id, String email, String jwt, Boolean isExpired) {
        this.id = id;
        this.email = email;
        this.jwt = jwt;
        this.isExpired = isExpired;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }
}

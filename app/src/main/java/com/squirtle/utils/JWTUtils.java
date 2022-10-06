package com.squirtle.utils;

import android.content.Context;

import com.auth0.android.jwt.JWT;
import com.squirtle.model.UsuarioLogado;

import java.util.Date;


public class JWTUtils {
    public static UsuarioLogado setUserJWT(Context context, String jwtString) {
        UsuarioLogado usuarioLogado = new UsuarioLogado();
        JWT jwt = new JWT(jwtString);

        usuarioLogado.setId(Integer.parseInt(jwt.getId()));
        usuarioLogado.setEmail(jwt.getSubject());
        usuarioLogado.setJwt(jwtString);
        usuarioLogado.setExpired(jwt.getExpiresAt().before(new Date()));

//        SharedPreferencesUtils.setInt(context, "usuario", "id", Integer.parseInt(jwt.getId()));
//        SharedPreferencesUtils.setString(context, "usuario", "jwt", jwt.getSubject());

        return usuarioLogado;
    }
}

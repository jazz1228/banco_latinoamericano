package com.udea.web.banco.banco.Object;

public class TokenObject {

    private String token;
    private String rol;
    public TokenObject(String token, String rol){
        this.token=token; this.rol=rol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

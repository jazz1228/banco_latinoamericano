package com.udea.web.banco.banco.BankDTO;

public class Mensaje {

    private String mensaje;
    public Mensaje(String mensaje){
        this.mensaje=mensaje;
    }

    public String getMessage() {
        return mensaje;
    }

    public void setMessage(String message) {
        this.mensaje = message;
    }
}

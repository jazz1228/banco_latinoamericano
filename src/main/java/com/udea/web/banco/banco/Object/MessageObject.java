package com.udea.web.banco.banco.Object;

public class MessageObject {

    private String mensaje;
    public MessageObject(String mensaje){
        this.mensaje=mensaje;
    }

    public String getMessage() {
        return mensaje;
    }

    public void setMessage(String message) {
        this.mensaje = message;
    }
}

package com.udea.web.banco.banco.Object;

public class Transacciones {
    private String tipoTRansaccion;
    private double monto;
    private String fecha;

    public Transacciones(String tipoTRansaccion, double monto, String fecha) {
        this.tipoTRansaccion = tipoTRansaccion;
        this.monto = monto;
        this.fecha = fecha;
    }

    public String getTipoTRansaccion() {
        return tipoTRansaccion;
    }

    public void setTipoTRansaccion(String tipoTRansaccion) {
        this.tipoTRansaccion = tipoTRansaccion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}

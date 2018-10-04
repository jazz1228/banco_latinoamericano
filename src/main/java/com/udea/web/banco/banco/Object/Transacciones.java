package com.udea.web.banco.banco.Object;

public class Transacciones {
    private String tipoTransaccion;
    private double monto;
    private String fecha;
    private String moneda;

    public Transacciones(String tipoTransaccion, double monto, String fecha, String moneda) {
        this.tipoTransaccion = tipoTransaccion;
        this.monto = monto;
        this.fecha = fecha;
        this.moneda=moneda;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getTipoTRansaccion() {
        return tipoTransaccion;
    }

    public void setTipoTRansaccion(String tipoTRansaccion) {
        this.tipoTransaccion = tipoTRansaccion;
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

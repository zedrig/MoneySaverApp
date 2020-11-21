package com.zedrig.moneysaverapp.model.entity;

import java.io.Serializable;

public class Ingreso implements Serializable {

    public int valor;
    public String usuario_id;
    public String fecha;

    public Ingreso(int valor, String usuario_id, String fecha) {
        this.valor = valor;
        this.usuario_id = usuario_id;
        this.fecha = fecha;
    }

    public Ingreso() {
    }

    public double getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "$ "+this.valor+" - "+this.fecha;
    }
}

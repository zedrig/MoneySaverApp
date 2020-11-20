package com.zedrig.moneysaverapp.model.entity;

import java.io.Serializable;

public class Ingreso implements Serializable {

    public int valor;
    public String usuario_id;

    public Ingreso(int valor, String usuario_id) {
        this.valor = valor;
        this.usuario_id = usuario_id;
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
}

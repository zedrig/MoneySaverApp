package com.zedrig.moneysaverapp.model.entity;

import java.io.Serializable;

public class Ingreso implements Serializable {

    public double valor;
    public String usuario_id;

    public Ingreso(double valor, String usuario_id) {
        this.valor = valor;
        this.usuario_id = usuario_id;
    }

    public Ingreso() {
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }
}

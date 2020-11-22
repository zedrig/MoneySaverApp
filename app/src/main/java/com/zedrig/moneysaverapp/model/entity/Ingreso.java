package com.zedrig.moneysaverapp.model.entity;

import java.io.Serializable;

public class Ingreso implements Serializable {

    public int valor;
    private String id;
    public String usuario_id;
    public String fecha;
    public String descripcion;

    public Ingreso(int valor, String usuario_id, String fecha, String descripcion) {
        this.valor = valor;
        this.usuario_id = usuario_id;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.id = "";
    }

    public Ingreso() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getValor() {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "$ "+this.valor+" - "+this.fecha;
    }
}

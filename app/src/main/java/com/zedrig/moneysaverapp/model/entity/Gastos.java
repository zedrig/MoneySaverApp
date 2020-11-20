package com.zedrig.moneysaverapp.model.entity;

import java.io.Serializable;

public class Gastos implements Serializable {

    private String fecha;
    private String categoria;
    private double valor;
    private String descripcion;
    private String usuario_id;
    private String id;

    public Gastos(String categoria, double valor, String descripcion, String usuario_id, String fecha) {
        this.categoria = categoria;
        this.valor = valor;
        this.descripcion = descripcion;
        this.usuario_id = usuario_id;
        this.fecha = fecha;
        this.id = "";
    }

    @Override
    public String toString() {
        return this.categoria+" ($"+this.valor+")"+" '"+this.descripcion+"' ";
    }

    public Gastos() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }
}

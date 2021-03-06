package com.zedrig.moneysaverapp.model.entity;

import java.io.Serializable;

public class Gastos implements Serializable {

    private String fecha;
    //3 variables for future features
    private String year;
    private String mes;
    private String dia;
    //
    private String categoria;
    private int valor;
    private String descripcion;
    private String usuario_id;
    private String id;

    public Gastos(String categoria, int valor, String descripcion, String usuario_id, String fecha, String year, String mes, String dia) {
        this.categoria = categoria;
        this.valor = valor;
        this.descripcion = descripcion;
        this.usuario_id = usuario_id;
        this.fecha = fecha;
        this.id = "";
        this.year = year;
        this.mes = mes;
        this.dia = dia;
    }

    @Override
    public String toString() {
        return "$ "+this.valor+" - "+this.categoria;
    }

    public Gastos() {
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
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

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
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

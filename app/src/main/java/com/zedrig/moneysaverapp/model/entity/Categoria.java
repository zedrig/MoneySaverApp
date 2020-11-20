package com.zedrig.moneysaverapp.model.entity;

public class Categoria {

    public String nombre;
    public String usuario_id;

    public Categoria(String nombre, String usuario_id) {
        this.nombre = nombre;
        this.usuario_id = usuario_id;
    }

    public Categoria() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}

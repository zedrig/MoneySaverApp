package com.zedrig.moneysaverapp.model.network;

public interface MoneyCallback<T> {

    void correcto (T respuesta);
    void error (Exception exception);
}

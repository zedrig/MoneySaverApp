package com.zedrig.moneysaverapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zedrig.moneysaverapp.R;
import com.zedrig.moneysaverapp.model.entity.Gastos;

import java.util.ArrayList;

public class GastoAdatador extends BaseAdapter {

    Context context;
    ArrayList<Gastos> lista;
    LayoutInflater layoutInflater;

    public GastoAdatador(Context context, ArrayList<Gastos> lista) {
        this.context = context;
        this.lista = lista;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null){

            view =layoutInflater.inflate(R.layout.list_view_gastos, null);
        }

        TextView valor = (TextView) view.findViewById(R.id.gasto_valor);
        TextView fecha = (TextView)view.findViewById(R.id.fecha_gasto);
        TextView cat = (TextView) view.findViewById(R.id.cat_gasto);
        TextView desc = (TextView) view.findViewById(R.id.desc_gasto);

        valor.setText("$ "+lista.get(i).getValor());
        fecha.setText(lista.get(i).getFecha());
        cat.setText(lista.get(i).getCategoria());
        desc.setText(lista.get(i).getDescripcion());


        return view;
    }
}

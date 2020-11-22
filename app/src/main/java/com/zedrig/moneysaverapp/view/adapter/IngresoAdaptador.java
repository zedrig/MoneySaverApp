package com.zedrig.moneysaverapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zedrig.moneysaverapp.R;
import com.zedrig.moneysaverapp.model.entity.Gastos;
import com.zedrig.moneysaverapp.model.entity.Ingreso;

import java.util.ArrayList;

public class IngresoAdaptador extends BaseAdapter {

    Context context;
    ArrayList<Ingreso> lista;
    LayoutInflater layoutInflater;

    public IngresoAdaptador(Context context, ArrayList<Ingreso> lista) {
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
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_view_gastos, null);
        }

        TextView valor = (TextView) view.findViewById(R.id.gasto_valor);
        TextView fecha = (TextView)view.findViewById(R.id.fecha_gasto);
        TextView desc = (TextView) view.findViewById(R.id.cat_gasto);

        valor.setText("$ "+lista.get(i).getValor());
        fecha.setText(lista.get(i).getFecha());
        desc.setText(lista.get(i).getDescripcion());

        return view;
    }
}

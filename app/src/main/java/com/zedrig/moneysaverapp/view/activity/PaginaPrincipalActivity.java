package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zedrig.moneysaverapp.model.entity.Gastos;
import com.zedrig.moneysaverapp.model.repository.GastosRepository;
import com.zedrig.moneysaverapp.model.entity.Ingreso;
import com.zedrig.moneysaverapp.model.repository.IngresoRepository;
import com.zedrig.moneysaverapp.model.network.MoneyCallback;
import com.zedrig.moneysaverapp.R;

import java.util.ArrayList;

public class PaginaPrincipalActivity extends AppCompatActivity {

    private TextView tvSaldoactual;
    private ListView lvGastos;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private IngresoRepository ingresoRepository;
    private GastosRepository gastosRepository;
    private Ingreso ingreso;
    private Gastos gastos;
    private ArrayList<Gastos> lista;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_logout:
                auth.signOut();
                Intent i = new Intent(PaginaPrincipalActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //añadir botón back en la action bar
//        setTitle("title"); //nombre del action bar

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        ingresoRepository = new IngresoRepository(PaginaPrincipalActivity.this);

        asociarElementos();

        mostrarIngreso();

    }

    public void nuevoGasto(View view) {
        Intent i = new Intent(PaginaPrincipalActivity.this, NuevoGastoActivity.class);
        startActivity(i);
    }

    public void nuevoIngreso(View view) {
        Intent i = new Intent(PaginaPrincipalActivity.this, NuevoIngresoActivity.class);
        startActivity(i);
    }

    private void asociarElementos() {

        tvSaldoactual = findViewById(R.id.tv_saldo_actual);
        lvGastos = findViewById(R.id.lv_gastos);

    }

    private double calcularTotal(ArrayList<Ingreso>datos) {
        double total = 0;
        for (Ingreso ingreso : datos) {
            total += ingreso.getValor();
        }
        return total;
    }

    private void mostrarIngreso(){
        ingresoRepository.obtenerIngreso(new MoneyCallback<ArrayList<Ingreso>>() {
            @Override
            public void correcto(ArrayList<Ingreso> respuesta) {
                double valorfinal = calcularTotal(respuesta);
                tvSaldoactual.setText("$ "+valorfinal);
                Log.d("finalnum", String.valueOf(respuesta));
            }

            @Override
            public void error(Exception exception) {

            }
        });
    }

//    private void actualizarListado(ArrayList<Gastos> datos){
//        ArrayAdapter<Gastos> adapter = new ArrayAdapter<Gastos>(PaginaPrincipalActivity.this, android.R.layout.simple_list_item_1, datos);
//
//        lvGastos.setAdapter(adapter);
//    }
}
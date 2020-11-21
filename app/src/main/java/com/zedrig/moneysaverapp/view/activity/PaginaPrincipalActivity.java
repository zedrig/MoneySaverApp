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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PaginaPrincipalActivity extends AppCompatActivity {

    private TextView tvSaldoactual;
    private TextView tvGastodiario;
    private ListView lvGastos;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private IngresoRepository ingresoRepository;
    private GastosRepository gastosRepository;
    private Ingreso ingreso;
    private Gastos gastos;
    private int valorfinalingreso;
    private int valorfinalgasto;
    private int valorfinal;
    private ArrayList<Gastos> lista;
    private int actualdia;
    private int maxdia;
    private int difdias;



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

        gastosRepository = new GastosRepository(PaginaPrincipalActivity.this);

        asociarElementos();

        mostrarGastos();

        mostrarGastos5();

        calcularDias();


    }

    @Override
    protected void onResume() {
        super.onResume();

        mostrarGastos();

        mostrarGastos5();

        mostrarIngreso();

        calcularDias();



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
        tvGastodiario = findViewById(R.id.tv_gasto_diario);
        lvGastos = findViewById(R.id.lv_gastos);
    }

    private int calcularTotalingreso(ArrayList<Ingreso>datos) {
        int total = 0;
        for (Ingreso ingreso : datos) {
            total += ingreso.getValor();
        }
        return total;
    }
    private int calcularTotalgasto(ArrayList<Gastos>datos) {
        int total = 0;
        for (Gastos gastos : datos) {
            total += gastos.getValor();
        }
        return total;
    }

    private void mostrarIngreso(){
        ingresoRepository.obtenerIngreso(new MoneyCallback<ArrayList<Ingreso>>() {
            @Override
            public void correcto(ArrayList<Ingreso> respuesta) {
                valorfinalingreso = calcularTotalingreso(respuesta);
                valorfinal = valorfinalingreso - valorfinalgasto;
                tvSaldoactual.setText("$ "+valorfinal);

                int gastodiario = valorfinal/difdias;
                tvGastodiario.setText("$ "+gastodiario);

                Log.d("testeofinal", String.valueOf(respuesta));
            }

            @Override
            public void error(Exception exception) {

            }
        });
    }

    private void calcularDias(){
        Calendar calendar = Calendar.getInstance();
        Date date = Calendar.getInstance().getTime();
        maxdia = calendar.getActualMaximum(Calendar.DATE);
        DateFormat dateFormat = new SimpleDateFormat("dd");

        String actualdiast = dateFormat.format(date);
        actualdia = Integer.parseInt(actualdiast)-1;

        difdias = maxdia - actualdia;

        Log.d("dia de hoy: ", String.valueOf(actualdia));
        Log.d("dia maximo: ", String.valueOf(maxdia));
        Log.d("dia diff: ", String.valueOf(difdias));


        //Log.d("testeogastomaximo: ", String.valueOf(gastodiario));
    }

    private void mostrarGastos(){
        gastosRepository.escucharGasto(new MoneyCallback<ArrayList<Gastos>>() {
            @Override
            public void correcto(ArrayList<Gastos> respuesta) {
                valorfinalgasto = calcularTotalgasto(respuesta);
                Log.d("testeogastosfinal", String.valueOf(respuesta));
            }

            @Override
            public void error(Exception exception) {

            }
        });
    }

    private void mostrarGastos5(){
        gastosRepository.escucharGasto5(new MoneyCallback<ArrayList<Gastos>>() {
            @Override
            public void correcto(ArrayList<Gastos> respuesta) {
                actualizarListado(respuesta);
            }

            @Override
            public void error(Exception exception) {

            }
        });
    }


    private void actualizarListado(ArrayList<Gastos> datos){
        ArrayAdapter<Gastos> adapter = new ArrayAdapter<Gastos>(PaginaPrincipalActivity.this, android.R.layout.simple_list_item_1, datos);
        lvGastos.setAdapter(adapter);

    }

    public void nuevaCategoria(View view) {
        Intent i = new Intent(PaginaPrincipalActivity.this, NuevaCategoriaActivity.class);
        startActivity(i);
    }

    public void verPerfil(View view) {
        Intent i = new Intent(PaginaPrincipalActivity.this, PerfilActivity.class);
        startActivity(i);
    }

    public void nuevoDato(View view) {
        Intent i = new Intent(PaginaPrincipalActivity.this, DatosActivity.class);
        startActivity(i);
    }
}
package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zedrig.moneysaverapp.R;
import com.zedrig.moneysaverapp.model.entity.Gastos;
import com.zedrig.moneysaverapp.model.entity.Ingreso;
import com.zedrig.moneysaverapp.model.network.MoneyCallback;
import com.zedrig.moneysaverapp.model.repository.GastosRepository;
import com.zedrig.moneysaverapp.model.repository.IngresoRepository;
import com.zedrig.moneysaverapp.model.repository.UsuarioRepository;

import java.util.ArrayList;

public class DatosActivity extends AppCompatActivity {

    private ListView lvGasto;
    private ListView lvIngreso;
    private GastosRepository gastosRepository;
    private IngresoRepository ingresoRepository;
    private UsuarioRepository usuarioRepository;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gastosRepository = new GastosRepository(DatosActivity.this);
        ingresoRepository = new IngresoRepository(DatosActivity.this);
        usuarioRepository = new UsuarioRepository(DatosActivity.this);

        asociarElementos();

//        lvGasto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Gastos nuevogasto = (Gastos) lvGasto.getAdapter().getItem(i);
//
//                gastosRepository.eliminarGasto(nuevogasto.getId(), new MoneyCallback<Boolean>() {
//                    @Override
//                    public void correcto(Boolean respuesta) {
//                    }
//
//                    @Override
//                    public void error(Exception exception) {
//                        Log.e("erroreliminar", exception.getMessage());
//                    }
//                });
//            }
//        });

        mostrarGastos();

        mostrarIngreso();

    }

    private void asociarElementos() {
        lvGasto = findViewById(R.id.lv_datos_gasto);
        lvIngreso = findViewById(R.id.lv_datos_ingreso);
    }

    private void actualizarListadoGasto(ArrayList<Gastos> datos){
        ArrayAdapter<Gastos> adapter = new ArrayAdapter<Gastos>(DatosActivity.this, android.R.layout.simple_list_item_1, datos);
        lvGasto.setAdapter(adapter);

    }

    private void actualizarListadoIngreso(ArrayList<Ingreso> datos){
        ArrayAdapter<Ingreso> adapter = new ArrayAdapter<Ingreso>(DatosActivity.this, android.R.layout.simple_list_item_1, datos);
        lvIngreso.setAdapter(adapter);

    }

    private void mostrarGastos(){
        gastosRepository.obtenerGasto(new MoneyCallback<ArrayList<Gastos>>() {
            @Override
            public void correcto(ArrayList<Gastos> respuesta) {
                actualizarListadoGasto(respuesta);
            }

            @Override
            public void error(Exception exception) {

            }
        });
    }

    private void mostrarIngreso(){
        ingresoRepository.obtenerIngreso(new MoneyCallback<ArrayList<Ingreso>>() {
            @Override
            public void correcto(ArrayList<Ingreso> respuesta) {
                actualizarListadoIngreso(respuesta);
            }

            @Override
            public void error(Exception exception) {

            }
        });
    }
}
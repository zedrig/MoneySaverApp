package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zedrig.moneysaverapp.R;
import com.zedrig.moneysaverapp.model.entity.Gastos;
import com.zedrig.moneysaverapp.model.entity.Ingreso;
import com.zedrig.moneysaverapp.model.network.MoneyCallback;
import com.zedrig.moneysaverapp.model.repository.GastosRepository;
import com.zedrig.moneysaverapp.model.repository.IngresoRepository;
import com.zedrig.moneysaverapp.model.repository.UsuarioRepository;
import com.zedrig.moneysaverapp.view.adapter.GastoAdatador;
import com.zedrig.moneysaverapp.view.adapter.IngresoAdaptador;

import java.util.ArrayList;

public class DatosActivity extends AppCompatActivity {

    private ListView lvGasto;
    private ListView lvIngreso;
    private TextView tvGastoValor;
    private TextView tvIngresoValor;
    private GastosRepository gastosRepository;
    private IngresoRepository ingresoRepository;
    private UsuarioRepository usuarioRepository;
    private int valorfinalingreso;
    private int valorfinalgasto;

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
    protected void onResume() {
        super.onResume();

        mostrarGastos();

        mostrarIngreso();

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

        mostrarGastos();

        mostrarIngreso();

        lvGasto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Gastos gastos = (Gastos) lvGasto.getAdapter().getItem(i);
                Intent ig = new Intent(DatosActivity.this, NuevoGastoActivity.class);
                ig.putExtra("gasto", gastos);
                startActivityForResult(ig, 100);
            }
        });

        lvGasto.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Gastos gastos = (Gastos) lvGasto.getAdapter().getItem(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(DatosActivity.this);
                builder.setTitle("Eliminar gasto")
                        .setMessage("¿Quieres eliminar este gasto de: $ "+gastos.getValor()+"?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                gastosRepository.eliminarGasto(gastos.getId(), new MoneyCallback<Boolean>() {
                                    @Override
                                    public void correcto(Boolean respuesta) {
                                        mostrarGastos();
                                    }

                                    @Override
                                    public void error(Exception exception) {

                                    }
                                });
                            }
                        }).setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

        lvIngreso.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ingreso ingreso = (Ingreso) lvIngreso.getAdapter().getItem(i);
                Intent ii = new Intent(DatosActivity.this, NuevoIngresoActivity.class);
                ii.putExtra("ingreso", ingreso);
                startActivityForResult(ii, 200);

            }
        });

        lvIngreso.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Ingreso ingreso = (Ingreso) lvIngreso.getAdapter().getItem(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(DatosActivity.this);
                builder.setTitle("Eliminar ingreso")
                        .setMessage("¿Quieres eliminar este ingreso de: $ "+ingreso.getValor()+"?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ingresoRepository.eliminarIngreso(ingreso.getId(), new MoneyCallback<Boolean>() {
                                    @Override
                                    public void correcto(Boolean respuesta) {
                                        mostrarIngreso();
                                    }

                                    @Override
                                    public void error(Exception exception) {

                                    }
                                });
                            }
                        }).setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });


    }

    private void asociarElementos() {
        lvGasto = findViewById(R.id.lv_datos_gasto);
        lvIngreso = findViewById(R.id.lv_datos_ingreso);
        tvGastoValor = findViewById(R.id.tv_gasto_valor);
        tvIngresoValor = findViewById(R.id.tv_ingreso_valor);
    }

    private void actualizarListadoGasto(ArrayList<Gastos> datos){

        GastoAdatador gastoAdatador = new GastoAdatador(this, datos);
        //ArrayAdapter<Gastos> adapter = new ArrayAdapter<Gastos>(DatosActivity.this, android.R.layout.simple_list_item_1, datos);
        lvGasto.setAdapter(gastoAdatador);

    }

    private void actualizarListadoIngreso(ArrayList<Ingreso> datos){

        IngresoAdaptador ingresoAdaptador = new IngresoAdaptador(this, datos);
        //ArrayAdapter<Ingreso> adapter = new ArrayAdapter<Ingreso>(DatosActivity.this, android.R.layout.simple_list_item_1, datos);
        lvIngreso.setAdapter(ingresoAdaptador);

    }

    private void mostrarGastos(){
        gastosRepository.obtenerGasto(new MoneyCallback<ArrayList<Gastos>>() {
            @Override
            public void correcto(ArrayList<Gastos> respuesta) {
                actualizarListadoGasto(respuesta);
                valorfinalgasto = calcularTotalgasto(respuesta);
                tvGastoValor.setText("$ "+valorfinalgasto);
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
                valorfinalingreso = calcularTotalingreso(respuesta);
                tvIngresoValor.setText("$ "+valorfinalingreso);
            }

            @Override
            public void error(Exception exception) {

            }
        });
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
}
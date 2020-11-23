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
import android.widget.ListView;

import com.zedrig.moneysaverapp.R;
import com.zedrig.moneysaverapp.model.entity.Categoria;
import com.zedrig.moneysaverapp.model.entity.Gastos;
import com.zedrig.moneysaverapp.model.network.MoneyCallback;
import com.zedrig.moneysaverapp.model.repository.GastosRepository;
import com.zedrig.moneysaverapp.view.adapter.GastoAdatador;

import java.util.ArrayList;

public class ListaCategoriaActivity extends AppCompatActivity {

    private ListView lvListaCat;
    private Categoria categoria;
    private GastosRepository gastosRepository;

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
        setContentView(R.layout.activity_lista_categoria);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gastosRepository = new GastosRepository(ListaCategoriaActivity.this);

        lvListaCat = findViewById(R.id.lv_lista_cat);

        categoria = (Categoria) getIntent().getSerializableExtra("categoria");

        mostrarGastoscat();


        lvListaCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Gastos gastos = (Gastos) lvListaCat.getAdapter().getItem(i);
                Intent ig = new Intent(ListaCategoriaActivity.this, NuevoGastoActivity.class);
                ig.putExtra("gasto", gastos);
                startActivityForResult(ig, 100);
            }
        });

        lvListaCat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Gastos gastos = (Gastos) lvListaCat.getAdapter().getItem(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(ListaCategoriaActivity.this);
                builder.setTitle("Eliminar gasto")
                        .setMessage("¿Quieres eliminar este gasto de: $ "+gastos.getValor()+"?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                gastosRepository.eliminarGasto(gastos.getId(), new MoneyCallback<Boolean>() {
                                    @Override
                                    public void correcto(Boolean respuesta) {
                                        mostrarGastoscat();
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

    private void mostrarGastoscat() {
        gastosRepository.obtenerGastocat(categoria.getNombre(), new MoneyCallback<ArrayList<Gastos>>() {
            @Override
            public void correcto(ArrayList<Gastos> respuesta) {
                actualizarListadoGasto(respuesta);
                Log.e("listagastos", respuesta.toString());
                Intent i = new Intent();
                i.putExtra("categoria", categoria);
                setResult(RESULT_OK, i);
            }

            @Override
            public void error(Exception exception) {

            }
        });
    }

    private void actualizarListadoGasto(ArrayList<Gastos> datos){

        GastoAdatador gastoAdatador = new GastoAdatador(this, datos);
        lvListaCat.setAdapter(gastoAdatador);

    }

}
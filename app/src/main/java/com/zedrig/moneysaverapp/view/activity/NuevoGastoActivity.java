package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.zedrig.moneysaverapp.model.entity.Categoria;
import com.zedrig.moneysaverapp.model.entity.Gastos;
import com.zedrig.moneysaverapp.model.entity.Ingreso;
import com.zedrig.moneysaverapp.model.repository.GastosRepository;
import com.zedrig.moneysaverapp.model.network.MoneyCallback;
import com.zedrig.moneysaverapp.R;
import com.zedrig.moneysaverapp.model.repository.IngresoRepository;
import com.zedrig.moneysaverapp.model.repository.UsuarioRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NuevoGastoActivity extends AppCompatActivity {

    private EditText etGasto;
    private Spinner spCategorias;
    private EditText etDescripcion;
    private Button btGasto;
    private TextView tvGasto;
    private GastosRepository gastosRepository;
    private IngresoRepository ingresoRepository;
    private UsuarioRepository usuarioRepository;
    private Gastos gastos;
    private int valorfinalingreso;
    private int valorfinalgasto;
    private int valorfinal;
    private int actualdia;
    private int maxdia;
    private int difdias;

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
        setContentView(R.layout.activity_nuevo_gasto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Calendar calendar = Calendar.getInstance();
        int res = calendar.getActualMaximum(Calendar.DATE);

        Date date = Calendar.getInstance().getTime(); // metodo para poner la hora actual
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); //
        DateFormat dia = new SimpleDateFormat("dd");


        //gastos = (Gastos) getIntent().getSerializableExtra("gasto");

        gastosRepository = new GastosRepository(NuevoGastoActivity.this);
        usuarioRepository = new UsuarioRepository(NuevoGastoActivity.this);
        ingresoRepository = new IngresoRepository(NuevoGastoActivity.this);

        asociarElementos();

        mostrarGastos();

        mostrarIngreso();

        PaginaPrincipalActivity paginaPrincipalActivity = new PaginaPrincipalActivity();


        usuarioRepository.obtenerCategorias(new MoneyCallback<ArrayList<Categoria>>() {
            @Override
            public void correcto(ArrayList<Categoria> respuesta) {
                actualizarLista(respuesta);
            }

            @Override
            public void error(Exception exception) {

            }
        });

        gastos = (Gastos) getIntent().getSerializableExtra("gasto");


        if (gastos == null) {

            btGasto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String categoria = spCategorias.getSelectedItem().toString();
                    String valor = etGasto.getText().toString();
                    double valorn = 0;
                    String descripcion = etDescripcion.getText().toString();
                    String fecha = dateFormat.format(date);
                    int max = Integer.MAX_VALUE;

                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    if (!valor.isEmpty()){
                        valorn = Double.parseDouble(valor);

                        if (valorn < valorfinal){

                            if (valorn < max){

                                int valorf = (int) valorn;
                                gastos = new Gastos(categoria, valorf, descripcion, auth.getUid(), fecha);

                                gastosRepository.agregarGasto(gastos, new MoneyCallback<Boolean>() {
                                    @Override
                                    public void correcto(Boolean respuesta) {
                                        Toast.makeText(NuevoGastoActivity.this, "Gasto ingresado", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void error(Exception exception) {
                                        Toast.makeText(NuevoGastoActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(NuevoGastoActivity.this, "Ingrese un gasto menor a: "+max+", máximo entero permitido", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(NuevoGastoActivity.this, "Su nuevo gasto es mayor a su saldo disponible: "+valorfinal, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(NuevoGastoActivity.this, "Ingrese un nuevo gasto", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{

            tvGasto.setText("Editar gasto");
            etGasto.setText(""+gastos.getValor());
            etDescripcion.setText(gastos.getDescripcion());
            btGasto.setText("Actualizar");

            btGasto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String categoria = spCategorias.getSelectedItem().toString();
                    String valor = etGasto.getText().toString();
                    double valorn = 0;
                    String descripcion = etDescripcion.getText().toString();
                    String fecha = dateFormat.format(date);
                    int max = Integer.MAX_VALUE;

                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    if (!valor.isEmpty()){
                        valorn = Double.parseDouble(valor);

                        if (valorn < valorfinal){

                            if (valorn < max){

                                int valorf = (int) valorn;
                                gastos.setValor(valorf);
                                gastos.setDescripcion(descripcion);
                                gastos.setCategoria(categoria);

                                gastosRepository.editarGasto(gastos, new MoneyCallback<Boolean>() {
                                    @Override
                                    public void correcto(Boolean respuesta) {
                                        Toast.makeText(NuevoGastoActivity.this, "Gasto actualizado", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent();
                                        i.putExtra("gasto", gastos);
                                        setResult(RESULT_OK, i);
                                        finish();
                                    }

                                    @Override
                                    public void error(Exception exception) {
                                        Toast.makeText(NuevoGastoActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(NuevoGastoActivity.this, "Ingrese un gasto menor a: "+max+", máximo entero permitido", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(NuevoGastoActivity.this, "Su nuevo gasto es mayor a su saldo disponible: "+valorfinal, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(NuevoGastoActivity.this, "Ingrese un nuevo gasto", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }




    }

    private void asociarElementos() {
        etGasto = findViewById(R.id.et_gasto);
        spCategorias = findViewById(R.id.sp_categorias);
        etDescripcion = findViewById(R.id.et_descripcion);
        btGasto = findViewById(R.id.bt_nuevo_gasto);
        tvGasto = findViewById(R.id.tv_nuevo_gasto);
    }

    private void actualizarLista(ArrayList<Categoria> datos){
        ArrayAdapter<Categoria> adapter = new ArrayAdapter<Categoria>(this, R.layout.support_simple_spinner_dropdown_item, datos);
        spCategorias.setAdapter(adapter);
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
    }

    private void mostrarIngreso(){
        ingresoRepository.obtenerIngreso(new MoneyCallback<ArrayList<Ingreso>>() {
            @Override
            public void correcto(ArrayList<Ingreso> respuesta) {
                valorfinalingreso = calcularTotalingreso(respuesta);
                valorfinal = valorfinalingreso - valorfinalgasto;
            }

            @Override
            public void error(Exception exception) {

            }
        });
    }

    private void mostrarGastos(){
        gastosRepository.escucharGasto(new MoneyCallback<ArrayList<Gastos>>() {
            @Override
            public void correcto(ArrayList<Gastos> respuesta) {
                valorfinalgasto = calcularTotalgasto(respuesta);
            }

            @Override
            public void error(Exception exception) {

            }
        });
    }

}
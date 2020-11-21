package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.zedrig.moneysaverapp.model.entity.Gastos;
import com.zedrig.moneysaverapp.model.entity.Ingreso;
import com.zedrig.moneysaverapp.model.repository.GastosRepository;
import com.zedrig.moneysaverapp.model.repository.IngresoRepository;
import com.zedrig.moneysaverapp.model.network.MoneyCallback;
import com.zedrig.moneysaverapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NuevoIngresoActivity extends AppCompatActivity {

    private EditText etIngreso;
    private Button btIngreso;
    private Ingreso ingreso;
    private IngresoRepository ingresoRepository;
    private GastosRepository gastosRepository;
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
        setContentView(R.layout.activity_nuevo_ingreso);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ingresoRepository = new IngresoRepository(NuevoIngresoActivity.this);
        gastosRepository = new GastosRepository(NuevoIngresoActivity.this);

        asociarElementos();

        btIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth auth = FirebaseAuth.getInstance();

                String valor = etIngreso.getText().toString();

                double valorn = 0;

                double valorsum = 0;

                int max = Integer.MAX_VALUE;


                if (!valor.isEmpty()){
                    valorn = Double.parseDouble(valor);

                    valorsum = valorn + valorfinal;

                    if (valorsum < max){

                        int valorf = (int) valorn;
                        ingreso = new Ingreso(valorf, auth.getUid());

                        ingresoRepository.agregarIngreso(ingreso, new MoneyCallback<Boolean>() {
                            @Override
                            public void correcto(Boolean respuesta) {
                                Toast.makeText(NuevoIngresoActivity.this, "Nuevo ingreso agregado", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void error(Exception exception) {

                            }
                        });
                    }else{
                        Toast.makeText(NuevoIngresoActivity.this, "Agregue un ingreso menor a: "+max+", máximo entero permitido", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(NuevoIngresoActivity.this, "Agregue un nuevo ingreso", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void asociarElementos() {

        etIngreso = findViewById(R.id.et_ingreso);
        btIngreso = findViewById(R.id.bt_ingreso);

    }

    private int calcularTotalingreso(ArrayList<Ingreso> datos) {
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
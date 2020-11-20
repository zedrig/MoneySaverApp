package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.zedrig.moneysaverapp.model.entity.Categoria;
import com.zedrig.moneysaverapp.model.entity.Gastos;
import com.zedrig.moneysaverapp.model.repository.GastosRepository;
import com.zedrig.moneysaverapp.model.network.MoneyCallback;
import com.zedrig.moneysaverapp.R;
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
    private GastosRepository gastosRepository;
    private UsuarioRepository usuarioRepository;
    private Gastos gastos;

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
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); //
        DateFormat dia = new SimpleDateFormat("dd");


        gastos = (Gastos) getIntent().getSerializableExtra("gasto");

        gastosRepository = new GastosRepository(NuevoGastoActivity.this);
        usuarioRepository = new UsuarioRepository(NuevoGastoActivity.this);

        asociarElementos();

        usuarioRepository.obtenerCategorias(new MoneyCallback<ArrayList<Categoria>>() {
            @Override
            public void correcto(ArrayList<Categoria> respuesta) {
                actualizarLista(respuesta);
            }

            @Override
            public void error(Exception exception) {

            }
        });

        btGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String categoria = spCategorias.getSelectedItem().toString();
                double valor = Double.parseDouble(etGasto.getText().toString());
                String descripcion = etDescripcion.getText().toString();
                String fecha = dateFormat.format(date);

                FirebaseAuth auth = FirebaseAuth.getInstance();

                gastos = new Gastos(categoria, valor, descripcion, auth.getUid(), fecha);

                gastosRepository.agregarGasto(gastos, new MoneyCallback<Boolean>() {
                    @Override
                    public void correcto(Boolean respuesta) {
                        Toast.makeText(NuevoGastoActivity.this, "gasto ingresado", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void error(Exception exception) {
                        Toast.makeText(NuevoGastoActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }

    private void asociarElementos() {
        etGasto = findViewById(R.id.et_gasto);
        spCategorias = findViewById(R.id.sp_categorias);
        etDescripcion = findViewById(R.id.et_descripcion);
        btGasto = findViewById(R.id.bt_nuevo_gasto);
    }

    private void actualizarLista(ArrayList<Categoria> datos){
        ArrayAdapter<Categoria> adapter = new ArrayAdapter<Categoria>(this, R.layout.support_simple_spinner_dropdown_item, datos);
        spCategorias.setAdapter(adapter);
    }

    public int diasMes(int mes) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.MONTH, mes - 1);
        return gc.getActualMaximum(Calendar.DATE);
    }

}
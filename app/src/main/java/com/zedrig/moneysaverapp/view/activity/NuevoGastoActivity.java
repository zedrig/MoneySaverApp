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
import com.zedrig.moneysaverapp.model.entity.Gastos;
import com.zedrig.moneysaverapp.model.repository.GastosRepository;
import com.zedrig.moneysaverapp.model.network.MoneyCallback;
import com.zedrig.moneysaverapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NuevoGastoActivity extends AppCompatActivity {

    private EditText etGasto;
    private Spinner spCategorias;
    private EditText etDescripcion;
    private Button btGasto;
    private GastosRepository gastosRepository;
    private Gastos gastos;

    String categorias[] = new String[]{"Salud","Transporte","Alimento"};

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

//        Calendar calendar = Calendar.getInstance();

        Date date = Calendar.getInstance().getTime(); // metodo para poner la hora actual
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss"); //


        gastos = (Gastos) getIntent().getSerializableExtra("gasto");

        gastosRepository = new GastosRepository(NuevoGastoActivity.this);

        asociarElementos();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.support_simple_spinner_dropdown_item, categorias);
        spCategorias.setAdapter(adapter);

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
}
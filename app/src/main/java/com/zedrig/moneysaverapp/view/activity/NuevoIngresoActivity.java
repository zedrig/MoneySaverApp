package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.zedrig.moneysaverapp.model.entity.Ingreso;
import com.zedrig.moneysaverapp.model.repository.IngresoRepository;
import com.zedrig.moneysaverapp.model.network.MoneyCallback;
import com.zedrig.moneysaverapp.R;

public class NuevoIngresoActivity extends AppCompatActivity {

    private EditText etIngreso;
    private Button btIngreso;
    private Ingreso ingreso;
    private IngresoRepository ingresoRepository;

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

        asociarElementos();

        btIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth auth = FirebaseAuth.getInstance();

                String valor = etIngreso.getText().toString();

                int valorn = 0;

                if (!valor.isEmpty()){
                    valorn = Integer.parseInt(valor);

                    ingreso = new Ingreso(valorn, auth.getUid());

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
                    Toast.makeText(NuevoIngresoActivity.this, "Agregue un nuevo ingreso", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void asociarElementos() {

        etIngreso = findViewById(R.id.et_ingreso);
        btIngreso = findViewById(R.id.bt_ingreso);

    }
}
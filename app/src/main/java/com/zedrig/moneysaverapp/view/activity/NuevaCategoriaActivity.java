package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.zedrig.moneysaverapp.R;
import com.zedrig.moneysaverapp.model.entity.Categoria;
import com.zedrig.moneysaverapp.model.entity.Gastos;
import com.zedrig.moneysaverapp.model.network.MoneyCallback;
import com.zedrig.moneysaverapp.model.repository.UsuarioRepository;

import java.util.ArrayList;

public class NuevaCategoriaActivity extends AppCompatActivity {

    private EditText etCategoria;
    private Button btCategoria;
    private ListView lvCategoria;
    private Categoria categoria;
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
        setContentView(R.layout.activity_nueva_categoria);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usuarioRepository = new UsuarioRepository(NuevaCategoriaActivity.this);

        asociarElementos();

        btCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth auth = FirebaseAuth.getInstance();

                String nombre = etCategoria.getText().toString();

                categoria = new Categoria(nombre, auth.getUid());

                if (!nombre.isEmpty()){
                    usuarioRepository.agregarCategoria(categoria, new MoneyCallback<Boolean>() {
                        @Override
                        public void correcto(Boolean respuesta) {
                            Toast.makeText(NuevaCategoriaActivity.this, "Categoria agregada", Toast.LENGTH_SHORT).show();
                            etCategoria.setText("");
                        }

                        @Override
                        public void error(Exception exception) {

                        }
                    });
                }else{
                    Toast.makeText(NuevaCategoriaActivity.this, "Ingrese un nombre para su categoria", Toast.LENGTH_SHORT).show();
                }


            }
        });

        usuarioRepository.escucharCategorias(new MoneyCallback<ArrayList<Categoria>>() {
            @Override
            public void correcto(ArrayList<Categoria> respuesta) {
                actualizarListado(respuesta);
            }

            @Override
            public void error(Exception exception) {

            }
        });

    }

    private void asociarElementos() {
        etCategoria = findViewById(R.id.et_categoria);
        btCategoria = findViewById(R.id.bt_categoria);
        lvCategoria = findViewById(R.id.lv_categoria);
    }

    private void actualizarListado(ArrayList<Categoria> datos){
        ArrayAdapter<Categoria> adapter = new ArrayAdapter<Categoria>(NuevaCategoriaActivity.this, android.R.layout.simple_list_item_1, datos);
        lvCategoria.setAdapter(adapter);

    }
}
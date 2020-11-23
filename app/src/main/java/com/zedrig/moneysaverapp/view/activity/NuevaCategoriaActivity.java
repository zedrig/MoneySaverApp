package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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


        lvCategoria.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Categoria categoria = (Categoria) lvCategoria.getAdapter().getItem(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(NuevaCategoriaActivity.this);
                builder.setTitle("Eliminar categoria")
                        .setMessage("¿Quieres eliminar la categoria "+categoria.getNombre()+"?")
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                usuarioRepository.eliminarCategoria(categoria.getId(), new MoneyCallback<Boolean>() {
                                    @Override
                                    public void correcto(Boolean respuesta) {
                                        Toast.makeText(NuevaCategoriaActivity.this, "Categoria eliminada", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void error(Exception exception) {

                                    }
                                });
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
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

        lvCategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Categoria categoria = (Categoria) lvCategoria.getAdapter().getItem(i);
                Intent icat = new Intent(NuevaCategoriaActivity.this, ListaCategoriaActivity.class);
                icat.putExtra("categoria", categoria);
                startActivityForResult(icat, 300);
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
package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zedrig.moneysaverapp.R;
import com.zedrig.moneysaverapp.model.entity.Usuario;
import com.zedrig.moneysaverapp.model.network.MoneyCallback;
import com.zedrig.moneysaverapp.model.repository.UsuarioRepository;

public class PerfilActivity extends AppCompatActivity {

    private UsuarioRepository usuarioRepository;
    private TextView tvNombre;
    private TextView tvCorreo;
    private ListView lvPerfil;

    String vectorOpc [] = new String[] {"Actualizar nombre","Actualizar correo"};

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
        setContentView(R.layout.activity_perfil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Perfil");

        usuarioRepository = new UsuarioRepository(PerfilActivity.this);

        asociarElementos();

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, vectorOpc);

        lvPerfil.setAdapter(adaptador);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarNombreCorreo();
    }

    private void mostrarNombreCorreo() {
        usuarioRepository.obtenerUsuario(new MoneyCallback<Usuario>() {
            @Override
            public void correcto(Usuario respuesta) {
                String nombre = respuesta.getNombre();
                String correo = respuesta.getCorreo();
                tvNombre.setText(nombre);
                tvCorreo.setText(correo);

            }

            @Override
            public void error(Exception exception) {

            }
        });
    }

    private void asociarElementos() {
        tvNombre = findViewById(R.id.tv_nombre);
        tvCorreo = findViewById(R.id.tv_correo_perfil);
        lvPerfil = findViewById(R.id.lv_perfil);
    }
}
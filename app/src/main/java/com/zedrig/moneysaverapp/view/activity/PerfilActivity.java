package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zedrig.moneysaverapp.CambiarNombreActivity;
import com.zedrig.moneysaverapp.R;
import com.zedrig.moneysaverapp.model.entity.Usuario;
import com.zedrig.moneysaverapp.model.network.MoneyCallback;
import com.zedrig.moneysaverapp.model.repository.UsuarioRepository;

public class PerfilActivity extends AppCompatActivity {

    private UsuarioRepository usuarioRepository;
    private TextView tvNombre;
    private TextView tvCorreo;
    private ListView lvPerfil;

    String vectorOpc [] = new String[] {"Actualizar nombre","Actualizar correo", "Actualizar contraseña"};

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

        lvPerfil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Actualizar nombre")){
                    Intent in = new Intent(PerfilActivity.this, CambiarNombreActivity.class);
                    startActivity(in);
                }else{
                    if (adapterView.getItemAtPosition(i).equals("Actualizar correo")){
                        Intent ic = new Intent(PerfilActivity.this, CambiarCorreoActivity.class);
                        startActivity(ic);
                    }else{
                        if (adapterView.getItemAtPosition(i).equals("Actualizar contraseña")){
                            Intent ip = new Intent(PerfilActivity.this, CambiarPassActivity.class);
                            startActivity(ip);
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //mostrarNombreCorreo();
        mostrarNombreCorreoFA();
    }
    // PENDIENTE!! CREAR DIALOGS PARA CAMBIAR NOMBRE Y CORREO!!1

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

    private void mostrarNombreCorreoFA(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            tvNombre.setText(name);
            tvCorreo.setText(email);
        }
    }

    private void asociarElementos() {
        tvNombre = findViewById(R.id.tv_nombre);
        tvCorreo = findViewById(R.id.tv_correo_perfil);
        lvPerfil = findViewById(R.id.lv_perfil);
    }
}
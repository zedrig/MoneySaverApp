package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zedrig.moneysaverapp.R;
import com.zedrig.moneysaverapp.model.entity.Usuario;

public class RegistrarUsuarioActivity extends AppCompatActivity {

    private EditText etNombre;
    private EditText etCorreo;
    private EditText etPass1;
    private EditText etPass2;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        asociarElementos();

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

    }

    private void asociarElementos() {
        etNombre = findViewById(R.id.et_nombreregistro);
        etCorreo = findViewById(R.id.et_emailregistro);
        etPass1 = findViewById(R.id.et_password_registro);
        etPass2 = findViewById(R.id.et_password_registro2);
    }

    public void loginUsuario(View view) {
        finish();
    }

    public void registrarNuevoUsuario(View view) {

        String nombre = etNombre.getText().toString();
        String correo = etCorreo.getText().toString();
        String pass1 = etPass1.getText().toString();
        String pass2 = etPass2.getText().toString();

        Usuario usuario = new Usuario(nombre, correo);

        if (!nombre.isEmpty() && !correo.isEmpty() && !pass1.isEmpty() && !pass2.isEmpty()){

            if (pass1.length() >= 6 && pass2.length() >=6){

                if (pass1.equals(pass2)){

                    auth.createUserWithEmailAndPassword(usuario.getCorreo(), pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                firestore.collection("users").document(auth.getUid()).set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            finish();
                                        }else{

                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(RegistrarUsuarioActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }

            }
            else {
                Toast.makeText(this, "La contraseña debe ser de al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}
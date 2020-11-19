package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zedrig.moneysaverapp.R;

public class MainActivity extends AppCompatActivity {
    private EditText etCorreo;
    private EditText etPassword;
    private Button btLogin;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){
            Intent i = new Intent(MainActivity.this, PaginaPrincipalActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        asociarElementos();

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = etCorreo.getText().toString();
                String password = etPassword.getText().toString();

                auth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent i = new Intent(MainActivity.this,PaginaPrincipalActivity.class);
                            startActivity(i);
                        }
                    }
                });
            }
        });

    }

    private void asociarElementos() {
        etCorreo = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btLogin = findViewById(R.id.bt_login);
    }

    public void registrarUsuario(View view) {
        Intent i = new Intent(MainActivity.this, RegistrarUsuarioActivity.class);
        startActivity(i);
    }
}
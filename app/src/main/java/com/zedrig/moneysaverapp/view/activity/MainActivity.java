package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private String correo = "";
    private String password = "";

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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        asociarElementos();

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correo = etCorreo.getText().toString();
                password = etPassword.getText().toString();

                if (!correo.isEmpty() && !password.isEmpty()){
                    loginUser();
                }
                else{
                    Toast.makeText(MainActivity.this, "Ingrese sus datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loginUser() {
        auth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent i = new Intent(MainActivity.this,PaginaPrincipalActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Toast.makeText(MainActivity.this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
                }
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

    public void resetPass(View view) {

        Intent i = new Intent(MainActivity.this, ResetPasswordActivity.class);
        startActivity(i);

    }
}
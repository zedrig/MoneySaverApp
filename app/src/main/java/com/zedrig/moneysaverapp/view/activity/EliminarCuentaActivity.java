package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zedrig.moneysaverapp.R;

public class EliminarCuentaActivity extends AppCompatActivity {

    private EditText etBorrar;
    private Button btBorrar;
    private FirebaseUser user;
    private String email;

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
        setContentView(R.layout.activity_eliminar_cuenta);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etBorrar = findViewById(R.id.et_borrar_cuenta);
        btBorrar = findViewById(R.id.bt_borrar_cuenta);


        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }

        btBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pass = etBorrar.getText().toString();

                if (!pass.isEmpty()){

                    AuthCredential credential = EmailAuthProvider.getCredential(email,pass);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(EliminarCuentaActivity.this);
                                builder.setTitle("¿Seguro que quieres eliminar tu cuenta?")
                                        .setMessage("Esta acción es irreversible")
                                        .setNegativeButton("Cancelar", null)
                                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Intent i = new Intent(EliminarCuentaActivity.this, MainActivity.class);
                                                            startActivity(i);
                                                            Toast.makeText(EliminarCuentaActivity.this, "Su cuenta se ha eliminado satisfactoriamente", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }else {
                                Toast.makeText(EliminarCuentaActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(EliminarCuentaActivity.this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
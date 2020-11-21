package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.zedrig.moneysaverapp.R;

public class CambiarCorreoActivity extends AppCompatActivity {

    private EditText etCorreo;
    private EditText etPass;
    private Button btCorreo;
    private String email;
    private FirebaseUser user;

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
        setContentView(R.layout.activity_cambiar_correo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etCorreo = findViewById(R.id.et_nuevo_correo);
        etPass = findViewById(R.id.et_pass_check);
        btCorreo = findViewById(R.id.bt_nuevo_correo);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            email = user.getEmail();

            etCorreo.setText(email);
        }

        btCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nuevocorreo = etCorreo.getText().toString();
                String contraseña = etPass.getText().toString();

                AuthCredential credential = EmailAuthProvider.getCredential(email,contraseña);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.updateEmail(nuevocorreo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(CambiarCorreoActivity.this, "Correo actualizado", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
                    }
                });
            }
        });

    }
}
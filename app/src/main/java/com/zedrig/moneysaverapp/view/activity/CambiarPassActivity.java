package com.zedrig.moneysaverapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class CambiarPassActivity extends AppCompatActivity {

    private EditText etPass1;
    private EditText etPass2;
    private Button btPass;
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
        setContentView(R.layout.activity_cambiar_pass);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etPass1 = findViewById(R.id.et_nueva_pass);
        etPass2 = findViewById(R.id.et_nueva_pass2);
        btPass = findViewById(R.id.bt_nueva_pass);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }

        btPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String passactual = etPass1.getText().toString();
                String passnueva = etPass2.getText().toString();

                if (!passactual.isEmpty() && !passnueva.isEmpty()){
                    AuthCredential credential = EmailAuthProvider.getCredential(email,passactual);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(passnueva).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(CambiarPassActivity.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }else{
                                Toast.makeText(CambiarPassActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(CambiarPassActivity.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
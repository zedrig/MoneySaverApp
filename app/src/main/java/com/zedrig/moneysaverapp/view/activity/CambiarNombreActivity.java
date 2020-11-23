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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zedrig.moneysaverapp.R;
import com.zedrig.moneysaverapp.model.entity.Usuario;

public class CambiarNombreActivity extends AppCompatActivity {

    private EditText etNombre;
    private Button btNombre;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

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
        setContentView(R.layout.activity_cambiar_nombre);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNombre = findViewById(R.id.et_nuevo_nombre);
        btNombre = findViewById(R.id.bt_nuevo_nombre);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            String nombre = user.getDisplayName();

            etNombre.setText(nombre);
        }

        btNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombrenuevo = etNombre.getText().toString();

                if (!nombrenuevo.isEmpty()){
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nombrenuevo)
                            .build();

                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                String correo = user.getEmail();
                                Usuario usuario = new Usuario(nombrenuevo,correo);
                                firestore.collection("users").document(auth.getUid()).set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(CambiarNombreActivity.this, "Nombre actualizado", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        }
                    });
                }else {
                    Toast.makeText(CambiarNombreActivity.this, "Ingrese un nombre", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
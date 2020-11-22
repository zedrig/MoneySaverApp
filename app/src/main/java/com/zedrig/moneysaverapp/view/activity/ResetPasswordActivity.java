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
import com.zedrig.moneysaverapp.R;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText et_resetpass;
    private Button bt_resetpass;

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
        setContentView(R.layout.activity_reset_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Reestablecer contraseña");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        et_resetpass = findViewById(R.id.et_reset_pass_correo);
        bt_resetpass = findViewById(R.id.bt_reset_pass_correo);

        bt_resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String correo = et_resetpass.getText().toString();

                if (!correo.isEmpty()){
                    auth.sendPasswordResetEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this, "Correo de restauración enviado", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }else {
                    Toast.makeText(ResetPasswordActivity.this, "Ingrese su correo", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
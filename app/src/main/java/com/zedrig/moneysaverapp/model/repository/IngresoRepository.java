package com.zedrig.moneysaverapp.model.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.zedrig.moneysaverapp.model.entity.Gastos;
import com.zedrig.moneysaverapp.model.entity.Ingreso;
import com.zedrig.moneysaverapp.model.network.MoneyCallback;

import java.util.ArrayList;

public class IngresoRepository {

    private Context context;
    private ArrayList<Gastos> lista;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private Ingreso ingreso;

    public IngresoRepository(Context context) {
        this.context = context;
        this.lista = new ArrayList<>();
        this.firestore = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    public void agregarIngreso(Ingreso ingreso, MoneyCallback<Boolean> respuesta){

        firestore.collection("users").document(auth.getUid()).collection("ingreso").add(ingreso).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    respuesta.correcto(true);
                }else{
                    respuesta.error(task.getException());
                }
            }
        });
    }

    public void obtenerIngreso(MoneyCallback<ArrayList <Ingreso>> respuesta){
        firestore.collection("users").document(auth.getUid()).collection("ingreso").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot item:task.getResult().getDocuments()) {
                        Ingreso ingresodoc = item.toObject(Ingreso.class);
                        Log.d("testeo", item.getData().toString());
                    }
                }
            }
        });
    }

}
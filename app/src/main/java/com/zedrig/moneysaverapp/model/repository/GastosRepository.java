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
import com.zedrig.moneysaverapp.model.network.MoneyCallback;

import java.util.ArrayList;

public class GastosRepository {

    private Context context;
    private ArrayList<Gastos> listado;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    public GastosRepository(Context context) {
        this.context = context;
        this.listado = new ArrayList<>();
        this.firestore = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();

    }

    public void agregarGasto(Gastos gastos, MoneyCallback<Boolean> respuesta){

        firestore.collection("users").document(auth.getUid()).collection("gastos").add(gastos).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
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

    public void obtenerGastos(final MoneyCallback<ArrayList<Gastos>> respuesta) {
        firestore.collection("users").document(auth.getUid()).collection("gastos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    listado.clear();
                    for (DocumentSnapshot item:task.getResult().getDocuments()) {
                        final Gastos gastos = item.toObject(Gastos.class);
                        gastos.setId(item.getId());
                        listado.add(gastos);
                        Log.d("gastotest", item.getData().toString());
                    }
                    respuesta.correcto(listado);
                }else{

                }
            }
        });
    }
}

package com.zedrig.moneysaverapp.model.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.zedrig.moneysaverapp.model.entity.Gastos;
import com.zedrig.moneysaverapp.model.entity.Ingreso;
import com.zedrig.moneysaverapp.model.network.MoneyCallback;

import java.util.ArrayList;

public class GastosRepository {

    private Context context;
    private ArrayList<Gastos> lista;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    public GastosRepository(Context context) {
        this.context = context;
        this.lista = new ArrayList<>();
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

    public void obtenerGasto(MoneyCallback<ArrayList <Gastos>> respuesta){
        firestore.collection("users").document(auth.getUid()).collection("gastos").orderBy("fecha", Query.Direction.DESCENDING).limit(5).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    lista.clear();
                    for (DocumentSnapshot item:task.getResult().getDocuments()) {
                        Gastos gastodoc = item.toObject(Gastos.class);
                        Log.d("testeogastos", item.getData().toString());
                        lista.add(gastodoc);
                    }
                }respuesta.correcto(lista);
            }
        });
    }

    public void escucharGasto(MoneyCallback<ArrayList<Gastos>> respuesta){
        firestore.collection("users").document(auth.getUid()).collection("gastos").orderBy("fecha", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error==null){
                    lista.clear();
                    for (DocumentSnapshot item:value.getDocuments()) {
                        Gastos gastodoc = item.toObject(Gastos.class);
                        Log.d("testeogastos", item.getData().toString());
                        lista.add(gastodoc);
                    }
                }respuesta.correcto(lista);
            }
        });
    }

    public void escucharGasto5(MoneyCallback<ArrayList<Gastos>> respuesta){
        firestore.collection("users").document(auth.getUid()).collection("gastos").orderBy("fecha", Query.Direction.DESCENDING).limit(5).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error==null){
                    lista.clear();
                    for (DocumentSnapshot item:value.getDocuments()) {
                        Gastos gastodoc = item.toObject(Gastos.class);
                        Log.d("testeogastos", item.getData().toString());
                        lista.add(gastodoc);
                    }
                }respuesta.correcto(lista);
            }
        });
    }
}

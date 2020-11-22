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

public class IngresoRepository {

    private Context context;
    private ArrayList<Ingreso> lista;
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
        firestore.collection("users").document(auth.getUid()).collection("ingreso").orderBy("fecha", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    lista.clear();
                    for (DocumentSnapshot item:task.getResult().getDocuments()) {
                        Ingreso ingresodoc = item.toObject(Ingreso.class);
                        ingresodoc.setId(item.getId());
                        Log.d("testeoingreso", item.getData().toString());
                        lista.add(ingresodoc);
                    }
                }respuesta.correcto(lista);
            }
        });
    }
    public void escucharIngreso(MoneyCallback<ArrayList<Ingreso>> respuesta){
        firestore.collection("users").document(auth.getUid()).collection("ingreso").orderBy("fecha", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error==null){
                    lista.clear();
                    for (DocumentSnapshot item:value.getDocuments()) {
                        Ingreso ingresodoc = item.toObject(Ingreso.class);
                        ingresodoc.setId(item.getId());
                        Log.d("testeoingreso", item.getData().toString());
                        lista.add(ingresodoc);
                    }
                }respuesta.correcto(lista);
            }
        });
    }

    public void eliminarIngreso(String id, MoneyCallback<Boolean> respuesta){
        firestore.collection("users").document(auth.getUid()).collection("ingreso")
                .document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    respuesta.correcto(true);
                }
            }
        });
    }

    public void editarIngreso(Ingreso ingreso, MoneyCallback<Boolean> respuesta){

        firestore.collection("users").document(auth.getUid()).collection("ingreso")
                .document(ingreso.getId()).set(ingreso).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    respuesta.correcto(true);
                }else{
                    respuesta.error(task.getException());
                }
            }
        });
    }

}
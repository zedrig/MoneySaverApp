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
import com.zedrig.moneysaverapp.model.entity.Categoria;
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
        firestore.collection("users").document(auth.getUid()).collection("gastos").orderBy("fecha", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    lista.clear();
                    for (DocumentSnapshot item:task.getResult().getDocuments()) {
                        Gastos gastodoc = item.toObject(Gastos.class);
                        gastodoc.setId(item.getId());
                        Log.d("testeogastos", item.getData().toString());
                        lista.add(gastodoc);
                    }
                }respuesta.correcto(lista);
            }
        });
    }

    // not being used yet
    public void obtenerGastohoy(String dia, String mes, String year, MoneyCallback<ArrayList<Gastos>> respuesta){
        firestore.collection("users").document(auth.getUid()).collection("gastos")
                .whereEqualTo("dia", dia).whereEqualTo("mes", mes).whereEqualTo("year", year)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {//REVISAR ESTO!!!
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    lista.clear();
                    for (DocumentSnapshot item:task.getResult().getDocuments()) {
                        Gastos gastodoc = item.toObject(Gastos.class);
                        gastodoc.setId(item.getId());
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
                        gastodoc.setId(item.getId());
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
                        gastodoc.setId(item.getId()); //asignamos ID de firestore al ID de nuestro objeto
                        Log.d("testeogastos", item.getData().toString());
                        lista.add(gastodoc);
                    }
                }respuesta.correcto(lista);
            }
        });
    }

    public void eliminarGasto(String id, MoneyCallback<Boolean> respuesta){
        firestore.collection("users").document(auth.getUid()).collection("gastos")
                .document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    respuesta.correcto(true);
                }
            }
        });
    }

    public void editarGasto(Gastos gastos, MoneyCallback<Boolean> respuesta){

        firestore.collection("users").document(auth.getUid()).collection("gastos")
                .document(gastos.getId()).set(gastos).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    //obtener los gastos que tenga la categoria seleccionada
    public void obtenerGastocat(String categoria, MoneyCallback<ArrayList <Gastos>> respuesta){

        firestore.collection("users").document(auth.getUid())
                .collection("gastos").whereEqualTo("categoria", categoria)
                .orderBy("fecha", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    lista.clear();
                    for (DocumentSnapshot item:task.getResult().getDocuments()) {
                        Gastos gastodoc = item.toObject(Gastos.class);
                        gastodoc.setId(item.getId());
                        Log.d("testeocats", item.getData().toString());
                        lista.add(gastodoc);
                    }
                }respuesta.correcto(lista);
            }
        });
    }

}

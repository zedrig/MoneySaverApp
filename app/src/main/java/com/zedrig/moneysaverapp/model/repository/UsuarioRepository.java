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
import com.google.firebase.firestore.QuerySnapshot;
import com.zedrig.moneysaverapp.model.entity.Categoria;
import com.zedrig.moneysaverapp.model.entity.Ingreso;
import com.zedrig.moneysaverapp.model.network.MoneyCallback;

import java.util.ArrayList;

public class UsuarioRepository {

    private Context context;
    private ArrayList<Categoria> lista;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    public UsuarioRepository(Context context) {
        this.context = context;
        this.lista = new ArrayList<>();
        this.firestore = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    public void agregarCategoria(Categoria categoria, MoneyCallback<Boolean> respuesta){

        firestore.collection("users").document(auth.getUid()).collection("categoria").add(categoria).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
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

    public void obtenerCategorias(MoneyCallback<ArrayList <Categoria>> respuesta){
        firestore.collection("users").document(auth.getUid()).collection("categoria").orderBy("nombre").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    lista.clear();
                    for (DocumentSnapshot item:task.getResult().getDocuments()) {
                        Categoria categoriadoc = item.toObject(Categoria.class);
                        Log.d("testeocategoria", item.getData().toString());
                        lista.add(categoriadoc);
                    }
                }respuesta.correcto(lista);
            }
        });
    }

    public void escucharCategorias(MoneyCallback<ArrayList<Categoria>> respuesta){
        firestore.collection("users").document(auth.getUid()).collection("categoria").orderBy("nombre").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error==null){
                    lista.clear();
                    for (DocumentSnapshot item:value.getDocuments()) {
                        Categoria categoriadoc = item.toObject(Categoria.class);
                        Log.d("testeocategoria", item.getData().toString());
                        lista.add(categoriadoc);
                    }
                }respuesta.correcto(lista);
            }
        });
    }

}

package com.zedrig.moneysaverapp.model.repository;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.Money;
import com.zedrig.moneysaverapp.model.entity.Categoria;
import com.zedrig.moneysaverapp.model.entity.Gastos;
import com.zedrig.moneysaverapp.model.entity.Ingreso;
import com.zedrig.moneysaverapp.model.entity.Usuario;
import com.zedrig.moneysaverapp.model.network.MoneyCallback;

import java.util.ArrayList;

public class UsuarioRepository {

    private Context context;
    private ArrayList<Categoria> lista;
    private ArrayList<Categoria> listaingreso;
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
                        categoriadoc.setId(item.getId());
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
                        categoriadoc.setId(item.getId());
                        Log.d("testeocategoria", item.getData().toString());
                        lista.add(categoriadoc);
                    }
                }respuesta.correcto(lista);
            }
        });
    }

    public void obtenerUsuario(MoneyCallback<Usuario> respuesta){
        firestore.collection("users").document(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Usuario usuario = task.getResult().toObject(Usuario.class);
                    respuesta.correcto(usuario);
                    Log.d("testeousuario", String.valueOf(usuario));
                }
            }
        });
    }

    public void eliminarUsuario(String id, MoneyCallback<Boolean> respuesta){
        firestore.collection("users").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    respuesta.correcto(true);
                }
            }
        });
    }

    public void eliminarCategoria(String id, MoneyCallback<Boolean> respuesta){
        firestore.collection("users").document(auth.getUid()).collection("categoria")
                .document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    respuesta.correcto(true);
                }
            }
        });
    }
}

package com.example.jawabanuasrevandra;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseController extends Application {
    public static FirebaseDatabase firebaseDatabase;
    public static FirebaseAuth firebaseAuth;
    public static DatabaseReference userReference;
    public static DatabaseReference beritaReference;
    public static String currentUsername;


    @Override
    public void onCreate() {
        super.onCreate();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("User");
        beritaReference = firebaseDatabase.getReference("Berita");
        beritaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    for (DataSnapshot currentData : snapshot.getChildren()){
//                        Berita berita = new Berita();
//                        berita.setKey(currentData.getKey());
//                        berita.setCategory(currentData.child("category").getValue().toString());
//                        berita.setUmur(Integer.parseInt(currentData.child("umur").getValue().toString()));
//                        berita.setJudul(currentData.child("judul").getValue().toString());
//                        berita.setContent(currentData.child("content").getValue().toString());
//                        berita.setEmail(currentData.child("email").getValue().toString());
                        Berita berita = currentData.getValue(Berita.class);
                        Model.beritaArrayList.add(berita);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public static ArrayList<Berita> getBeritaFromGenre(String genre){
        ArrayList<Berita> resultList = new ArrayList<>();
        userReference.orderByChild("genre").equalTo(genre).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot currentData : dataSnapshot.getChildren()){
                        Berita berita = currentData.getValue(Berita.class);
                        resultList.add(berita);
                    }
                }
            }
        });
        return resultList;
    }
    public static void getBeritaFromEmaiil(String email){
//        ArrayList<Berita> resultList = new ArrayList<>();
        beritaReference.orderByChild("email").equalTo(email).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                System.out.println("YEss");
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot currentData : dataSnapshot.getChildren()){
                        Berita berita = currentData.getValue(Berita.class);
                        System.out.println(berita.getJudul());
                        Model.beritaArrayList.add(berita);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });

    }
    public static String getCurrentUserEmail(){
        return FirebaseController.firebaseAuth.getCurrentUser().getEmail();
    }
    public static String getCurrentUserFullName(){
       return FirebaseController.firebaseAuth.getCurrentUser().getDisplayName();
    }
    public static void insertData(Berita berita){
        beritaReference.push().setValue(berita);
        TampilBerita.getRvAdaper().notifyDataSetChanged();
    }
}

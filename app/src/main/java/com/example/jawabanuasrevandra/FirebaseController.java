package com.example.jawabanuasrevandra;

import android.app.Application;

import androidx.annotation.NonNull;

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
//        userReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

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
    public static ArrayList<Berita> getBeritaFromEmaiil(String email){
        ArrayList<Berita> resultList = new ArrayList<>();
        userReference.orderByChild("email").equalTo(email).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
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

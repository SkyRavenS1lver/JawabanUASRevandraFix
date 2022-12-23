package com.example.jawabanuasrevandra;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.jawabanuasrevandra.Notifikasi.Notif;
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
    public static DatabaseReference notifReference;
    public static String currentUsername;
    public static int currentUmur;
    public static SharedPreferences sharedPreferences2;
    private final String sharedPrefFile = "com.example.jawabanuasrevandra";





    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences2 = getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("User");
        beritaReference = firebaseDatabase.getReference("Berita");
        notifReference = firebaseDatabase.getReference("Notification");
        beritaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String preferensi = sharedPreferences2.getString(CariBerita.GENRE_KEY,"");
                if (snapshot.hasChildren() && !preferensi.equals("Fav")){
                    Model.beritaArrayList.clear();
                    for (DataSnapshot currentData : snapshot.getChildren()){
                        Berita berita = currentData.getValue(Berita.class);
                        berita.setKey(currentData.getKey());
                        Model.beritaPublicArrayList.add(berita);
                        if(preferensi.equals("Edit") && firebaseAuth.getCurrentUser()!=null){
                            String email = getCurrentUserEmail();
                            if(currentData.child("email").getValue().equals(email)){
                            Model.beritaArrayList.add(berita);}}
                        else {
                            if(currentData.child("category").equals(preferensi) && berita.getUmur() <= FirebaseController.currentUmur){
                                Model.beritaArrayList.add(berita);
                            }
                        }
                    }if(TampilBerita.getRvAdaper()!=null){
                        TampilBerita.beritaAdapter.setList(new ArrayList<>(Model.beritaArrayList));
                        TampilBerita.rvBerita.setAdapter(TampilBerita.beritaAdapter);
                        TampilBerita.dataChange();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        notifReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    Model.allNotif.clear();
                    for (DataSnapshot currentData : snapshot.getChildren()){
                        if (firebaseAuth.getCurrentUser()!=null){
                        if (currentData.child("name").equals(FirebaseController.getCurrentUserEmail())) {
                            Notif notif = currentData.getValue(Notif.class);
                            notif.setKey(currentData.getKey());
                            Model.allNotif.add(notif);
                        }}}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public static void getBeritaFromGenre(String genre){
        Model.beritaArrayList.clear();
        beritaReference.orderByChild("category").equalTo(genre).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot currentData : dataSnapshot.getChildren()){
                        Berita berita = currentData.getValue(Berita.class);
                        berita.setKey(currentData.getKey());
                        Model.beritaArrayList.add(berita);
                    }
                    if(TampilBerita.getRvAdaper()!=null){
                        TampilBerita.refreshList();}
                }
            }
        });
    }
    public static void getBeritaFromEmaiil(String email){
            Model.beritaArrayList.clear();
            beritaReference.orderByChild("email").equalTo(email).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot currentData : dataSnapshot.getChildren()){
                        Berita berita = currentData.getValue(Berita.class);
                        berita.setKey(currentData.getKey());
                        Model.beritaArrayList.add(berita);
                    }
                    if(TampilBerita.getRvAdaper()!=null){
                        TampilBerita.refreshList();}
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });
    }

    public static void getBerita(String key){
        beritaReference.child(key).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    Berita berita = dataSnapshot.getValue(Berita.class);
                    berita.setKey(key);
                    Model.tempBerita = berita;
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
    public static void deleteData(Berita berita){
        beritaReference.child(berita.getKey()).removeValue();
        TampilBerita.getRvAdaper().notifyDataSetChanged();
    }
    public static void updatedata(Berita berita) {
        beritaReference.child(berita.getKey()).setValue(Berita.convertData(berita));
        TampilBerita.getRvAdaper().notifyDataSetChanged();
    }
    public static void getAllNotif(String email){
        notifReference.orderByChild("name").equalTo(email).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Model.allNotif.clear();
                CariBerita activity = new CariBerita();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot currentData:dataSnapshot.getChildren()){
                        Notif notif = currentData.getValue(Notif.class);
                        notif.setKey(currentData.getKey());
                        Model.allNotif.add(notif);
                    }
                    CariBerita.avail = true;
                }
                else {
                    CariBerita.gagal = true;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });
    }
    public static void insertData(Notif notif){
        notifReference.push().setValue(notif);
    }
    public static void deleteData(Notif notif){
        notifReference.child(notif.getKey()).removeValue();
    }
}

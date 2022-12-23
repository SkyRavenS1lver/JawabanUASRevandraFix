package com.example.jawabanuasrevandra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.jawabanuasrevandra.Favorite.Fav;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TampilBerita extends AppCompatActivity {
    public static ExecutorService executorService;
    public static RecyclerView rvBerita;
    FloatingActionButton addData;
    public static ArrayList<Berita> listBerita = new ArrayList<>();
    public static BeritaAdapter beritaAdapter;
    Drawable bgBaru;
    TextView judul;
    public static TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_berita);

        executorService = Executors.newSingleThreadExecutor();
        rvBerita = findViewById(R.id.rvBerita);
        judul = findViewById(R.id.JudulPage);
        addData = findViewById(R.id.addData);
        message = findViewById(R.id.Messages);
        ConstraintLayout bg = findViewById(R.id.background);
        String preferensi = CariBerita.sharedPreferences.getString(CariBerita.GENRE_KEY,"");
        if(preferensi.equals("Pariwisata")){
            bgBaru = ContextCompat.getDrawable(bg.getContext(), R.drawable.bg1);
            judul.setTextColor(getResources().getColor(R.color.text));
            message.setTextColor(getResources().getColor(R.color.text));
        }
        else if(preferensi.equals("Sport")){
            bgBaru = ContextCompat.getDrawable(bg.getContext(), R.drawable.bg2);
            judul.setTextColor(getResources().getColor(R.color.white));
            message.setTextColor(getResources().getColor(R.color.white));
        }
        else if(preferensi.equals("Politics")){
            bgBaru = ContextCompat.getDrawable(bg.getContext(), R.drawable.bg3);
            judul.setTextColor(getResources().getColor(R.color.text));
            message.setTextColor(getResources().getColor(R.color.white));
        }
        else if(preferensi.equals("Entertainment")){
            bgBaru = ContextCompat.getDrawable(bg.getContext(), R.drawable.bg6);
            judul.setTextColor(getResources().getColor(R.color.white));
            message.setTextColor(getResources().getColor(R.color.white));
        }
        else if(preferensi.equals("Crime")){
            bgBaru = ContextCompat.getDrawable(bg.getContext(), R.drawable.bg5);
            judul.setTextColor(getResources().getColor(R.color.white));
            message.setTextColor(getResources().getColor(R.color.white));
        }
        else if(preferensi.equals("Fav")){
            message.setText("Belum ada berita yang disukai");
            judul.setText("Berita Yang\nAnda Sukai");
            bgBaru = ContextCompat.getDrawable(bg.getContext(), R.drawable.bgphoto);
            judul.setTextColor(getResources().getColor(R.color.text));
            message.setTextColor(getResources().getColor(R.color.text));
            getFavId(FirebaseController.getCurrentUserEmail());
            getFavEmail();
        }
        else if(preferensi.equals("Edit")){
            judul.setText("List Semua\nArtikel Anda");
            bgBaru = ContextCompat.getDrawable(bg.getContext(), R.drawable.bgphoto);
            judul.setTextColor(getResources().getColor(R.color.text));
            message.setTextColor(getResources().getColor(R.color.text));
            addData.setVisibility(View.VISIBLE);

            addData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TampilBerita.this, ManageBerita.class);
                    intent.putExtra("mode", "add");
                    intent.putExtra("judul","Tambah Data Berita");
                    startActivity(intent);
                }
            });
        }
        bg.setBackground(bgBaru);
        if (!preferensi.equals("Edit") && !preferensi.equals("Fav")){
            getFavId(FirebaseController.getCurrentUserEmail());
            getFavEmail();
                FirebaseController.getBeritaFromGenre(preferensi);}
        else if (preferensi.equals("Fav")){}
        else{
                    FirebaseController.getBeritaFromEmaiil(FirebaseController.getCurrentUserEmail());
        }
        beritaAdapter = new BeritaAdapter(this, Model.beritaArrayList);
        rvBerita.setLayoutManager(new LinearLayoutManager(this));
        rvBerita.setAdapter(beritaAdapter);
        if(getRvAdaper()!=null){
            refreshList();}
    }

    public static void favUpdate() {
        if(TampilBerita.beritaAdapter!=null && TampilBerita.rvBerita!=null){
            Model.beritaArrayList.clear();
            ArrayList<String> temp = new ArrayList<>();
            for (Fav fav:new ArrayList<>(new HashSet<>(Model.allFav))){
                if (temp.contains(fav.toString())){continue;}
                if (fav.getEmail().equals(FirebaseController.getCurrentUserEmail())){
                    for (Berita berita: Model.beritaPublicArrayList){
                        if (fav.getKeyBerita().equals(berita.getKey())){
                            Model.beritaArrayList.add(berita);
                            temp.add(fav.toString());}}
                }
            }
            TampilBerita.beritaAdapter.setList(Model.beritaArrayList);
            TampilBerita.rvBerita.setAdapter(TampilBerita.beritaAdapter);
            TampilBerita.dataChange();}
    }

    public static void dataChange() {
        if (Model.beritaArrayList.size() > 0){
            message.setVisibility(View.INVISIBLE);
        }
        else {message.setVisibility(View.VISIBLE);}
    }
    public static BeritaAdapter getRvAdaper(){
        return beritaAdapter;
    }
    public static void refreshList(){
        if (beritaAdapter!=null){
        getRvAdaper().notifyDataSetChanged();
        dataChange();}
    }

    public void getFavId(String email){
        CariBerita.favDao.getFavEmail(email).observe(this, favs -> {
            Model.allFav.clear();
            Model.allFav = new ArrayList<>(favs);
        });

    }
    public static void getFavEmail(){
            Model.beritaArrayList.clear();
            ArrayList<String> temp = new ArrayList<>();
            if(Model.allFav!=null){
            for (Fav fav:Model.allFav){
                if (temp.contains(fav.toString())){continue;}
                for(Berita berita : Model.beritaPublicArrayList){
                    if(berita.getKey().equals(fav.getKeyBerita())){
                    Model.beritaArrayList.add(berita);
                    temp.add(fav.toString());}
            }}
            }
            if(beritaAdapter!=null){
                TampilBerita.beritaAdapter.setList(Model.beritaArrayList);
                TampilBerita.rvBerita.setAdapter(TampilBerita.beritaAdapter);
                TampilBerita.dataChange();}

    }
    public void isFav(String email, String keyBerita){
        CariBerita.favDao.isFav(email,keyBerita).observe(
                this, aBoolean -> {
                    Model.isFav = aBoolean;
                });
    }
    public void getId(String email, String keyBerita){
        CariBerita.favDao.getFavId(email,keyBerita).observe(
                this, integer -> {
                    Model.idBerita = integer;
                });
    }

    //function insert data ke room
    public void insertData(Fav fav){
        executorService.execute(()->CariBerita.favDao.insert(fav));
        CariBerita.favDao.getLast().observe(this,favs -> {
            Model.idBerita = favs.get(0).getId();
        });
    }


    //function delete
    public void deleteData(Fav fav){
        executorService.execute(()->CariBerita.favDao.delete(fav));
    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(TampilBerita.this, CariBerita.class));
    }
}

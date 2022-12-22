package com.example.jawabanuasrevandra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TampilBerita extends AppCompatActivity {
    private RecyclerView rvBerita;
    FloatingActionButton addData;
    public static ArrayList<Berita> listBerita = new ArrayList<>();
    private static BeritaAdapter beritaAdapter;
    Drawable bgBaru;
    TextView judul;
    public static TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_berita);
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
        else if(preferensi.equals("Edit")){
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
//        listBerita =
        if (!preferensi.equals("Edit")){
        Model.beritaArrayList = FirebaseController.getBeritaFromGenre(preferensi);}
        else{
            FirebaseController.getBeritaFromEmaiil(FirebaseController.getCurrentUserEmail());}
        dataChange();
        beritaAdapter = new BeritaAdapter(this, Model.beritaArrayList);
        System.out.println(Model.beritaArrayList.size()+"--------");
        rvBerita.setLayoutManager(new LinearLayoutManager(this));
        rvBerita.setAdapter(beritaAdapter);
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
        getRvAdaper().notifyDataSetChanged();
        dataChange();
    }
}

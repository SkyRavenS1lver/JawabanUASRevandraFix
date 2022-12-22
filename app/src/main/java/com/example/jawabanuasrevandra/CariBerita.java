package com.example.jawabanuasrevandra;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CariBerita extends AppCompatActivity {
    Spinner spinnerText;
    public static ArrayAdapter<String> arrayAdapter;
    public static SharedPreferences sharedPreferences;
    public static final String GENRE_KEY = "genre";
    private final String sharedPrefFile = "com.example.jawabanuasrevandra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari_berita);
        spinnerText = findViewById(R.id.Status);
        sharedPreferences = getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
        ((TextView)findViewById(R.id.namaUser)).setText(FirebaseController.getCurrentUserFullName());
        findViewById(R.id.Logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CariBerita.this);
                alert.setTitle("Logout");
                alert.setMessage("Apakah anda ingin logout dari aplikasi?");
                alert.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(CariBerita.this, "Berhasil Logout", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.show();
            }
        });
        findViewById(R.id.cari).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cari_berita();
            }
        });
        Model.genres.clear();
        Model.genres.add("Pariwisata");
        Model.genres.add("Crime");
        Model.genres.add("Sport");
        Model.genres.add("Politics");
        Model.genres.add("Entertainment");
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.item, Model.genres);
        arrayAdapter.setDropDownViewResource(R.layout.dropitem);
        spinnerText.setAdapter(arrayAdapter);
        if (spinnerText != null){
//            spinnerText.setOnItemSelectedListener(this);
        }
        findViewById(R.id.manageData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(GENRE_KEY, "Edit");
                editor.apply();
                startActivity(new Intent(CariBerita.this, TampilBerita.class));
            }
        });
    }
    public void cari_berita(){
//        String preferensi = spinnerText.getSelectedItem().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GENRE_KEY, spinnerText.getSelectedItem().toString());
        editor.apply();
        Intent tampilkan = new Intent(CariBerita.this, TampilBerita.class);
        startActivity(tampilkan);

    }
}
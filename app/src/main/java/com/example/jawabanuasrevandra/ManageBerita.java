package com.example.jawabanuasrevandra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ManageBerita extends AppCompatActivity {
    TextView judul;
    EditText judulBerita, contentBerita, umur;
    TextView penulisBerita,rilisBerita;
    LinearLayout layoutUmur, kumpul;
    FloatingActionButton edit,back,confirm,cancel;
    ArrayAdapter<String> arrayAdapter;
    Spinner spinnerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_berita);
        judul = findViewById(R.id.JudulPage);
        umur = findViewById(R.id.umurMin);
        Intent getter = getIntent();
        String mode = getter.getStringExtra("mode");
        String judulPage = getter.getStringExtra("judul");
        Intent intent = new Intent(ManageBerita.this,TampilBerita.class);
        judul.setText(judulPage);
        kumpul = findViewById(R.id.Penulis);
        back = findViewById(R.id.backButton);
        edit = findViewById(R.id.editData);
        confirm = findViewById(R.id.confirmButton);
        cancel = findViewById(R.id.cancelButton);
        judulBerita = findViewById(R.id.judulBerita);
        contentBerita = findViewById(R.id.contentBerita);
        penulisBerita = findViewById(R.id.writerBerita);
        rilisBerita = findViewById(R.id.rilisBerita);
        spinnerText = findViewById(R.id.Status);
        layoutUmur = findViewById(R.id.LayoutUmur);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.item2, Model.genres);
        spinnerText.setAdapter(arrayAdapter);



        if (mode.equals("add")){
            changeState(true);
            kumpul.setVisibility(View.GONE);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean error = false;
                    if (TextUtils.isEmpty(judulBerita.getText())){
                        judulBerita.setError("Judul Tidak Boleh Kosong!");
                        error = true;
                    }
                    if (TextUtils.isEmpty(contentBerita.getText())){
                        contentBerita.setError("Konten Harus Diisi!");
                        error = true;
                    }
                    if (TextUtils.isEmpty(umur.getText())){
                        umur.setText("0");
                    }
                    if (!error){
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy HH.mm", Locale.getDefault());
                    String formattedDate = df.format(c);
                    Berita berita = new Berita(
                            judulBerita.getText().toString(),
                            formattedDate,
                            spinnerText.getSelectedItem().toString(),
                            contentBerita.getText().toString(),
                            FirebaseController.getCurrentUserEmail(),
                            FirebaseController.getCurrentUserFullName(),
                            Integer.parseInt(umur.getText().toString())
                            );
                    FirebaseController.insertData(berita);
                    startActivity(intent);
                }}
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                }
            });
        }
        if (mode.equals("edit")){
            layoutUmur.setVisibility(View.GONE);
            changeState(false);
            penulisBerita.setTextSize(18);
            rilisBerita.setTextSize(18);
            Berita berita = Model.currentBerita;
            findViewById(R.id.TVJudul).setVisibility(View.GONE);
            findViewById(R.id.TVGenre).setVisibility(View.GONE);
            judulBerita.setText(berita.getJudul());
            judulBerita.setGravity(Gravity.CENTER);
            judulBerita.setTextSize(40);
            judulBerita.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            int urutan = Model.genres.indexOf(berita.getCategory());
            spinnerText.setSelection(urutan);
            umur.setText(String.valueOf(berita.getUmur()));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(35,0,0,10);
            layoutUmur.setLayoutParams(params);
            penulisBerita.setText(berita.getPenulis());
            rilisBerita.setText("("+berita.getRilis()+")");
            contentBerita.setText(berita.getContent());
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ManageBerita.this, TampilBerita.class));
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    judul.setText("Edit Data");
                    changeState(true);
                    Berita currentBerita = Model.currentBerita;
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean error = false;
                            if (TextUtils.isEmpty(judulBerita.getText())){
                                judulBerita.setError("Judul Tidak Boleh Kosong!");
                                error = true;
                            }
                            if (TextUtils.isEmpty(contentBerita.getText())){
                                contentBerita.setError("Konten Harus Diisi!");
                                error = true;
                            }
                            if (TextUtils.isEmpty(umur.getText())){
                                umur.setText("0");
                            }
                            if (!error){
                            Berita berita = new Berita(
                                    judulBerita.getText().toString(),
                                    currentBerita.getRilis(),
                                    spinnerText.getSelectedItem().toString(),
                                    contentBerita.getText().toString(),
                                    currentBerita.getEmail(),
                                    currentBerita.getPenulis(),
                                    Integer.parseInt(umur.getText().toString())
                            );
                            berita.setKey(currentBerita.getKey());
                            FirebaseController.updatedata(berita);
                            judul.setText(judulPage);
                            changeState(false);
                        }}
                    });
                    int urutan = Model.genres.indexOf(currentBerita.getCategory());
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            judulBerita.setText(currentBerita.getJudul());
                            spinnerText.setSelection(urutan);
                            contentBerita.setText(currentBerita.getContent());
                            umur.setText(String.valueOf(currentBerita.getUmur()));
                            changeState(false);
                            judul.setText(judulPage);
                        }
                    });
                }
            });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy HH.mm", Locale.getDefault());
                    String formattedDate = df.format(c);
                    boolean error = false;
                    if (TextUtils.isEmpty(judulBerita.getText())){
                        judulBerita.setError("Judul Tidak Boleh Kosong!");
                        error = true;
                    }
                    if (TextUtils.isEmpty(contentBerita.getText())){
                        contentBerita.setError("Konten Harus Diisi!");
                        error = true;
                    }
                    if (TextUtils.isEmpty(umur.getText())){
                        umur.setText("0");
                    }
                    if (!error){
                    Berita berita = new Berita(
                            judulBerita.getText().toString(),
                            formattedDate,
                            spinnerText.getSelectedItem().toString(),
                            contentBerita.getText().toString(),
                            FirebaseController.getCurrentUserEmail(),
                            FirebaseController.getCurrentUserFullName(),
                            Integer.parseInt(umur.getText().toString())
                    );
                    FirebaseController.insertData(berita);
                    startActivity(intent);
                }}
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                }
            });
        }
    }
    public void changeState(Boolean bool){
        judulBerita.setEnabled(bool);
        umur.setEnabled(bool);
        spinnerText.setEnabled(bool);
        contentBerita.setEnabled(bool);
        String preferensi = CariBerita.sharedPreferences.getString(CariBerita.GENRE_KEY,"");
        if(!preferensi.equals("Edit")){
            edit.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        return;}

        if (bool){
            edit.setVisibility(View.INVISIBLE);
            back.setVisibility(View.INVISIBLE);
            confirm.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            layoutUmur.setVisibility(View.VISIBLE);
            spinnerText.setVisibility(View.VISIBLE);
            kumpul.setVisibility(View.GONE);
        }
        else{
            kumpul.setVisibility(View.VISIBLE);
            layoutUmur.setVisibility(View.GONE);
            spinnerText.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.INVISIBLE);
        }

    }
}

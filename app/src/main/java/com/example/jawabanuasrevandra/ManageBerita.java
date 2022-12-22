package com.example.jawabanuasrevandra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ManageBerita extends AppCompatActivity {
    TextView judul;
    EditText judulBerita,penulisBerita,rilisBerita, contentBerita, umur;
    FloatingActionButton edit,back,confirm,cancel;
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
        back = findViewById(R.id.backButton);
        edit = findViewById(R.id.editData);
        confirm = findViewById(R.id.confirmButton);
        cancel = findViewById(R.id.cancelButton);
        judulBerita = findViewById(R.id.judulBerita);
        contentBerita = findViewById(R.id.contentBerita);
        penulisBerita = findViewById(R.id.writerBerita);
        rilisBerita = findViewById(R.id.rilisBerita);
        spinnerText = findViewById(R.id.Status);
        spinnerText.setAdapter(CariBerita.arrayAdapter);

        if (mode.equals("add")){
            changeState(true);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
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
                }
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
        if (bool){
            edit.setVisibility(View.INVISIBLE);
            back.setVisibility(View.INVISIBLE);
            confirm.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
        }
        else{
            edit.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.INVISIBLE);
        }
        judulBerita.setFocusable(bool);
        judulBerita.setFocusableInTouchMode(bool);
        umur.setFocusable(bool);
        umur.setFocusableInTouchMode(bool);
        spinnerText.setClickable(bool);
        contentBerita.setFocusable(bool);
        contentBerita.setFocusableInTouchMode(bool);
        penulisBerita.setFocusable(bool);
        penulisBerita.setFocusableInTouchMode(bool);
    }
}

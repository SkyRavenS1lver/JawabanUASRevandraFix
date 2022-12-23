package com.example.jawabanuasrevandra;

import static com.example.jawabanuasrevandra.TampilBerita.executorService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jawabanuasrevandra.Favorite.FavDao;
import com.example.jawabanuasrevandra.Favorite.FavRoomDatabase;
import com.example.jawabanuasrevandra.Notifikasi.Notif;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CariBerita extends AppCompatActivity {
    public static NotificationManager notificationManager;
    public static FavDao favDao;
    private int penanda;
    private final static String CHANNEL_ID = "primary channel";
    public static boolean gagal = false;
    private static int NOTIFICATION_ID = 0;
    private final static String ACTION_UPDATE_NOTIF = "ACTION_UPDATE_NOTIF";
    private final static String ACTION_CANCEL_NOTIF = "ACTION_CANCEL_NOTIF";
    Spinner spinnerText;
    private ArrayAdapter<String> arrayAdapter;
    public static SharedPreferences sharedPreferences;
    public static final String GENRE_KEY = "genre";
    private final String sharedPrefFile = "com.example.jawabanuasrevandra";
    public static boolean avail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari_berita);
        FavRoomDatabase db = FavRoomDatabase.getDatabase(this);
        favDao = db.favDao();
        FirebaseController.getAllNotif(FirebaseController.getCurrentUserEmail());
        ExecutorService executorServices = Executors.newSingleThreadExecutor();
        TampilBerita baru = new TampilBerita();
        baru.getFavId(FirebaseController.getCurrentUserEmail());
        CariBerita.favDao.getFavEmail(FirebaseController.getCurrentUserEmail()).observe(this, favs -> {
            Model.allFav.clear();
            Model.allFav = new ArrayList<>(favs);
        });

        executorServices.execute(()-> {

                while (true) {
                    if (gagal){gagal = false;break;}
                    if (Model.allNotif.size() > 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getAllNotification();
                            }
                        });
                        break;
                    }
                }

        });

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "ap notif",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

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
                        Model.beritaArrayList.clear();
                        Model.allFav.clear();
                        startActivity(new Intent(CariBerita.this, LoginActivity.class));
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
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.item2, Model.genres);
        arrayAdapter.setDropDownViewResource(R.layout.dropitem);
        spinnerText.setAdapter(arrayAdapter);
        if (spinnerText != null){
        }
        findViewById(R.id.manageData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                SharedPreferences.Editor editors = FirebaseController.sharedPreferences2.edit();
                editor.putString(GENRE_KEY, "Edit");
                editors.putString(GENRE_KEY, "Edit");
                editor.apply();
                editors.apply();
                startActivity(new Intent(CariBerita.this, TampilBerita.class));
            }
        });
        findViewById(R.id.Favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                SharedPreferences.Editor editors = FirebaseController.sharedPreferences2.edit();
                editor.putString(GENRE_KEY, "Fav");
                editors.putString(GENRE_KEY, "Fav");
                editor.apply();
                editors.apply();
                startActivity(new Intent(CariBerita.this, TampilBerita.class));
            }
        });
    }
    public void cari_berita(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences.Editor editors = FirebaseController.sharedPreferences2.edit();
        editor.putString(GENRE_KEY, spinnerText.getSelectedItem().toString());
        editors.putString(GENRE_KEY, spinnerText.getSelectedItem().toString());
        editor.apply();
        editors.apply();
        Intent tampilkan = new Intent(CariBerita.this, TampilBerita.class);
        startActivity(tampilkan);
    }

    public void getAllNotification(){
        avail = true;
        penanda = Model.allNotif.size();
        for (Notif notif:Model.allNotif){
            sendNotification(notif);
        }
    }
    private void sendNotification(Notif notifApk){
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(notifApk);
        notificationManager.notify(penanda, notifyBuilder.build());
        FirebaseController.deleteData(notifApk);
        penanda--;
    }
    private NotificationCompat.Builder getNotificationBuilder(Notif notifApk){

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Berita APP")
                .setContentText(notifApk.getMessage())
                .setSmallIcon(R.drawable.ic_baseline_favorite_24);
        return notifyBuilder;
    }

    @Override
    public void onBackPressed() {
            AlertDialog.Builder alert = new AlertDialog.Builder(CariBerita.this);
            alert.setTitle("Perhatian!");
            alert.setMessage("Apakah anda ingin keluar dari aplikasi?");
            alert.setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finishAffinity();
                    finish();
                }
            });
            alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            alert.show();
    }
}

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
    private static NotificationReceiver receiverRegister;
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
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    if (gagal){gagal = false;break;}
//                    if (Model.allNotif.size() > 0) {
//                        System.out.println("Jalan====");
//                        getAllNotification();
//                        break;
//                    }
//                }
//            }
//        });
        executorServices.execute(()-> {
//            while (true){
                while (true) {
                    if (gagal){gagal = false;break;}
                    if (Model.allNotif.size() > 0) {
                        System.out.println("Jalan====");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getAllNotification();
                            }
                        });
                        break;
                    }
                }
//    }
        });

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Importance kepentingan notif, HIGH (POPUP dan muncul di manager)
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "ap notif",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

//        Model.allNotif.add(new Notif("Halo",FirebaseController.getCurrentUserEmail()));
//        sendNotification(Model.allNotif.get(0));
        spinnerText = findViewById(R.id.Status);
        receiverRegister= new NotificationReceiver();
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
//            spinnerText.setOnItemSelectedListener(this);
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
//                TampilBerita activity = new TampilBerita();
//                activity.getFavId(FirebaseController.getCurrentUserEmail());
//                TampilBerita.getFavEmail();
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
//        String preferensi = spinnerText.getSelectedItem().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences.Editor editors = FirebaseController.sharedPreferences2.edit();
        editor.putString(GENRE_KEY, spinnerText.getSelectedItem().toString());
        editors.putString(GENRE_KEY, spinnerText.getSelectedItem().toString());
        editor.apply();
        editors.apply();
        Intent tampilkan = new Intent(CariBerita.this, TampilBerita.class);
        startActivity(tampilkan);
    }

//    public void getAllNotification(){
//        notifDao.getAllNotif(FirebaseController.getCurrentUserFullName()).observe(this, notifApks -> {
//            for (NotifApk notifApk:notifApks){
//                sendNotification(notifApk);
//            }
//        });
//    }
    public void getAllNotification(){
        avail = true;
        penanda = Model.allNotif.size();
        for (Notif notif:Model.allNotif){
            sendNotification(notif);
        }
    }
//    public void getAllNotification(){
//        avail = true;
//        System.out.println(Model.allNotif.size());
//        System.out.println("----");
//
//        for (Iterator<Notif> iterator = Model.allNotif.iterator(); iterator.hasNext();){
//            sendNotification();
//        }
//    }
    private void sendNotification(Notif notifApk){
//        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIF);
//        Intent cancerIntent = new Intent(ACTION_CANCEL_NOTIF);
//        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this,
//                NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_IMMUTABLE);
//        PendingIntent cancerPendingIntent = PendingIntent.getBroadcast(this,
//                NOTIFICATION_ID, cancerIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(notifApk);
//        //Penambahan Teks Untuk Notif
//        notifyBuilder.addAction(R.drawable.ic_copyright_black_24dp, "Update Notification", updatePendingIntent);
//        notifyBuilder.addAction(R.drawable.ic_copyright_black_24dp, "Delete", cancerPendingIntent);

        //Supaya bisa mengirim, kita butuh yang namanya notification id
        notificationManager.notify(Model.allNotif.size(), notifyBuilder.build());
        FirebaseController.deleteData(notifApk);
        penanda--;
    }
    private NotificationCompat.Builder getNotificationBuilder(Notif notifApk){

//        Intent noificationIntent =  new Intent(this, Main2Activity.class);
//        PendingIntent notificationpendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID,
//                noificationIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Berita APP")
                .setContentText(notifApk.getMessage())
                .setSmallIcon(R.drawable.ic_baseline_favorite_24);
        return notifyBuilder;
    }
    public class NotificationReceiver extends BroadcastReceiver {

        public NotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_UPDATE_NOTIF)){
//                updateNotification();
            }
            else if (action.equals(ACTION_CANCEL_NOTIF)){
                notificationManager.cancel(NOTIFICATION_ID);
            }
        }
    }

    //Jangan lupa unregister receiver onDestroy, untuk formalitas
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(receiverRegister);
//    }
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

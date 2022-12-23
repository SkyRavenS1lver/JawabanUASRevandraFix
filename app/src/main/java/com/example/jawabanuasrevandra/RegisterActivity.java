package com.example.jawabanuasrevandra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class RegisterActivity extends AppCompatActivity {
    EditText username,password,email,fullName, dateOfBirth;
    private Boolean available = true;
    private long mLastClickTime = 0;
    private Boolean valid = true;
    public static String dateMessage;
    public static int umur;
    private boolean response = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        fullName = findViewById(R.id.fullName);
        dateOfBirth = findViewById(R.id.DateOfBirth);
        dateOfBirth.setKeyListener(null);
        //Making the popup
        dateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){showDataPicker();}
            }
        });
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDataPicker();
            }
        });
        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean error = false;
                available = false;
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (TextUtils.isEmpty(username.getText())){
                    username.setError("Username Tidak Boleh Kosong!");
                    error = true;
                }
                if (TextUtils.isEmpty(password.getText())){
                    password.setError("Password Tidak Boleh Kosong!");
                    error = true;
                }
                if (TextUtils.isEmpty(email.getText())){
                    email.setError("Email Tidak Boleh Kosong!");
                    error = true;
                }
                else if (!email.getText().toString().matches(emailPattern)){
                    email.setError("Isi Email dengan benar!");
                    error = true;
                }
                if (TextUtils.isEmpty(fullName.getText())){
                    fullName.setError("Tolong Isi nama lengkap anda!");
                    error = true;
                }
                if (TextUtils.isEmpty(dateOfBirth.getText())){
                    dateOfBirth.setError("Silahkan Isi Tanggal Lahir Anda!");
                    error = true;
                }
                if (!error){
                    String usernameUser = username.getText().toString();
                    String passwordUser = password.getText().toString();
                    String emailUser = email.getText().toString();
                    String fullnameUser = fullName.getText().toString();
                    available = false;
                    valid = true;
                    response = true;
                    FirebaseController.userReference.child(usernameUser).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChildren()){
                                username.setError("Username Sudah Terdaftar!");
                                valid = false;
                            }
                            response = true;

                        }})
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this,"Tidak dapat terhubung dengan internet", Toast.LENGTH_LONG).show();
                                    valid = false;
                                    response = true;
                                }
                            });
                    FirebaseController.userReference.orderByChild("email").equalTo(emailUser).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChildren()){
                                email.setError("Email sudah terdaftar!");
                                valid = false;
                            }response = true;}
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println(e.toString());
                                    valid = false;
                                    response = true;
                                }
                            });
                    if (valid && response){
                        FirebaseController.firebaseAuth.createUserWithEmailAndPassword(emailUser,passwordUser).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                if (!available && response && valid){
                                    FirebaseController.userReference.child(usernameUser).child("email").setValue(emailUser);
                                    FirebaseController.userReference.child(usernameUser).child("umur").setValue(String.valueOf(umur));
                                    FirebaseController.firebaseAuth.signInWithEmailAndPassword(emailUser,passwordUser);
                                    FirebaseUser user = FirebaseController.firebaseAuth.getCurrentUser();
                                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(fullnameUser).build();
                                    user.updateProfile(profileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(RegisterActivity.this, "Selamat Datang Pengguna Baru!",Toast.LENGTH_LONG).show();
                                            FirebaseController.currentUsername = usernameUser;
                                            FirebaseController.currentUmur = umur;
                                            startActivity(new Intent(RegisterActivity.this, CariBerita.class));
                                            available = true;
                                            response = false;
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            available = true;
                                            response = false;
                                            Toast.makeText(RegisterActivity.this, "Mohon Maaf Terjadi Kesalahan!",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });


    }
    public void showDataPicker(){
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(), "date-picker");}
    public void processDatePicker(int day, int month, int year){
        String day_string = Integer.toString(day);
        String month_string = Integer.toString(month+1);
        String year_string = Integer.toString(year);
        dateMessage = day_string + "-" + month_string + "-" + year_string;
        Calendar currentDate = new GregorianCalendar();
        umur = currentDate.get(Calendar.YEAR) - year;
        if (month > currentDate.get(Calendar.MONTH) || (month == currentDate.get(Calendar.MONTH) &&
                day > currentDate.get(Calendar.DAY_OF_MONTH)))
        {umur--;}
        dateOfBirth.setText(dateMessage);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
        alert.setTitle("Perhatian!");
        alert.setMessage("Apakah anda ingin kembali ke halaman login?");
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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
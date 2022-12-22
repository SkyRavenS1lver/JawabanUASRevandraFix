package com.example.jawabanuasrevandra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    EditText username,password;
    private Boolean available = true;
    private long mLastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean error = false;
                available = false;
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
                if(!error){
                    String usernameUser = username.getText().toString();
                    String passwordUser = password.getText().toString();
                    available = false;
                    FirebaseController.userReference.child(usernameUser).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChildren()){
                                    for (DataSnapshot currentData : dataSnapshot.getChildren()){
                                        String email = currentData.getValue().toString();
                                        FirebaseController.firebaseAuth.signInWithEmailAndPassword(email,passwordUser).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                if (!available){
                                                Toast.makeText(LoginActivity.this,"Login Success!", Toast.LENGTH_LONG).show();
                                                FirebaseController.currentUsername = usernameUser;
                                                startActivity(new Intent(LoginActivity.this, CariBerita.class));
                                                available = true;
                                            }
                                        }}).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                password.setError("Password Salah!");
                                                available = true;
                                            }
                                        });
                                        break;
                                    }
                                }
                                else {username.setError("Username Tidak Terdaftar!");}
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this,"Tidak dapat terhubung dengan internet", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
//        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                firebaseAuth.createUserWithEmailAndPassword("Revandra@gmail.com", "denpasar").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult authResult) {
//                        Toast.makeText(LoginActivity.this,"Sukses", Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        });
//        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                userReference.child("radk111002").setValue(new User("revandra@gmail.com", "denpasar"));
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseController.firebaseAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(this, "Sudah Login.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, CariBerita.class);
            startActivity(intent);
        }
    }
}
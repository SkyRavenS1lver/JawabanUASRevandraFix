package com.example.jawabanuasrevandra.Favorite;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Fav {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    private int id;
    @ColumnInfo(name="keyBerita")
    private String keyBerita;

    @Ignore
    public Fav() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyBerita() {
        return keyBerita;
    }

    public void setKeyBerita(String keyBerita) {
        this.keyBerita = keyBerita;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Fav(int id, String keyBerita, String email) {
        this.id = id;
        this.keyBerita = keyBerita;
        this.email = email;
    }
    @Ignore
    public Fav(String keyBerita, String email) {
        this.keyBerita = keyBerita;
        this.email = email;
    }

    @ColumnInfo(name="email")
    private String email;

    @Override
    public String toString(){
        return this.keyBerita+this.email;
    }

}

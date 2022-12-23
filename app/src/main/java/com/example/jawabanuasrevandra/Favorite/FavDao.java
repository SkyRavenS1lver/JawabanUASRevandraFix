package com.example.jawabanuasrevandra.Favorite;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Fav fav);

    @Update
    void update(Fav fav);
    @Delete
    void delete(Fav fav);


    //Query ber parameter
    @Query("SELECT EXISTS(SELECT * FROM fav where email = :email and keyBerita = :key LIMIT 1)")
    LiveData<Boolean> isFav(String email, String key);
    @Query("SELECT id FROM fav where email = :email and keyBerita = :key LIMIT 1")
    LiveData<Integer> getFavId(String email, String key);
    //Query ber parameter
    @Query("SELECT * FROM fav where email = :email")
    LiveData<List<Fav>> getFavEmail(String email);

    @Query("SELECT * FROM fav order by id desc limit 1")
    LiveData<List<Fav>> getLast();


}

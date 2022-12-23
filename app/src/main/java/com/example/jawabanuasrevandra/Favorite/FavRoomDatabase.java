package com.example.jawabanuasrevandra.Favorite;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Fav.class}, version = 1, exportSchema = false)
public abstract class FavRoomDatabase extends RoomDatabase {
    public abstract FavDao favDao();
    public static volatile FavRoomDatabase INSTANCE;
    public static FavRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (FavRoomDatabase.class){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        FavRoomDatabase.class, "db").build();
            }
        }
        return INSTANCE;
    }
}

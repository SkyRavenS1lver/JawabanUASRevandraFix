package com.example.jawabanuasrevandra;

import com.example.jawabanuasrevandra.Favorite.Fav;
import com.example.jawabanuasrevandra.Notifikasi.Notif;

import java.util.ArrayList;

public class Model{
    public static ArrayList<String> genres = new ArrayList<>();
    public static ArrayList<Berita> beritaArrayList = new ArrayList<>();
    public static ArrayList<Berita> beritaPublicArrayList = new ArrayList<>();
    public static Berita currentBerita;
    public static Berita tempBerita;
    public static boolean isFav;
    public static int idBerita;
    public static ArrayList<Fav> allFav = new ArrayList<>();
    public static ArrayList<Notif> allNotif = new ArrayList<>();
//    public static String preferensi;
}

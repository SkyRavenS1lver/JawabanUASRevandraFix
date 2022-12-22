package com.example.jawabanuasrevandra;

public class Berita {
    private String key;
    private String judul;
    private String content;
    private String rilis;
    private String penulis;

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;
    private String category;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getRilis() {
        return rilis;
    }

    public void setRilis(String rilis) {
        this.rilis = rilis;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Berita(String judul, String rilis, String category, String content, String email, String penulis)
    {
        this.judul = judul;
        this.content = content;
        this.rilis = rilis;
        this.category = category;
        this.email = email;
        this.penulis = penulis;
    }
    public static Berita convertData(Berita berita){
        return new Berita(berita.getJudul(),berita.getRilis(),berita.getCategory(),berita.getContent(), berita.getEmail(), berita.getPenulis());
    }

}

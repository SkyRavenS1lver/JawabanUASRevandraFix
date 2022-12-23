package com.example.jawabanuasrevandra.Notifikasi;

public class Notif {
    private String key;
    private String message;
    private String name;

    public Notif() {
    }

    public Notif(String message, String name) {
        this.message = message;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

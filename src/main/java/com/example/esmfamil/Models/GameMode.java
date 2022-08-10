package com.example.esmfamil.Models;

import com.google.gson.annotations.SerializedName;

public enum GameMode {
    @SerializedName("1")
    STOP_GAMEMODE("پایان با گفتن استُپ",1),
    @SerializedName("2")
    TIME_GAMEMODE("پایان با تایمر",2);

    private final String name;
    private final int id;

    GameMode(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

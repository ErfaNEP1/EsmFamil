package com.example.esmfamil.Models;

import com.google.gson.annotations.SerializedName;

public enum GameFields {
    @SerializedName("1")
    NAME("نام",1),
    @SerializedName("2")
    FAMILY_NAME("فامیل",2),
    @SerializedName("3")
    CITY("شهر",3),
    @SerializedName("4")
    COUNTRY("کشور",4),
    @SerializedName("5")
    FOOD("غذا",5),
    @SerializedName("6")
    CLOTH("پوشاک",6),
    @SerializedName("7")
    FRUIT("میوه",7),
    @SerializedName("8")
    CAR("ماشین",8),
    @SerializedName("9")
    FLOWER("گل",9),
    @SerializedName("10")
    ANIMAL("حیوان",10),
    @SerializedName("11")
    THING("اشیا",11);

    private final String name;
    private final int id;

    GameFields(String name, int id) {
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

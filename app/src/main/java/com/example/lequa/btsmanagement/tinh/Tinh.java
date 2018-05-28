package com.example.lequa.btsmanagement.tinh;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Tinh implements Comparable<Tinh>{
    @SerializedName("name")
    private String name;
    @SerializedName("slug")
    private String slug;
    @SerializedName("type")
    private String type;
    @SerializedName("name_with_type")
    private String nameWithType;
    @SerializedName("code")
    private String code;

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getType() {
        return type;
    }

    public String getNameWithType() {
        return nameWithType;
    }

    public String getCode() {
        return code;
    }

    @Override
    public int compareTo(@NonNull Tinh tinh) {
        return name.compareTo(tinh.name);
    }
}

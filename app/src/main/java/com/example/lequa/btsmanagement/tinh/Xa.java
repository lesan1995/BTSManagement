package com.example.lequa.btsmanagement.tinh;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Xa implements Comparable<Xa>{
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("slug")
    private String slug;
    @SerializedName("name_with_type")
    private String nameWithType;
    @SerializedName("path")
    private String path;
    @SerializedName("path_with_type")
    private String pathWithType;
    @SerializedName("code")
    private String code;
    @SerializedName("parent_code")
    private String parentCode;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSlug() {
        return slug;
    }

    public String getNameWithType() {
        return nameWithType;
    }

    public String getPath() {
        return path;
    }

    public String getPathWithType() {
        return pathWithType;
    }

    public String getCode() {
        return code;
    }

    public String getParentCode() {
        return parentCode;
    }

    @Override
    public int compareTo(@NonNull Xa xa) {
        return name.compareTo(xa.name);
    }
}

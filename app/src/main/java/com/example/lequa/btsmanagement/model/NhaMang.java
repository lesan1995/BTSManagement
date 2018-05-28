package com.example.lequa.btsmanagement.model;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "iDNhaMang")
public class NhaMang {
    @NonNull
    @SerializedName("IDNhaMang")
    private Integer iDNhaMang;
    @SerializedName("TenNhaMang")
    private String tenNhaMang;
    @SerializedName("Image")
    private String image;

    /**
     * No args constructor for use in serialization
     *
     */
    public NhaMang() {
    }

    /**
     *
     * @param tenNhaMang
     * @param iDNhaMang
     */
    public NhaMang(Integer iDNhaMang, String tenNhaMang,String image) {
        this.iDNhaMang = iDNhaMang;
        this.tenNhaMang = tenNhaMang;
        this.image=image;
    }

    public Integer getIDNhaMang() {
        return iDNhaMang;
    }

    public void setIDNhaMang(Integer iDNhaMang) {
        this.iDNhaMang = iDNhaMang;
    }

    public String getTenNhaMang() {
        return tenNhaMang;
    }

    public void setTenNhaMang(String tenNhaMang) {
        this.tenNhaMang = tenNhaMang;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}

package com.example.lequa.btsmanagement.model;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "iDHinhAnh")
public class HinhAnhTram {
    @NonNull
    @SerializedName("IDHinhAnh")
    private Integer iDHinhAnh;
    @SerializedName("IDTram")
    private Integer iDTram;
    @SerializedName("Ten")
    private String ten;
    /**
     * No args constructor for use in serialization
     *
     */
    public HinhAnhTram() {
    }

    /**
     *
     * @param ten
     * @param iDTram
     * @param iDHinhAnh
     */
    public HinhAnhTram(Integer iDHinhAnh, Integer iDTram, String ten) {
        this.iDHinhAnh = iDHinhAnh;
        this.iDTram = iDTram;
        this.ten = ten;
    }

    public Integer getIDHinhAnh() {
        return iDHinhAnh;
    }

    public void setIDHinhAnh(Integer iDHinhAnh) {
        this.iDHinhAnh = iDHinhAnh;
    }

    public Integer getIDTram() {
        return iDTram;
    }

    public void setIDTram(Integer iDTram) {
        this.iDTram = iDTram;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

}

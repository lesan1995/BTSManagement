package com.example.lequa.btsmanagement.model;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "iDTram")
public class Tram {
    @NonNull
    @SerializedName("IDTram")
    private Integer iDTram;
    @SerializedName("TenTram")
    private String tenTram;
    @SerializedName("CotAnten")
    private Integer cotAnten;
    @SerializedName("CotTiepDat")
    private Integer cotTiepDat;
    @SerializedName("SanTram")
    private Integer sanTram;
    @SerializedName("Tinh")
    private String tinh;
    @SerializedName("ViDo")
    private Double viDo;
    @SerializedName("KinhDo")
    private Double kinhDo;
    @SerializedName("IDQuanLy")
    private String iDQuanLy;
    @SerializedName("BanKinhPhuSong")
    private Double banKinhPhuSong;

    /**
     * No args constructor for use in serialization
     *
     */
    public Tram() {
    }

    /**
     *
     * @param kinhDo
     * @param tinh
     * @param tenTram
     * @param cotTiepDat
     * @param cotAnten
     * @param sanTram
     * @param viDo
     * @param iDTram
     * @param banKinhPhuSong
     * @param iDQuanLy
     */
    public Tram(Integer iDTram, String tenTram, Integer cotAnten, Integer cotTiepDat,Integer sanTram, String tinh, Double viDo, Double kinhDo, String iDQuanLy, Double banKinhPhuSong) {
        this.iDTram = iDTram;
        this.tenTram = tenTram;
        this.cotAnten = cotAnten;
        this.cotTiepDat = cotTiepDat;
        this.sanTram = sanTram;
        this.tinh = tinh;
        this.viDo = viDo;
        this.kinhDo = kinhDo;
        this.iDQuanLy = iDQuanLy;
        this.banKinhPhuSong = banKinhPhuSong;
    }

    public Integer getIDTram() {
        return iDTram;
    }

    public void setIDTram(Integer iDTram) {
        this.iDTram = iDTram;
    }

    public String getTenTram() {
        return tenTram;
    }

    public void setTenTram(String tenTram) {
        this.tenTram = tenTram;
    }

    public Integer getCotAnten() {
        return cotAnten;
    }

    public void setCotAnten(Integer cotAnten) {
        this.cotAnten = cotAnten;
    }

    public Integer getSanTram() {
        return sanTram;
    }

    public void setSanTram(Integer sanTram) {
        this.sanTram = sanTram;
    }
    public Integer getCotTiepDat() {
        return cotTiepDat;
    }

    public void setCotTiepDat(Integer cotTiepDat) {
        this.cotTiepDat = cotTiepDat;
    }

    public String getTinh() {
        return tinh;
    }

    public void setTinh(String tinh) {
        this.tinh = tinh;
    }

    public Double getViDo() {
        return viDo;
    }

    public void setViDo(Double viDo) {
        this.viDo = viDo;
    }

    public Double getKinhDo() {
        return kinhDo;
    }

    public void setKinhDo(Double kinhDo) {
        this.kinhDo = kinhDo;
    }

    public String getIDQuanLy() {
        return iDQuanLy;
    }

    public void setIDQuanLy(String iDQuanLy) {
        this.iDQuanLy = iDQuanLy;
    }

    public Double getBanKinhPhuSong() {
        return banKinhPhuSong;
    }

    public void setBanKinhPhuSong(Double banKinhPhuSong) {
        this.banKinhPhuSong = banKinhPhuSong;
    }

    @Override
    public String toString() {
        return tenTram;
    }

}
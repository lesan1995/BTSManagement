package com.example.lequa.btsmanagement.model;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "iDNhatKy")
public class NhatKy {
    @NonNull
    @SerializedName("IDNhatKy")
    private Integer iDNhatKy;
    @SerializedName("IDQuanLy")
    private String iDQuanLy;
    @SerializedName("IDTram")
    private Integer iDTram;
    @SerializedName("Loai")
    private String loai;
    @SerializedName("TieuDe")
    private String tieuDe;
    @SerializedName("ThoiGian")
    private String thoiGian;
    @SerializedName("NoiDung")
    private String noiDung;
    @SerializedName("DaGiaiQuyet")
    private Boolean daGiaiQuyet;

    /**
     * No args constructor for use in serialization
     *
     */
    public NhatKy() {
    }

    /**
     *
     * @param iDNhatKy
     * @param noiDung
     * @param tieuDe
     * @param loai
     * @param daGiaiQuyet
     * @param iDTram
     * @param thoiGian
     * @param iDQuanLy
     */
    public NhatKy(Integer iDNhatKy, String iDQuanLy, Integer iDTram, String loai, String tieuDe, String thoiGian, String noiDung, Boolean daGiaiQuyet) {
        this.iDNhatKy = iDNhatKy;
        this.iDQuanLy = iDQuanLy;
        this.iDTram = iDTram;
        this.loai = loai;
        this.tieuDe = tieuDe;
        this.thoiGian = thoiGian;
        this.noiDung = noiDung;
        this.daGiaiQuyet = daGiaiQuyet;
    }

    public Integer getIDNhatKy() {
        return iDNhatKy;
    }

    public void setIDNhatKy(Integer iDNhatKy) {
        this.iDNhatKy = iDNhatKy;
    }

    public String getIDQuanLy() {
        return iDQuanLy;
    }

    public void setIDQuanLy(String iDQuanLy) {
        this.iDQuanLy = iDQuanLy;
    }

    public Integer getIDTram() {
        return iDTram;
    }

    public void setIDTram(Integer iDTram) {
        this.iDTram = iDTram;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Boolean getDaGiaiQuyet() {
        return daGiaiQuyet;
    }

    public void setDaGiaiQuyet(Boolean daGiaiQuyet) {
        this.daGiaiQuyet = daGiaiQuyet;
    }

}

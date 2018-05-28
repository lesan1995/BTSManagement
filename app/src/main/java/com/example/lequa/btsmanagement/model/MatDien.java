package com.example.lequa.btsmanagement.model;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "iDMatDien")
public class MatDien {
    @NonNull
    @SerializedName("IDMatDien")
    private Integer iDMatDien;
    @SerializedName("IDTram")
    private Integer iDTram;
    @SerializedName("NgayMatDien")
    private String ngayMatDien;
    @SerializedName("GioMatDien")
    private String gioMatDien;
    @SerializedName("ThoiGianMayNo")
    private String thoiGianMayNo;
    @SerializedName("ThoiGianNgung")
    private String thoiGianNgung;
    @SerializedName("TongThoiGianChay")
    private String tongThoiGianChay;
    @SerializedName("QuangDuongDiChuyen")
    private Double quangDuongDiChuyen;
    @SerializedName("TienPhat")
    private Integer tienPhat;

    /**
     * No args constructor for use in serialization
     *
     */
    public MatDien() {
    }
    /**
     *
     * @param gioMatDien
     * @param tienPhat
     * @param thoiGianNgung
     * @param ngayMatDien
     * @param quangDuongDiChuyen
     * @param tongThoiGianChay
     * @param iDTram
     * @param iDMatDien
     * @param thoiGianMayNo
     */
    public MatDien(Integer iDMatDien, Integer iDTram, String ngayMatDien, String gioMatDien, String thoiGianMayNo, String thoiGianNgung, String tongThoiGianChay, Double quangDuongDiChuyen, Integer tienPhat) {
        this.iDMatDien = iDMatDien;
        this.iDTram = iDTram;
        this.ngayMatDien = ngayMatDien;
        this.gioMatDien = gioMatDien;
        this.thoiGianMayNo = thoiGianMayNo;
        this.thoiGianNgung = thoiGianNgung;
        this.tongThoiGianChay = tongThoiGianChay;
        this.quangDuongDiChuyen = quangDuongDiChuyen;
        this.tienPhat = tienPhat;
    }

    public Integer getIDMatDien() {
        return iDMatDien;
    }

    public void setIDMatDien(Integer iDMatDien) {
        this.iDMatDien = iDMatDien;
    }

    public Integer getIDTram() {
        return iDTram;
    }

    public void setIDTram(Integer iDTram) {
        this.iDTram = iDTram;
    }

    public String getNgayMatDien() {
        return ngayMatDien;
    }

    public void setNgayMatDien(String ngayMatDien) {
        this.ngayMatDien = ngayMatDien;
    }

    public String getGioMatDien() {
        return gioMatDien;
    }

    public void setGioMatDien(String gioMatDien) {
        this.gioMatDien = gioMatDien;
    }

    public String getThoiGianMayNo() {
        return thoiGianMayNo;
    }

    public void setThoiGianMayNo(String thoiGianMayNo) {
        this.thoiGianMayNo = thoiGianMayNo;
    }

    public String getThoiGianNgung() {
        return thoiGianNgung;
    }

    public void setThoiGianNgung(String thoiGianNgung) {
        this.thoiGianNgung = thoiGianNgung;
    }

    public String getTongThoiGianChay() {
        return tongThoiGianChay;
    }

    public void setTongThoiGianChay(String tongThoiGianChay) {
        this.tongThoiGianChay = tongThoiGianChay;
    }

    public Double getQuangDuongDiChuyen() {
        return quangDuongDiChuyen;
    }

    public void setQuangDuongDiChuyen(Double quangDuongDiChuyen) {
        this.quangDuongDiChuyen = quangDuongDiChuyen;
    }

    public Integer getTienPhat() {
        return tienPhat;
    }

    public void setTienPhat(Integer tienPhat) {
        this.tienPhat = tienPhat;
    }

}

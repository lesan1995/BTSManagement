package com.example.lequa.btsmanagement.model;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "iDUser")
public class UserBTS {
    @NonNull
    @SerializedName("IDUser")
    @Expose
    private String iDUser;
    @SerializedName("Ten")
    @Expose
    private String ten;
    @SerializedName("DiaChi")
    @Expose
    private String diaChi;
    @SerializedName("NgaySinh")
    @Expose
    private String ngaySinh;
    @SerializedName("GioiTinh")
    @Expose
    private String gioiTinh;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("ChucVu")
    @Expose
    private String chucVu;

    public String getIDUser() {
        return iDUser;
    }

    public void setIDUser(String iDUser) {
        this.iDUser = iDUser;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public UserBTS() {
    }

    /**
     *
     * @param gioiTinh
     * @param ten
     * @param phone
     * @param email
     * @param ngaySinh
     * @param diaChi
     * @param iDUser
     * @param image
     * @param chucVu
     */
    public UserBTS(String iDUser, String ten, String diaChi, String ngaySinh, String gioiTinh, String image, String email, String phone, String chucVu) {
        this.iDUser = iDUser;
        this.ten = ten;
        this.diaChi = diaChi;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.image = image;
        this.email = email;
        this.phone = phone;
        this.chucVu = chucVu;
    }
}
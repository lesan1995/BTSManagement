package com.example.lequa.btsmanagement.model;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "email")
public class Register {
    @SerializedName("Email")
    private String email;
    @SerializedName("Ten")
    private String ten;
    @SerializedName("DiaChi")
    private String diaChi;
    @SerializedName("NgaySinh")
    private String ngaySinh;
    @SerializedName("GioiTinh")
    private String gioiTinh;
    @SerializedName("Phone")
    private String phone;
    @SerializedName("Image")
    private String image;
    @SerializedName("ChucVu")
    private String chucVu;
    @SerializedName("Password")
    private String password;
    @SerializedName("ConfirmPassword")
    private String confirmPassword;

    /**
     * No args constructor for use in serialization
     *
     */
    public Register() {
    }

    /**
     *
     * @param gioiTinh
     * @param ten
     * @param confirmPassword
     * @param phone
     * @param email
     * @param ngaySinh
     * @param diaChi
     * @param image
     * @param password
     * @param chucVu
     */
    public Register(String email, String ten, String diaChi, String ngaySinh, String gioiTinh, String phone, String image, String chucVu, String password, String confirmPassword) {
        this.email = email;
        this.ten = ten;
        this.diaChi = diaChi;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.phone = phone;
        this.image = image;
        this.chucVu = chucVu;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}

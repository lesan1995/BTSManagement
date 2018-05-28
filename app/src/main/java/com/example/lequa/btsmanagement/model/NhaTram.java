package com.example.lequa.btsmanagement.model;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "iDNhaTram")
public class NhaTram {
    @NonNull
    @SerializedName("IDNhaTram")
    private Integer iDNhaTram;
    @SerializedName("IDNhaMang")
    private Integer iDNhaMang;
    @SerializedName("IDTram")
    private Integer iDTram;
    @SerializedName("CauCap")
    private Integer cauCap;
    @SerializedName("HeThongDien")
    private Integer heThongDien;
    @SerializedName("HangRao")
    private Integer hangRao;
    @SerializedName("DieuHoa")
    private Integer dieuHoa;
    @SerializedName("OnAp")
    private Integer onAp;
    @SerializedName("CanhBao")
    private Integer canhBao;
    @SerializedName("BinhCuuHoa")
    private Integer binhCuuHoa;
    @SerializedName("MayPhatDien")
    private Integer mayPhatDien;
    @SerializedName("ChungMayPhat")
    private Boolean chungMayPhat;

    /**
     * No args constructor for use in serialization
     *
     */
    public NhaTram() {
    }

    /**
     *
     * @param chungMayPhat
     * @param binhCuuHoa
     * @param dieuHoa
     * @param canhBao
     * @param cauCap
     * @param iDNhaMang
     * @param iDNhaTram
     * @param hangRao
     * @param mayPhatDien
     * @param heThongDien
     * @param iDTram
     * @param onAp
     */
    public NhaTram(Integer iDNhaTram, Integer iDNhaMang, Integer iDTram, Integer cauCap, Integer heThongDien, Integer hangRao, Integer dieuHoa, Integer onAp, Integer canhBao, Integer binhCuuHoa, Integer mayPhatDien, Boolean chungMayPhat) {
        this.iDNhaTram = iDNhaTram;
        this.iDNhaMang = iDNhaMang;
        this.iDTram = iDTram;
        this.cauCap = cauCap;
        this.heThongDien = heThongDien;
        this.hangRao = hangRao;
        this.dieuHoa = dieuHoa;
        this.onAp = onAp;
        this.canhBao = canhBao;
        this.binhCuuHoa = binhCuuHoa;
        this.mayPhatDien = mayPhatDien;
        this.chungMayPhat = chungMayPhat;
    }

    public Integer getIDNhaTram() {
        return iDNhaTram;
    }

    public void setIDNhaTram(Integer iDNhaTram) {
        this.iDNhaTram = iDNhaTram;
    }

    public Integer getIDNhaMang() {
        return iDNhaMang;
    }

    public void setIDNhaMang(Integer iDNhaMang) {
        this.iDNhaMang = iDNhaMang;
    }

    public Integer getIDTram() {
        return iDTram;
    }

    public void setIDTram(Integer iDTram) {
        this.iDTram = iDTram;
    }

    public Integer getCauCap() {
        return cauCap;
    }

    public void setCauCap(Integer cauCap) {
        this.cauCap = cauCap;
    }

    public Integer getHeThongDien() {
        return heThongDien;
    }

    public void setHeThongDien(Integer heThongDien) {
        this.heThongDien = heThongDien;
    }

    public Integer getHangRao() {
        return hangRao;
    }

    public void setHangRao(Integer hangRao) {
        this.hangRao = hangRao;
    }

    public Integer getDieuHoa() {
        return dieuHoa;
    }

    public void setDieuHoa(Integer dieuHoa) {
        this.dieuHoa = dieuHoa;
    }

    public Integer getOnAp() {
        return onAp;
    }

    public void setOnAp(Integer onAp) {
        this.onAp = onAp;
    }

    public Integer getCanhBao() {
        return canhBao;
    }

    public void setCanhBao(Integer canhBao) {
        this.canhBao = canhBao;
    }

    public Integer getBinhCuuHoa() {
        return binhCuuHoa;
    }

    public void setBinhCuuHoa(Integer binhCuuHoa) {
        this.binhCuuHoa = binhCuuHoa;
    }

    public Integer getMayPhatDien() {
        return mayPhatDien;
    }

    public void setMayPhatDien(Integer mayPhatDien) {
        this.mayPhatDien = mayPhatDien;
    }

    public Boolean getChungMayPhat() {
        return chungMayPhat;
    }

    public void setChungMayPhat(Boolean chungMayPhat) {
        this.chungMayPhat = chungMayPhat;
    }

}

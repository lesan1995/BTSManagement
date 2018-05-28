package com.example.lequa.btsmanagement.model;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "oldPassword")
public class Password {

    @SerializedName("OldPassword")
    private String oldPassword;
    @SerializedName("NewPassword")
    private String newPassword;
    @SerializedName("ConfirmPassword")
    private String confirmPassword;
    /**
     * No args constructor for use in serialization
     *
     */
    public Password() {
    }

    /**
     *
     * @param confirmPassword
     * @param newPassword
     * @param oldPassword
     */
    public Password(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}

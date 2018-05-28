package com.example.lequa.btsmanagement.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.lequa.btsmanagement.model.HinhAnhTram;
import com.example.lequa.btsmanagement.model.Login;
import com.example.lequa.btsmanagement.model.MatDien;
import com.example.lequa.btsmanagement.model.NhaMang;
import com.example.lequa.btsmanagement.model.NhaTram;
import com.example.lequa.btsmanagement.model.NhatKy;
import com.example.lequa.btsmanagement.model.Tram;
import com.example.lequa.btsmanagement.model.UserBTS;

@Database(entities = {Login.class, UserBTS.class,Tram.class,HinhAnhTram.class,
        NhaTram.class, NhaMang.class,MatDien.class,NhatKy.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract LoginDAO loginDAO();
    public abstract UserDAO userDAO();
    public abstract TramDAO tramDAO();
    public abstract HinhAnhTramDAO hinhAnhTramDAO();
    public abstract NhaTramDAO nhaTramDAO();
    public abstract NhaMangDAO nhaMangDAO();
    public abstract MatDienDAO matDienDAO();
    public abstract NhatKyDAO nhatKyDAO();
}

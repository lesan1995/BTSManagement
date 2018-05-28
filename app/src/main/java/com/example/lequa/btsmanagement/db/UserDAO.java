package com.example.lequa.btsmanagement.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.lequa.btsmanagement.model.UserBTS;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDAO {
    @Insert(onConflict = REPLACE)
    void save(UserBTS userBTS);
    @Insert(onConflict = REPLACE)
    void save(List<UserBTS> listUserBTS);
    @Query("SELECT * FROM UserBTS WHERE iDUser = :idUser")
    LiveData<UserBTS> load(String idUser);
    @Query("SELECT * FROM UserBTS WHERE email = :email")
    LiveData<UserBTS> loadWithEmail(String email);
    @Query("SELECT * FROM UserBTS")
    LiveData<List<UserBTS>> loadAll();
    @Query("SELECT * FROM UserBTS where ten LIKE :ten")
    LiveData<List<UserBTS>> loadWithName(String ten);
    @Query("SELECT * FROM UserBTS where chucVu ='QuanLy'")
    LiveData<List<UserBTS>> loadAllQuanLy();
    @Query("DELETE FROM UserBTS")
    void deleteTable();
}

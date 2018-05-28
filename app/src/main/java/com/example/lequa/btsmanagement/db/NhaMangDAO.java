package com.example.lequa.btsmanagement.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.lequa.btsmanagement.model.NhaMang;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface NhaMangDAO {
    @Insert(onConflict = REPLACE)
    void save(NhaMang nhaMang);
    @Insert(onConflict = REPLACE)
    void save(List<NhaMang> nhaMang);
    @Query("SELECT * FROM NhaMang where iDNhaMang= :idNhaMang")
    LiveData<NhaMang> load(int idNhaMang);
    @Query("SELECT * FROM NhaMang where tenNhaMang= :tenNhaMang")
    LiveData<NhaMang> load(String tenNhaMang);
    @Query("SELECT * FROM NhaMang")
    LiveData<List<NhaMang>> load();
    @Query("SELECT * FROM NhaMang where iDNhaMang != :idNhaMang")
    LiveData<List<NhaMang>> loadExcept(int idNhaMang);
    @Query("DELETE FROM NhaMang")
    void deleteTable();
}

package com.example.lequa.btsmanagement.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.lequa.btsmanagement.model.NhaTram;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface NhaTramDAO {
    @Insert(onConflict = REPLACE)
    void save(List<NhaTram> nhaTram);
    @Insert(onConflict = REPLACE)
    void save(NhaTram nhaTram);
    @Query("SELECT * FROM NhaTram where iDNhaTram= :idNhaTram")
    LiveData<NhaTram> load(int idNhaTram);
    @Query("SELECT * FROM NhaTram where iDTram= :idTram")
    LiveData<List<NhaTram>> loadWithIDTram(int idTram);
    @Query("DELETE FROM NhaTram where iDNhaTram= :idNhaTram")
    void delete(int idNhaTram);
    @Query("DELETE FROM NhaTram")
    void deleteTable();
}

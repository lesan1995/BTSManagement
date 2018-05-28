package com.example.lequa.btsmanagement.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.lequa.btsmanagement.model.NhatKy;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface NhatKyDAO {
    @Insert(onConflict = REPLACE)
    void save(List<NhatKy> nhatKy);
    @Insert(onConflict = REPLACE)
    void save(NhatKy nhatKy);
    @Query("SELECT * FROM NhatKy")
    LiveData<NhatKy> load();
    @Query("SELECT * FROM NhatKy where iDNhatKy= :iDNhatKy")
    LiveData<NhatKy> load(int iDNhatKy);
    @Query("SELECT * FROM NhatKy where iDTram= :idTram ORDER BY thoiGian DESC")
    LiveData<List<NhatKy>> loadWithIDTram(int idTram);
    @Query("SELECT * FROM NhatKy where iDTram= :idTram AND loai='NhatKy' ORDER BY thoiGian DESC")
    LiveData<List<NhatKy>> loadNKWithIDTram(int idTram);
    @Query("SELECT * FROM NhatKy where iDTram= :idTram AND loai='SuCo' ORDER BY thoiGian DESC")
    LiveData<List<NhatKy>> loadSCWithIDTram(int idTram);

    @Query("SELECT * FROM NhatKy where loai='SuCo' ORDER BY thoiGian DESC")
    LiveData<List<NhatKy>> loadSC();
    @Query("SELECT * FROM NhatKy where loai='SuCo' AND daGiaiQuyet=1 ORDER BY thoiGian DESC")
    LiveData<List<NhatKy>> loadSCHT();
    @Query("SELECT * FROM NhatKy where loai='SuCo' AND daGiaiQuyet=0 ORDER BY thoiGian DESC")
    LiveData<List<NhatKy>> loadSCCHT();


    @Query("DELETE FROM NhatKy where iDNhatKy= :idNhatKy")
    void delete(int idNhatKy);
    @Query("DELETE FROM NhatKy")
    void deleteTable();
}

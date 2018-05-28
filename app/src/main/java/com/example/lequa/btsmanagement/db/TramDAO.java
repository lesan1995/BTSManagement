package com.example.lequa.btsmanagement.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.lequa.btsmanagement.model.Tram;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


@Dao
public interface TramDAO {
    @Insert(onConflict = REPLACE)
    void save(List<Tram> tram);
    @Insert(onConflict = REPLACE)
    void save(Tram tram);
    @Query("SELECT * FROM Tram")
    LiveData<List<Tram>> load();
    @Query("SELECT * FROM Tram where tenTram LIKE :tenTram")
    LiveData<List<Tram>> loadWithTenTram(String tenTram);
    @Query("SELECT * FROM Tram where iDTram= :idTram")
    LiveData<Tram> load(int idTram);
    @Query("SELECT * FROM Tram where iDTram= :idUser")
    LiveData<List<Tram>> loadWithIDUser(String idUser);
    @Query("DELETE FROM Tram where iDTram= :idTram")
    void delete(int idTram);
    @Query("DELETE FROM Tram")
    void deleteTable();

    @Query("SELECT * FROM Tram WHERE iDTram IN (SELECT iDTram FROM NhaTram where iDNhaMang = :idNhaMang)")
    LiveData<List<Tram>> loadWithNhaMang(int idNhaMang);
}

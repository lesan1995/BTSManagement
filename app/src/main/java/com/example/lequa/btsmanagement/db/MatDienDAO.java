package com.example.lequa.btsmanagement.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.lequa.btsmanagement.model.MatDien;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MatDienDAO {
    @Insert(onConflict = REPLACE)
    void save(List<MatDien> matDien);
    @Insert(onConflict = REPLACE)
    void save(MatDien matDien);
    @Query("SELECT * FROM MatDien ORDER BY ngayMatDien,gioMatDien")
    LiveData<List<MatDien>> load();
    @Query("SELECT * FROM MatDien where iDMatDien = :idMatDien")
    LiveData<MatDien> load(int idMatDien);
    @Query("SELECT * FROM MatDien where iDTram = :idTram ORDER BY ngayMatDien DESC, gioMatDien DESC")
    LiveData<List<MatDien>> loadWithIDTram(int idTram);
    @Query("DELETE FROM MatDien")
    void deleteTable();
}

package com.example.lequa.btsmanagement.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.lequa.btsmanagement.model.HinhAnhTram;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface HinhAnhTramDAO {
    @Insert(onConflict = REPLACE)
    void save(HinhAnhTram hinhAnhTram);
    @Insert(onConflict = REPLACE)
    void save(List<HinhAnhTram> listHinhAnhTram);
    @Query("SELECT * FROM HinhAnhTram WHERE iDHinhAnh = :idHinhAnh")
    LiveData<HinhAnhTram> load(int idHinhAnh);
    @Query("SELECT * FROM HinhAnhTram WHERE iDTram = :idTram")
    LiveData<List<HinhAnhTram>> loadByIDTram(int idTram);
    @Query("DELETE FROM HinhAnhTram where iDHinhAnh= :idHinhAnh")
    void delete(int idHinhAnh);
    @Query("DELETE FROM HinhAnhTram")
    void deleteTable();
}

package com.example.kwesi.labrat;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MouseDao {
    @Query("SELECT * FROM mouse")
    List<Mouse> getAll();

    @Insert
    void insert(Mouse mouse);

    @Delete
    void delete(Mouse mouse);



}

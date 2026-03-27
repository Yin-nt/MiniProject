package com.example.miniproject2.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface TheaterDao {
    @Query("SELECT * FROM theaters")
    List<Theater> getAllTheaters();

    @Insert
    long insert(Theater theater);
}
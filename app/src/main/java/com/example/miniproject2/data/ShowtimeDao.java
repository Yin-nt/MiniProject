package com.example.miniproject2.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ShowtimeDao {
    @Query("SELECT * FROM showtimes WHERE id = :showtimeId LIMIT 1")
    Showtime getShowtimeById(int showtimeId);

    @Query("SELECT * FROM showtimes")
    List<Showtime> getAllShowtimes();

    @Query("SELECT * FROM showtimes WHERE movieId = :movieId")
    List<Showtime> getShowtimesByMovie(int movieId);

    @Query("SELECT * FROM showtimes WHERE theaterId = :theaterId")
    List<Showtime> getShowtimesByTheater(int theaterId);

    @Insert
    long insert(Showtime showtime);
}

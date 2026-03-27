package com.example.miniproject2.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface TicketDao {
    @Query("SELECT * FROM tickets WHERE userId = :userId")
    List<Ticket> getTicketsByUser(int userId);

    @Query("SELECT seatNumber FROM tickets WHERE showtimeId = :showtimeId")
    List<String> getBookedSeats(int showtimeId);

    @Query("SELECT EXISTS(SELECT 1 FROM tickets WHERE showtimeId = :showtimeId AND seatNumber = :seatNumber)")
    boolean isSeatBooked(int showtimeId, String seatNumber);

    @Insert
    long insert(Ticket ticket);
}

package com.example.miniproject2.dal;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.miniproject2.entities.User;

@Dao
public interface DAO {
    // ---------------- BẢNG USERS ----------------
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM Users WHERE username = :u AND password = :p")
    User login(String u, String p);

    @Query("SELECT COUNT(*) FROM Users")
    int getUserCount();


}
package com.example.miniproject2.dal;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.miniproject2.entities.User;
// import com.example.shoppingapp.entities.Product; // Mở ra khi đồng đội code xong

// Khi ghép code, thêm các class khác vào đây: entities = {User.class, Product.class, ...}
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDB extends RoomDatabase {

    public abstract DAO dao();
    private static AppDB INSTANCE;

    public static AppDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDB.class,
                    "ShoppingDB"
            ).allowMainThreadQueries().build(); // Cho phép chạy trên luồng chính để test
        }
        return INSTANCE;
    }
}

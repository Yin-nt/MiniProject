package com.example.miniproject2.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface OrderDao {
    @Query("SELECT * FROM orders WHERE userId = :userId")
    List<Order> getOrdersByUser(int userId);

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    Order getOrderById(int orderId);

    @Insert
    long insert(Order order);

    @Update
    void update(Order order);

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    void updateOrderStatus(int orderId, String status);
}
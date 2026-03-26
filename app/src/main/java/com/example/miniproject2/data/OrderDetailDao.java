package com.example.miniproject2.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface OrderDetailDao {
    @Query("SELECT * FROM order_details WHERE orderId = :orderId")
    List<OrderDetail> getOrderDetailsByOrder(int orderId);

    @Insert
    void insertAll(OrderDetail... orderDetails);

    @Insert
    long insert(OrderDetail orderDetail);
}
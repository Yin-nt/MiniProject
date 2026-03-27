package com.example.miniproject2.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.miniproject2.data.User;

import java.util.List;

@Dao
public interface UserDao {

    // 1. Đăng nhập: Trả về User nếu khớp username và password
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    // 2. Đăng ký: Thêm một user mới (Trả về ID của user vừa tạo)
    @Insert
    long insert(User user);

    // 3. Kiểm tra Username: Dùng khi Đăng ký để tránh trùng tên tài khoản
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);

    // 4. Lấy thông tin theo ID: Dùng để hiển thị Profile sau khi đã đăng nhập
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User getUserById(int id);

    // 5. Cập nhật thông tin: Dùng khi user muốn đổi mật khẩu hoặc đổi tên
    @Update
    void update(User user);

    // 6. Xóa user (nếu cần)
    @Delete
    void delete(User user);

    // 7. Đếm số lượng user: Dùng để check xem có cần chèn dữ liệu mẫu (Seed data) không
    @Query("SELECT COUNT(*) FROM users")
    int getUserCount();
}
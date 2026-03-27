package com.example.miniproject2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miniproject2.R;
import com.example.miniproject2.dal.AppDB;
import com.example.miniproject2.entities.User;
// TODO: Mở comment 2 dòng dưới khi đồng đội của bạn đã tạo file Category.java và Product.java
// import com.example.miniproject2.entities.Category;
// import com.example.miniproject2.entities.Product;
import com.example.miniproject2.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    EditText edtUser, edtPass;
    Button btnLogin;
    AppDB db;
    SessionManager sessionManager;
    int pendingProductId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUser = findViewById(R.id.edtUsername);
        edtPass = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        db = AppDB.getInstance(this);
        sessionManager = new SessionManager(this);

        // Gọi hàm chèn toàn bộ dữ liệu mẫu
        insertDummyData();

        pendingProductId = getIntent().getIntExtra("PENDING_PRODUCT_ID", -1);

        btnLogin.setOnClickListener(v -> {
            String u = edtUser.getText().toString().trim();
            String p = edtPass.getText().toString().trim();

            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            User acc = db.dao().login(u, p);

            if (acc != null) {
                // Lưu session
                sessionManager.saveLoginSession(acc.getId());
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                // Logic Gatekeeper
                if (pendingProductId != -1) {
                    // Mở comment khi nhóm đã có Activity này
                    // Intent intent = new Intent(this, CreateOrderActivity.class);
                    // intent.putExtra("PRODUCT_ID", pendingProductId);
                    // startActivity(intent);
                } else {
                    // Mở comment khi nhóm đã có Activity này
                    // startActivity(new Intent(this, MainActivity.class));
                }
                finish();
            } else {
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertDummyData() {
        // Kiểm tra xem DB đã có user chưa, nếu chưa thì mới chèn data để tránh chèn lặp lại mỗi lần mở app
        if (db.dao().getUserCount() == 0) {

            // 1. Thêm User admin
            User u = new User();
            u.setUsername("admin");
            u.setPassword("123");
            u.setFullName("Quản trị viên");
            db.dao().insertUser(u);

            /* ========================================================================
             PHẦN DƯỚI ĐÂY LÀ DỮ LIỆU CỦA ĐỒNG ĐỘI
             Bạn hãy bỏ comment (xóa ký tự /* và KHI VÀ CHỈ KHI đồng đội đã:
            1. Tạo xong file Category.java và Product.java
            2. Thêm hàm insertCategory() và insertProduct() vào file DAO.java
                    ========================================================================
                    */

            /*
            // 2. Thêm Danh mục (Categories)
            Category c1 = new Category();
            c1.name = "Điện thoại";

            Category c2 = new Category();
            c2.name = "Laptop";

            db.dao().insertCategory(c1);
            db.dao().insertCategory(c2);

            // 3. Thêm Sản phẩm (Products)
            Product p1 = new Product();
            p1.name = "iPhone 15 Pro Max";
            p1.price = 30000000;
            p1.categoryId = 1; // Khớp với ID của Điện thoại

            Product p2 = new Product();
            p2.name = "Samsung Galaxy S24 Ultra";
            p2.price = 32000000;
            p2.categoryId = 1;

            Product p3 = new Product();
            p3.name = "MacBook Pro M3";
            p3.price = 40000000;
            p3.categoryId = 2; // Khớp với ID của Laptop

            db.dao().insertProduct(p1);
            db.dao().insertProduct(p2);
            db.dao().insertProduct(p3);
            */
        }
    }
}
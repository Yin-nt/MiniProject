package com.example.miniproject2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miniproject2.ui.HomeActivity; // Đảm bảo đúng package của MainActivity
import com.example.miniproject2.R;
import com.example.miniproject2.data.AppDatabase;
import com.example.miniproject2.data.User;
import com.example.miniproject2.utils.SessionManager;

import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    EditText edtUser, edtPass;
    Button btnLogin;
    AppDatabase db;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUser = findViewById(R.id.edtUsername);
        edtPass = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        db = AppDatabase.getDatabase(this);
        sessionManager = new SessionManager(this);

        btnLogin.setOnClickListener(v -> {
            String u = edtUser.getText().toString().trim();
            String p = edtPass.getText().toString().trim();

            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Room không cho phép truy vấn trên Main Thread, dùng Executor để chạy ngầm
            Executors.newSingleThreadExecutor().execute(() -> {
                // Sử dụng userDao() đã định nghĩa trong AppDatabase
                User acc = db.userDao().login(u, p);

                runOnUiThread(() -> {
                    if (acc != null) {
                        // Lưu session
                        sessionManager.saveLoginSession(acc.getId());
                        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }
}
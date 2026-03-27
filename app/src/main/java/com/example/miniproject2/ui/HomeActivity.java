package com.example.miniproject2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;
import com.example.miniproject2.adapters.MovieAdapter;
import com.example.miniproject2.data.AppDatabase;
import com.example.miniproject2.data.Movie;
import com.example.miniproject2.utils.SessionManager;

import java.util.List;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    private Button btnLoginLogout;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);

        btnLoginLogout = findViewById(R.id.btnLoginLogout);
        Button btnTheaters = findViewById(R.id.btnTheaters);
        Button btnShowtimes = findViewById(R.id.btnShowtimes);
        Button btnMyTickets = findViewById(R.id.btnMyTickets);

        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Movie> movies = AppDatabase.getDatabase(this).movieDao().getAllMovies();
            runOnUiThread(() -> rvMovies.setAdapter(new MovieAdapter(movies)));
        });

        btnLoginLogout.setOnClickListener(v -> {
            if (sessionManager.checkIsLoggedIn()) {
                sessionManager.logout();
                updateLoginButton();
                Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        btnTheaters.setOnClickListener(v ->
                startActivity(new Intent(this, TheatersActivity.class)));

        btnShowtimes.setOnClickListener(v ->
                startActivity(new Intent(this, ShowtimesActivity.class)));

        btnMyTickets.setOnClickListener(v ->
                startActivity(new Intent(this, MyTicketsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLoginButton();
    }

    private void updateLoginButton() {
        if (sessionManager.checkIsLoggedIn()) {
            btnLoginLogout.setText("Đăng xuất");
        } else {
            btnLoginLogout.setText("Đăng nhập");
        }
    }
}

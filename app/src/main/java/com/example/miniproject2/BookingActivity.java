package com.example.miniproject2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.miniproject2.data.AppDatabase;
import com.example.miniproject2.data.Movie;
import com.example.miniproject2.data.Showtime;
import com.example.miniproject2.data.Theater;
import com.example.miniproject2.data.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class BookingActivity extends AppCompatActivity {

    public static final String EXTRA_SHOWTIME_ID = "EXTRA_SHOWTIME_ID";
    private static final String PREFS_NAME = "MOVIE_PREFS";
    private static final String KEY_IS_LOGGED_IN = "IS_LOGGED_IN";
    private static final String KEY_USER_ID = "USER_ID";

    private TextView tvMovieTitle, tvTheaterName, tvShowtime;
    private Spinner spinnerSeat;
    private Button btnBook;

    private AppDatabase db;
    private int showtimeId;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Check login status
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false);

        if (!isLoggedIn) {
            // Redirect to LoginActivity (Dev 2's screen)
            try {
                Intent loginIntent = new Intent(this, Class.forName("com.example.miniproject2.LoginActivity"));
                startActivity(loginIntent);
            } catch (ClassNotFoundException e) {
                Toast.makeText(this, "Vui lòng đăng nhập trước khi đặt vé", Toast.LENGTH_LONG).show();
            }
            finish();
            return;
        }

        userId = prefs.getInt(KEY_USER_ID, -1);

        // Get showtimeId from Intent
        showtimeId = getIntent().getIntExtra(EXTRA_SHOWTIME_ID, -1);
        if (showtimeId == -1) {
            Toast.makeText(this, "Không tìm thấy suất chiếu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Init views
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvTheaterName = findViewById(R.id.tvTheaterName);
        tvShowtime = findViewById(R.id.tvShowtime);
        spinnerSeat = findViewById(R.id.spinnerSeat);
        btnBook = findViewById(R.id.btnBook);

        db = AppDatabase.getDatabase(this);

        // Setup seat spinner
        setupSeatSpinner();

        // Load showtime info
        loadShowtimeInfo();

        // Book button click
        btnBook.setOnClickListener(v -> bookTicket());
    }

    private void setupSeatSpinner() {
        List<String> seats = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            seats.add("Ghế A" + i);
        }
        for (int i = 1; i <= 10; i++) {
            seats.add("Ghế B" + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, seats);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeat.setAdapter(adapter);
    }

    private void loadShowtimeInfo() {
        Executors.newSingleThreadExecutor().execute(() -> {
            Showtime showtime = db.showtimeDao().getShowtimeById(showtimeId);
            if (showtime == null) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Suất chiếu không tồn tại", Toast.LENGTH_SHORT).show();
                    finish();
                });
                return;
            }

            Movie movie = db.movieDao().getMovieById(showtime.movieId);
            Theater theater = db.theaterDao().getTheaterById(showtime.theaterId);

            runOnUiThread(() -> {
                tvMovieTitle.setText("Phim: " + (movie != null ? movie.title : "N/A"));
                tvTheaterName.setText("Rạp: " + (theater != null ? theater.name : "N/A"));
                tvShowtime.setText("Giờ chiếu: " + showtime.time);
            });
        });
    }

    private void bookTicket() {
        String seatNumber = spinnerSeat.getSelectedItem().toString();

        Executors.newSingleThreadExecutor().execute(() -> {
            Ticket ticket = new Ticket(userId, showtimeId, seatNumber);
            long ticketId = db.ticketDao().insert(ticket);

            runOnUiThread(() -> {
                if (ticketId > 0) {
                    new AlertDialog.Builder(this)
                            .setTitle("Đặt vé thành công!")
                            .setMessage("Ghế: " + seatNumber + "\nMã vé: " + ticketId)
                            .setPositiveButton("OK", (dialog, which) -> finish())
                            .setCancelable(false)
                            .show();
                } else {
                    Toast.makeText(this, "Đặt vé thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

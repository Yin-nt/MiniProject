package com.example.miniproject2.ui;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.Gravity;
import android.graphics.drawable.GradientDrawable;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.miniproject2.R;
import com.example.miniproject2.data.AppDatabase;
import com.example.miniproject2.data.Movie;
import com.example.miniproject2.data.Showtime;
import com.example.miniproject2.data.Theater;
import com.example.miniproject2.data.Ticket;
import com.example.miniproject2.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

public class BookingActivity extends AppCompatActivity {

    public static final String EXTRA_SHOWTIME_ID = "EXTRA_SHOWTIME_ID";

    private TextView tvMovieTitle, tvTheaterName, tvShowtime, tvSelectedSeat;
    private GridLayout gridRowA, gridRowB;
    private Button btnBook;

    private AppDatabase db;
    private int showtimeId;
    private Set<String> bookedSeats;
    private String selectedSeat;

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

        db = AppDatabase.getDatabase(this);

        showtimeId = getIntent().getIntExtra(EXTRA_SHOWTIME_ID, -1);
        if (showtimeId == -1) {
            Toast.makeText(this, "Không tìm thấy suất chiếu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvTheaterName = findViewById(R.id.tvTheaterName);
        tvShowtime = findViewById(R.id.tvShowtime);
        tvSelectedSeat = findViewById(R.id.tvSelectedSeat);
        gridRowA = findViewById(R.id.gridRowA);
        gridRowB = findViewById(R.id.gridRowB);
        btnBook = findViewById(R.id.btnBook);

        btnBook.setEnabled(false);

        loadBookedSeatsAndBuildGrid();
        loadShowtimeInfo();

        btnBook.setOnClickListener(v -> bookTicket());
    }

    private void loadBookedSeatsAndBuildGrid() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<String> booked = db.ticketDao().getBookedSeats(showtimeId);
            bookedSeats = new HashSet<>(booked);
            runOnUiThread(() -> {
                buildSeatGrid(gridRowA, "A");
                buildSeatGrid(gridRowB, "B");
            });
        });
    }

    private void buildSeatGrid(GridLayout grid, String row) {
        grid.removeAllViews();
        for (int i = 1; i <= 10; i++) {
            String seat = "Ghế " + row + i;
            Button seatBtn = createSeatButton(seat);
            grid.addView(seatBtn);
        }
    }

    private Button createSeatButton(String seat) {
        Button btn = new Button(this);
        btn.setBackground(null); // strip Material button background
        btn.setText(seat.replace("Ghế ", ""));
        GradientDrawable initialBg = new GradientDrawable();
        initialBg.setCornerRadius(12);
        boolean booked = bookedSeats.contains(seat);
        initialBg.setColor(booked ? 0xFFF44336 : 0xFF4CAF50);
        btn.setBackground(initialBg);
        btn.setTextColor(0xFFFFFFFF);
        btn.setEnabled(!booked);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(8, 8, 8, 8);
        btn.setLayoutParams(params);

        btn.setTextSize(14);
        btn.setGravity(Gravity.CENTER);
        btn.setMinHeight(0);
        btn.setMinWidth(0);
        btn.setPadding(16, 24, 16, 24);

        applySeatStyle(btn, seat);
        btn.setOnClickListener(v -> onSeatClicked(seat, btn));

        return btn;
    }

    private void applySeatStyle(Button btn, String seatText) {
        String seatFull = "Ghế " + seatText;
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(12);
        if (bookedSeats.contains(seatFull)) {
            drawable.setColor(0xFFF44336);
            btn.setEnabled(false);
            btn.setTextColor(0xFFFFFFFF);
        } else if (seatFull.equals(selectedSeat)) {
            drawable.setColor(0xFFFFC107);
            btn.setTextColor(0xFF000000);
        } else {
            drawable.setColor(0xFF4CAF50);
            btn.setTextColor(0xFFFFFFFF);
        }
        btn.setBackground(drawable);
    }

    private void onSeatClicked(String seat, Button btn) {
        if (bookedSeats.contains(seat)) {
            Toast.makeText(this, "Ghế này đã được đặt", Toast.LENGTH_SHORT).show();
            return;
        }

        // Deselect previous
        String prev = selectedSeat;
        selectedSeat = seat;

        // Re-apply styles for all seats (prev + new)
        refreshAllSeatStyles(
                prev != null ? prev.replace("Ghế ", "") : null,
                seat.replace("Ghế ", ""));

        tvSelectedSeat.setText("Ghế đã chọn: " + seat);
        btnBook.setEnabled(true);
    }

    private void refreshAllSeatStyles(String prev, String current) {
        refreshRowStyles(gridRowA, prev, current);
        refreshRowStyles(gridRowB, prev, current);
    }

    private void refreshRowStyles(GridLayout grid, String prev, String current) {
        for (int i = 0; i < grid.getChildCount(); i++) {
            Button btn = (Button) grid.getChildAt(i);
            String seat = btn.getText().toString();
            // Skip booked seats — they must always stay red + disabled
            String seatFull = "Ghế " + seat;
            if (bookedSeats.contains(seatFull)) continue;
            if (seat.equals(prev) || seat.equals(current)) {
                applySeatStyle(btn, seat);
            }
        }
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
        if (selectedSeat == null || bookedSeats.contains(selectedSeat)) {
            Toast.makeText(this, "Vui lòng chọn ghế hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = new SessionManager(this).getUserId();

        Executors.newSingleThreadExecutor().execute(() -> {
            Ticket ticket = new Ticket(userId, showtimeId, selectedSeat);
            try {
                long ticketId = db.ticketDao().insert(ticket);
                runOnUiThread(() -> {
                    if (ticketId > 0) {
                        new AlertDialog.Builder(this)
                                .setTitle("Đặt vé thành công!")
                                .setMessage("Ghế: " + selectedSeat + "\nMã vé: " + ticketId)
                                .setPositiveButton("Xem vé", (dialog, which) -> {
                                    startActivity(new Intent(this, MyTicketsActivity.class));
                                    finish();
                                })
                                .setNegativeButton("Đóng", (dialog, which) -> finish())
                                .setCancelable(false)
                                .show();
                    } else {
                        Toast.makeText(this, "Đặt vé thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (SQLiteConstraintException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Ghế vừa được đặt bởi người khác. Vui lòng chọn ghế khác.", Toast.LENGTH_LONG).show();
                    loadBookedSeatsAndBuildGrid();
                    selectedSeat = null;
                    tvSelectedSeat.setText("Chưa chọn ghế");
                    btnBook.setEnabled(false);
                });
            }
        });
    }
}

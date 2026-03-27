package com.example.miniproject2.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;
import com.example.miniproject2.adapters.TicketAdapter;
import com.example.miniproject2.data.AppDatabase;
import com.example.miniproject2.data.Movie;
import com.example.miniproject2.data.Showtime;
import com.example.miniproject2.data.Theater;
import com.example.miniproject2.data.Ticket;
import com.example.miniproject2.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MyTicketsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);

        Button btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        RecyclerView rvTickets = findViewById(R.id.rvTickets);
        rvTickets.setLayoutManager(new LinearLayoutManager(this));

        TextView tvEmpty = findViewById(R.id.tvEmpty);

        SessionManager sessionManager = new SessionManager(this);
        int userId = sessionManager.getUserId();

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            List<Ticket> tickets = db.ticketDao().getTicketsByUser(userId);

            // Build display info for each ticket
            List<TicketAdapter.TicketDisplay> displays = new ArrayList<>();
            for (Ticket ticket : tickets) {
                Showtime st = db.showtimeDao().getShowtimeById(ticket.showtimeId);
                String movieTitle = "N/A";
                String theaterName = "N/A";
                String time = "N/A";
                if (st != null) {
                    Movie movie = db.movieDao().getMovieById(st.movieId);
                    Theater theater = db.theaterDao().getTheaterById(st.theaterId);
                    if (movie != null) movieTitle = movie.title;
                    if (theater != null) theaterName = theater.name;
                    time = st.time;
                }
                displays.add(new TicketAdapter.TicketDisplay(
                    ticket.id, movieTitle, theaterName, time, ticket.seatNumber));
            }

            runOnUiThread(() -> {
                if (displays.isEmpty()) {
                    tvEmpty.setVisibility(android.view.View.VISIBLE);
                    rvTickets.setVisibility(android.view.View.GONE);
                } else {
                    tvEmpty.setVisibility(android.view.View.GONE);
                    rvTickets.setVisibility(android.view.View.VISIBLE);
                    rvTickets.setAdapter(new TicketAdapter(displays));
                }
            });
        });
    }
}

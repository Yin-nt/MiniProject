package com.example.miniproject2.ui;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;
import com.example.miniproject2.adapters.ShowtimeFullAdapter;
import com.example.miniproject2.data.AppDatabase;
import com.example.miniproject2.data.Movie;
import com.example.miniproject2.data.Showtime;
import com.example.miniproject2.data.Theater;

import java.util.List;
import java.util.concurrent.Executors;

public class ShowtimesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtimes);

        Button btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        RecyclerView rvShowtimes = findViewById(R.id.rvShowtimes);
        rvShowtimes.setLayoutManager(new LinearLayoutManager(this));

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            List<Showtime> showtimes = db.showtimeDao().getAllShowtimes();
            List<Theater> theaters = db.theaterDao().getAllTheaters();
            List<Movie> movies = db.movieDao().getAllMovies();

            for (Showtime st : showtimes) {
                for (Theater t : theaters) {
                    if (st.theaterId == t.id) { st.theaterName = t.name; break; }
                }
                for (Movie m : movies) {
                    if (st.movieId == m.id) { st.movieTitle = m.title; break; }
                }
            }

            runOnUiThread(() -> rvShowtimes.setAdapter(new ShowtimeFullAdapter(showtimes)));
        });
    }
}

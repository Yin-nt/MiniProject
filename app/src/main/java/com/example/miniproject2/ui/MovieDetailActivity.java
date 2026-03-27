package com.example.miniproject2.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;
import com.example.miniproject2.adapters.ShowtimeAdapter;
import com.example.miniproject2.data.AppDatabase;
import com.example.miniproject2.data.Movie;
import com.example.miniproject2.data.Showtime;
import com.example.miniproject2.data.Theater;

import java.util.List;
import java.util.concurrent.Executors;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Nút Back
        Button btnBack = findViewById(R.id.btnBack);
        if(btnBack != null) btnBack.setOnClickListener(v -> finish());

        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);
        RecyclerView rvShowtimes = findViewById(R.id.rvShowtimes);
        rvShowtimes.setLayoutManager(new LinearLayoutManager(this));

        Movie movie = (Movie) getIntent().getSerializableExtra("EXTRA_MOVIE");

        if (movie != null) {
            tvTitle.setText(movie.title);
            tvDesc.setText(movie.description);

            // XỬ LÝ DATABASE NGẦM
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getDatabase(this);

                List<Showtime> realShowtimes = db.showtimeDao().getShowtimesByMovie(movie.id);

                List<Theater> allTheaters = db.theaterDao().getAllTheaters();
                for (Showtime st : realShowtimes) {
                    for (Theater t : allTheaters) {
                        if (st.theaterId == t.id) {
                            st.theaterName = t.name;
                            break;
                        }
                    }
                }

                runOnUiThread(() -> {
                    rvShowtimes.setAdapter(new ShowtimeAdapter(realShowtimes));
                });
            });
        }
    }
}
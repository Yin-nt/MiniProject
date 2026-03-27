package com.example.miniproject2.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;
import com.example.miniproject2.adapters.ShowtimeAdapter;
import com.example.miniproject2.models.Movie;
import com.example.miniproject2.models.Showtime;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);
        RecyclerView rvShowtimes = findViewById(R.id.rvShowtimes);

        Movie movie = (Movie) getIntent().getSerializableExtra("EXTRA_MOVIE");

        if (movie != null) {
            tvTitle.setText(movie.getTitle());
            tvDesc.setText(movie.getDescription());

            List<Showtime> dummyShowtimes = new ArrayList<>();
            dummyShowtimes.add(new Showtime(101, movie.getId(), "CGV Vincom", "18:00"));
            dummyShowtimes.add(new Showtime(102, movie.getId(), "CGV Vincom", "20:30"));
            dummyShowtimes.add(new Showtime(103, movie.getId(), "Lotte Cinema", "19:15"));

            rvShowtimes.setLayoutManager(new LinearLayoutManager(this));
            rvShowtimes.setAdapter(new ShowtimeAdapter(dummyShowtimes));
        }
    }
}
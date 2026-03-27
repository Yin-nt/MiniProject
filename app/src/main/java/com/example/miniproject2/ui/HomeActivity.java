package com.example.miniproject2.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;
import com.example.miniproject2.adapters.MovieAdapter;
import com.example.miniproject2.data.AppDatabase;
import com.example.miniproject2.data.Movie;

import java.util.List;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Movie> realMovies = AppDatabase.getDatabase(this).movieDao().getAllMovies();

            runOnUiThread(() -> {
                rvMovies.setAdapter(new MovieAdapter(realMovies));
            });
        });
    }
}
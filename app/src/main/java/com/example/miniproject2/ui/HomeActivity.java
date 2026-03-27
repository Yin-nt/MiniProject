package com.example.miniproject2.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;
import com.example.miniproject2.adapters.MovieAdapter;
import com.example.miniproject2.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Đã xóa các code auto-gen thừa thãi

        List<Movie> dummyMovies = new ArrayList<>();
        dummyMovies.add(new Movie(1, "Dune: Part Two", "Hành trình khoa học viễn tưởng hoành tráng."));
        dummyMovies.add(new Movie(2, "Kung Fu Panda 4", "Gấu Po trở lại với chuyến phiêu lưu mới."));
        dummyMovies.add(new Movie(3, "Mai", "Bộ phim tâm lý tình cảm đạo diễn Trấn Thành."));

        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(new MovieAdapter(dummyMovies));
    }
}
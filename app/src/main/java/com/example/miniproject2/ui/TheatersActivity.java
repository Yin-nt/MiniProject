package com.example.miniproject2.ui;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;
import com.example.miniproject2.adapters.TheaterAdapter;
import com.example.miniproject2.data.AppDatabase;
import com.example.miniproject2.data.Theater;

import java.util.List;
import java.util.concurrent.Executors;

public class TheatersActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theaters);

        Button btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        RecyclerView rvTheaters = findViewById(R.id.rvTheaters);
        rvTheaters.setLayoutManager(new LinearLayoutManager(this));

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Theater> theaters = AppDatabase.getDatabase(this).theaterDao().getAllTheaters();
            runOnUiThread(() -> rvTheaters.setAdapter(new TheaterAdapter(theaters)));
        });
    }
}

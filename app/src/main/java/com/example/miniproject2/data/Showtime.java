package com.example.miniproject2.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "showtimes",
        foreignKeys = {
                @ForeignKey(entity = Movie.class, parentColumns = "id", childColumns = "movieId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Theater.class, parentColumns = "id", childColumns = "theaterId", onDelete = ForeignKey.CASCADE)
        })
public class Showtime {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int movieId;
    public int theaterId;
    public String time;

    @Ignore
    public String theaterName;

    @Ignore
    public String movieTitle;

    public Showtime(int movieId, int theaterId, String time) {
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.time = time;
    }
}

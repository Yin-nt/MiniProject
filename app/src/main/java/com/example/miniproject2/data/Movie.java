package com.example.miniproject2.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "movies")
public class Movie implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String description;
    public String posterUrl;

    public Movie(String title, String description, String posterUrl) {
        this.title = title;
        this.description = description;
        this.posterUrl = posterUrl;
    }
}
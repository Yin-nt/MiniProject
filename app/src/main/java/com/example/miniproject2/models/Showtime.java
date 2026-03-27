package com.example.miniproject2.models;

public class Showtime {
    private int id;
    private int movieId;
    private String theaterName;
    private String time;

    public Showtime(int id, int movieId, String theaterName, String time) {
        this.id = id;
        this.movieId = movieId;
        this.theaterName = theaterName;
        this.time = time;
    }

    public int getId() { return id; }
    public int getMovieId() { return movieId; }
    public String getTheaterName() { return theaterName; }
    public String getTime() { return time; }

    public void setId(int id) {
        this.id = id;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setTheaterName(String theaterName) {
        this.theaterName = theaterName;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
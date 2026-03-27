package com.example.miniproject2.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


import java.util.concurrent.Executors;

@Database(entities = {User.class, Movie.class, Theater.class, Showtime.class, Ticket.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract MovieDao movieDao();
    public abstract TheaterDao theaterDao();
    public abstract ShowtimeDao showtimeDao();
    public abstract TicketDao ticketDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "movie_ticket_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                UserDao userDao = INSTANCE.userDao();
                MovieDao movieDao = INSTANCE.movieDao();
                TheaterDao theaterDao = INSTANCE.theaterDao();
                ShowtimeDao showtimeDao = INSTANCE.showtimeDao();

                // Seed User
                userDao.insert(new User("admin", "admin", "System Administrator"));

                // Seed Movies with real TMDB image URLs
                long m1 = movieDao.insert(new Movie("Avengers: Endgame",
                        "After the devastating events of Infinity War, the universe is in ruins.",
                        "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/or06vS3STuS5jB0gpZ3oXpkiYvB.jpg"));

                long m2 = movieDao.insert(new Movie("Inception",
                        "A thief who steals corporate secrets through the use of dream-sharing technology.",
                        "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/9gk7Fn9sVAsS9699G1o3oH0mBqX.jpg"));

                long m3 = movieDao.insert(new Movie("The Dark Knight",
                        "Batman raises the stakes in his war on crime with the help of Lt. Jim Gordon and Harvey Dent.",
                        "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/qJ2tW6WMUDp9aqSCWpzkN6h9Euq.jpg"));

                // Seed Theaters (2 theaters)
                long t1 = theaterDao.insert(new Theater("CGV Vincom", "123 Ba Trieu, Hanoi"));
                long t2 = theaterDao.insert(new Theater("Lotte Cinema", "456 Tay Son, Hanoi"));

                // Seed Showtimes (4-5 showtimes)
                showtimeDao.insert(new Showtime((int) m1, (int) t1, "18:00"));
                showtimeDao.insert(new Showtime((int) m1, (int) t2, "20:30"));
                showtimeDao.insert(new Showtime((int) m2, (int) t1, "15:00"));
                showtimeDao.insert(new Showtime((int) m3, (int) t2, "21:00"));
                showtimeDao.insert(new Showtime((int) m2, (int) t2, "17:30"));
            });
        }
    };
}
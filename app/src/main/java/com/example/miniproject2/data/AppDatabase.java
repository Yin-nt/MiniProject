package com.example.miniproject2.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


import java.util.concurrent.Executors;

@Database(entities = {User.class, Movie.class, Theater.class, Showtime.class, Ticket.class}, version = 4, exportSchema = false)
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
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // Seed data here using the Context for drawable IDs
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        AppDatabase database = getDatabase(context);
                                        UserDao userDao = database.userDao();
                                        MovieDao movieDao = database.movieDao();
                                        TheaterDao theaterDao = database.theaterDao();
                                        ShowtimeDao showtimeDao = database.showtimeDao();

                                        // Seed User
                                        userDao.insert(new User("admin", "admin", "System Administrator"));

                                        // Use resource names or IDs. Since Room stores Strings, 
                                        // we'll store the name of the drawable resource.
                                        long m1 = movieDao.insert(new Movie("Avengers: Endgame", 
                                                "Sau các sự kiện thảm khốc trong Infinity War, vũ trụ đang bị hủy hoại.", 
                                                "avengers"));
                                        
                                        long m2 = movieDao.insert(new Movie("Inception", 
                                                "Một kẻ trộm đánh cắp bí mật thông qua việc xâm nhập vào giấc mơ.", 
                                                "inception"));
                                        
                                        long m3 = movieDao.insert(new Movie("The Dark Knight", 
                                                "Người dơi nâng mức đặt cược trong cuộc chiến chống tội phạm.", 
                                                "dark_knight"));

                                        // Seed Theaters
                                        long t1 = theaterDao.insert(new Theater("CGV Vincom", "123 Bà Triệu, Hà Nội"));
                                        long t2 = theaterDao.insert(new Theater("Lotte Cinema", "456 Tây Sơn, Hà Nội"));

                                        // Seed Showtimes
                                        showtimeDao.insert(new Showtime((int) m1, (int) t1, "18:00"));
                                        showtimeDao.insert(new Showtime((int) m1, (int) t2, "20:30"));
                                        showtimeDao.insert(new Showtime((int) m2, (int) t1, "15:00"));
                                        showtimeDao.insert(new Showtime((int) m3, (int) t2, "21:00"));
                                        showtimeDao.insert(new Showtime((int) m2, (int) t2, "17:30"));
                                    });
                                }
                            })
                            .addMigrations(new Migration(3, 4) {
                                @Override
                                public void migrate(@NonNull SupportSQLiteDatabase db) {
                                    // Step 1: Remove duplicate (showtimeId, seatNumber) rows, keep lowest id
                                    db.execSQL(
                                        "DELETE FROM tickets WHERE id NOT IN (" +
                                        "SELECT MIN(id) FROM tickets GROUP BY showtimeId, seatNumber" +
                                        ")"
                                    );
                                    // Step 2: Create new table with unique index
                                    db.execSQL(
                                        "CREATE TABLE tickets_new (" +
                                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                        "userId INTEGER NOT NULL, " +
                                        "showtimeId INTEGER NOT NULL, " +
                                        "seatNumber TEXT NOT NULL, " +
                                        "UNIQUE(showtimeId, seatNumber), " +
                                        "FOREIGN KEY(userId) REFERENCES `users`(`id`) ON DELETE CASCADE, " +
                                        "FOREIGN KEY(showtimeId) REFERENCES `showtimes`(`id`) ON DELETE CASCADE)"
                                    );
                                    // Step 3: Copy data
                                    db.execSQL(
                                        "INSERT INTO tickets_new (id, userId, showtimeId, seatNumber) " +
                                        "SELECT id, userId, showtimeId, seatNumber FROM tickets"
                                    );
                                    // Step 4: Drop old table
                                    db.execSQL("DROP TABLE tickets");
                                    // Step 5: Rename
                                    db.execSQL("ALTER TABLE tickets_new RENAME TO tickets");
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
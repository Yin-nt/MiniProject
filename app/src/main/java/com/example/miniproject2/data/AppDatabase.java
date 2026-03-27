package com.example.miniproject2.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Movie.class, Theater.class, Showtime.class, Ticket.class}, version = 2, exportSchema = false)
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
                            .fallbackToDestructiveMigration() // Tự động xóa và tạo lại DB khi tăng version
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

                // Seed Movies with very reliable Wikimedia/Direct URLs
                long m1 = movieDao.insert(new Movie("Avengers: Endgame", 
                        "Sau các sự kiện thảm khốc trong Infinity War, vũ trụ đang bị hủy hoại.", 
                        "https://static.wikia.nocookie.net/marvelcinematicuniverse/images/9/91/Endgame_Poster_2.jpg/revision/latest?cb=20190314215527"));
                
                long m2 = movieDao.insert(new Movie("Inception", 
                        "Một kẻ trộm đánh cắp bí mật thông qua việc xâm nhập vào giấc mơ.", 
                        "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_FMjpg_UX1000_.jpg"));
                
                long m3 = movieDao.insert(new Movie("The Dark Knight", 
                        "Người dơi nâng mức đặt cược trong cuộc chiến chống tội phạm.", 
                        "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_QL75_UX190_CR0,0,190,281_.jpg"));

                // Seed Theaters (2 theaters)
                long t1 = theaterDao.insert(new Theater("CGV Vincom", "123 Bà Triệu, Hà Nội"));
                long t2 = theaterDao.insert(new Theater("Lotte Cinema", "456 Tây Sơn, Hà Nội"));

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
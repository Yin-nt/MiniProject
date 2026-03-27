package com.example.miniproject2.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Category.class, Product.class, Order.class, OrderDetail.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract ProductDao productDao();
    public abstract OrderDao orderDao();
    public abstract OrderDetailDao orderDetailDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "shopping_database")
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
                CategoryDao categoryDao = INSTANCE.categoryDao();
                ProductDao productDao = INSTANCE.productDao();

                // Seed User
                userDao.insert(new User("admin", "admin", "System Administrator"));

                // Seed Categories
                long cat1 = categoryDao.insert(new Category("Electronics", "Gadgets and devices"));
                long cat2 = categoryDao.insert(new Category("Clothing", "Apparel and fashion"));
                long cat3 = categoryDao.insert(new Category("Home & Kitchen", "Appliances and furniture"));

                // Seed Products
                productDao.insert(new Product((int) cat1, "Smartphone", 999.99, "Latest flagship smartphone", "url_phone"));
                productDao.insert(new Product((int) cat1, "Laptop", 1299.50, "Powerful laptop for work", "url_laptop"));
                productDao.insert(new Product((int) cat2, "T-Shirt", 19.99, "Comfortable cotton t-shirt", "url_tshirt"));
                productDao.insert(new Product((int) cat2, "Jeans", 49.99, "Classic blue jeans", "url_jeans"));
                productDao.insert(new Product((int) cat3, "Coffee Maker", 89.00, "Brews delicious coffee", "url_coffee"));
                productDao.insert(new Product((int) cat3, "Toaster", 25.00, "2-slice toaster", "url_toaster"));
                productDao.insert(new Product((int) cat1, "Headphones", 150.00, "Noise-cancelling headphones", "url_headphones"));
                productDao.insert(new Product((int) cat2, "Jacket", 120.00, "Warm winter jacket", "url_jacket"));
            });
        }
    };
}
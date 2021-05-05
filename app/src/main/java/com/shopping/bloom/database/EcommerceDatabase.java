package com.shopping.bloom.database;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.shopping.bloom.App;
import com.shopping.bloom.database.dao.WishListProductDao;
import com.shopping.bloom.model.WishListItem;
import com.shopping.bloom.model.shoppingbag.ProductEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {WishListItem.class, ProductEntity.class}, version = 2, exportSchema = false)
public abstract class EcommerceDatabase extends RoomDatabase {

    private static String DATABASE_NAME = "ecommerse_databse";

    public abstract WishListProductDao wishListProductDao();

    private static volatile EcommerceDatabase DATABASE_INSTANCE = null;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static EcommerceDatabase getInstance() {
        synchronized (EcommerceDatabase.class) {
            if(DATABASE_INSTANCE == null) {
                DATABASE_INSTANCE = Room.databaseBuilder(App.getContext(),
                        EcommerceDatabase.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return DATABASE_INSTANCE;
    }

}

package com.ecoscan.app.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ecoscan.app.data.Pantry.PantryDao;
import com.ecoscan.app.data.Pantry.PantryItem;
import com.ecoscan.app.data.User.User;
import com.ecoscan.app.data.User.UserDao;

@Database(entities = {PantryItem.class, User.class}, version = 1, exportSchema = false)
public abstract class EcoScanDatabase extends RoomDatabase {

    private static EcoScanDatabase instance;

    public abstract PantryDao pantryDao();
    public abstract UserDao userDao();

    // Singleton — only one instance of the database exists
    public static synchronized EcoScanDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            EcoScanDatabase.class,
                            "ecoscan_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
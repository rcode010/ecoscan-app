package com.ecoscan.app.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PantryItem.class}, version = 1, exportSchema = false)
public abstract class EcoScanDatabase extends RoomDatabase {

    private static EcoScanDatabase instance;

    public abstract PantryDao pantryDao();

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
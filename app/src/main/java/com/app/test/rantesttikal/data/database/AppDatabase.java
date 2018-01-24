package com.app.test.rantesttikal.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.app.test.rantesttikal.data.dao.ProtocolDao;
import com.app.test.rantesttikal.data.model.Movie;

/**
 * Created by Ran on 12/19/2017.
 */

@Database(entities = {Movie.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract ProtocolDao protocolDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "movies-database")
                    // Note this will re-create the database with empty tables
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
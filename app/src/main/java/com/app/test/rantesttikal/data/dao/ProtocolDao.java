package com.app.test.rantesttikal.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.app.test.rantesttikal.data.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ran on 12/19/2017.
 */
@Dao
public interface ProtocolDao {

    /**
     * Insert a item in the database. If item already exists, replace it.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(List<Movie> movies);

    /**
     * Delete all movies.
     */
    @Query("DELETE FROM movies")
    abstract void deleteAll();

    /**
     * Get all movies.
     */
    @Query("SELECT * FROM movies")
    abstract List<Movie> getAllMovies();

}

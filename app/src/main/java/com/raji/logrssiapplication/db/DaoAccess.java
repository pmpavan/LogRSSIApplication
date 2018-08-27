package com.raji.logrssiapplication.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;


@Dao
public interface DaoAccess {

    @Insert
    void insert(Result movies);

    @Query("SELECT * FROM Result WHERE ssid = :movieId")
    LiveData<List<Result>> fetch(String movieId);

    @Query("SELECT * FROM Result")
    LiveData<List<Result>> fetchAll();

    @Query("SELECT * FROM Result")
    Single<List<Result>> fetch();

    @Query("delete from Result WHERE ssid = :movieId")
    void delete(String movieId);

    @Query("delete from Result")
    void deleteAll();
}

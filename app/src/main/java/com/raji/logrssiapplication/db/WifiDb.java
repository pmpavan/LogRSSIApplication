package com.raji.logrssiapplication.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Result.class, Distance.class}, version = 6, exportSchema = false)
public abstract class WifiDb extends RoomDatabase {
    public abstract DaoAccess daoAccess();

}

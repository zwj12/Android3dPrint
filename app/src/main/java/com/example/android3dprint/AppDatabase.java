package com.example.android3dprint;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * @author CNMIZHU7
 * @date 11/19/2019
 * description：
 */

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
}
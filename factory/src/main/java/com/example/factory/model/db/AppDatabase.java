package com.example.factory.model.db;

import com.dbflow5.annotation.Database;
import com.dbflow5.config.DBFlowDatabase;

/**
 * @author brsmsg
 * @time 2020/3/30
 */
@Database(version = AppDatabase.VERSION)
public abstract class AppDatabase extends DBFlowDatabase {
    public static final int VERSION = 1;
}

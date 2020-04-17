package com.example.factory.model.db;

import com.dbflow5.annotation.Database;
import com.dbflow5.config.DBFlowDatabase;

/**
 * @author brsmsg
 * @time 2020/4/16
 */
@Database(version = MyAppDB.VERSION)
public abstract class MyAppDB extends DBFlowDatabase {
    public static final int VERSION = 1;
}

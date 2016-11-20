package com.example.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Administrator on 11/20/2016.
 */

@Database(name = DBFlowDatabase.NAME, version = DBFlowDatabase.VERSION)
public class DBFlowDatabase {
    //数据库名称
    public static final String NAME = "DBFlowDatabase";
    //数据库版本号
    public static final int VERSION = 1;
}
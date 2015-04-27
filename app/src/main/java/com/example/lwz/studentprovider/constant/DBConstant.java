package com.example.lwz.studentprovider.constant;

/**
 * Created by lwz on 2015/4/27.
 */
public class DBConstant {
    public final static String DB_NAME = "student.db";
    public final static int DB_VERSION = 1;
    public final static String TABLE_NAME = "student";
    public final static String COLUMN_ID = "id";
    public final static String COLUMN_NAME = "name";
    public final static String COLUMN_ADDRESS = "address";
    public final static String COLUMN_SEX = "sex";
    public final static String CREATE_TABLE = "create table " + TABLE_NAME + "(" + COLUMN_ID + " integer primary key autoincrement," +
            COLUMN_NAME + " varchar(64)," + COLUMN_ADDRESS + " varchar(64)," + COLUMN_SEX + " varchar(4)" + ")";
}

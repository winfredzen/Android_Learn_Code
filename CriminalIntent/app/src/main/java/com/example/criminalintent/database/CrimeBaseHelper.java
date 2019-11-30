package com.example.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class CrimeBaseHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public CrimeBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库 表
        db.execSQL("create table " + CrimeDbSchema.CrimeTable.NAME +
                "(" + " _id integer primary key autoincrement, "
                + CrimeDbSchema.Cols.UUID + ", "
                + CrimeDbSchema.Cols.TITLE + ", "
                + CrimeDbSchema.Cols.DATE + ", "
                + CrimeDbSchema.Cols.SOLVED + ", "
                + CrimeDbSchema.Cols.SUSPECT
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

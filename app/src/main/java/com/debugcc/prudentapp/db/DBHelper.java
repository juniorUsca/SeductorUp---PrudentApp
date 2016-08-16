package com.debugcc.prudentapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dubgcc on 27/11/15.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "PrudentApp.db";
    public static final int DATABASE_VERSION = 10;
    private static final String DATABASE_CREATE = "create table Periode_PrudentApp( _id INTEGER primary key AUTOINCREMENT,start_date INTEGER not null,end_date INTEGER not null,ini_pos INTEGER not null,end_pos INTEGER not null);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS Periode_PrudentApp");
        onCreate(db);
    }
}

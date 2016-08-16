package com.debugcc.prudentapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by dubgcc on 27/11/15.
 */
public class PrudentAppDB {

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public final static String PER_TABLE = "Periode_PrudentApp"; // name of table

    public final static String PER_ID = "_id"; // id value for employee
    public final static String PER_STARTDATE = "start_date";  // name of employee
    public final static String PER_ENDDATE = "end_date";  // name of employee
    public final static String PER_INIPOS = "ini_pos";  // name of employee
    public final static String PER_ENDPOS = "end_pos";  // name of employee

    public PrudentAppDB(Context context) {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long createRecord(long start_date, long end_date, int ini_pos, int end_pos){
        ContentValues values = new ContentValues();
        values.put(PER_STARTDATE, start_date);
        values.put(PER_ENDDATE, end_date);
        values.put(PER_INIPOS, ini_pos);
        values.put(PER_ENDPOS, end_pos);
        Log.e("DB", "INSERTANDO ");
        return db.insert(PER_TABLE, null, values);
        //Log.e("insertando", "insert into "+PER_TABLE+"(ini,fin,pini,pfin) VALUES(\"" +ini.toString()+ "\" ,\"" +fin.toString()+ "\"," +pini+","+pfin+ ")" );
        //db.execSQL("insert into "+PER_TABLE+"(ini,fin,pini,pfin) VALUES(\"" +ini.toString()+ "\" ,\"" +fin.toString()+ "\"," +pini+","+pfin+ ")");
    }

    public Cursor selectRecords() {
        String[] cols = new String[] {PER_ID, PER_STARTDATE, PER_ENDDATE, PER_INIPOS, PER_ENDPOS};
        Cursor mCursor = db.query(true, PER_TABLE, cols, null, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor selectRecords(long start, long end) {
        //String[] cols = new String[] {PER_ID, PER_INICIO, PER_FIN};
        Log.e("consultando", "select * from " + PER_TABLE + " where start_date BETWEEN \"" +start+ "\" AND \"" + end + "\" " );
        Cursor mCursor = db.rawQuery("select * from " + PER_TABLE + " where start_date BETWEEN \"" + start + "\" AND \"" + end + "\" ", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            Log.e("DB", "se encontraron " + mCursor.getCount() + " filas");
        }
        return mCursor;
    }

    public boolean deleteRecords(int id) {
        Log.e("DB", "deleteRecords: eLIMINADNDO!!" );
        return db.delete(PER_TABLE, PER_ID + "=" + id, null) > 0;
    }
}

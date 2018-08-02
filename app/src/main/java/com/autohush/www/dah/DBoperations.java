package com.autohush.www.dah;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by PKBEST on 17-03-2016.
 */
public class DBoperations extends SQLiteOpenHelper{

    //pSQLiteDatabase db;
    public static final int db_version = 1;

    public String Create_Query = "CREATE TABLE "+DBAdapter.DBinfo.Tablename+"("+ DBAdapter.DBinfo.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DBAdapter.DBinfo.Name+" TEXT,"+ DBAdapter.DBinfo.Latitude+" TEXT,"+ DBAdapter.DBinfo.Longitude+" TEXT,"+ DBAdapter.DBinfo.Profile+" TEXT);";

    public String Create_Time_Query = "CREATE TABLE "+DBAdapter.DBtimeinfo.Tablename+"("+ DBAdapter.DBtimeinfo.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DBAdapter.DBtimeinfo.StartTimeHour+" INTEGER,"+ DBAdapter.DBtimeinfo.StartTimeMin+" INTEGER,"+ DBAdapter.DBtimeinfo.EndTimeHour+" INTEGER,"
            + DBAdapter.DBtimeinfo.EndTimeMin+" INTEGER,"+ DBAdapter.DBtimeinfo.Profile+" TEXT);";

    public DBoperations(Context context) {
        super(context, DBAdapter.DBinfo.DBname, null, db_version);
        Log.d("DataBase operations", "DataBase created Successfully");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_Time_Query);
        db.execSQL(Create_Query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void puttimeInfo(DBoperations dop,int sth,int stm,int eth,int etm,String pro){
        SQLiteDatabase sq = dop.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBAdapter.DBtimeinfo.StartTimeHour, sth);
        cv.put(DBAdapter.DBtimeinfo.StartTimeMin, stm);
        cv.put(DBAdapter.DBtimeinfo.EndTimeHour, eth);
        cv.put(DBAdapter.DBtimeinfo.EndTimeMin, etm);
        cv.put(DBAdapter.DBtimeinfo.Profile, pro);
        long k = sq.insert(DBAdapter.DBtimeinfo.Tablename,null,cv);         //if not in cv insert null.
        Log.d("DataBase operations", "Time Row Inserted");
    }

    public void putInfo(DBoperations dop,String name,String lat,String lon,String pro){
        SQLiteDatabase sq = dop.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBAdapter.DBinfo.Name, name);
        cv.put(DBAdapter.DBinfo.Latitude, lat);
        cv.put(DBAdapter.DBinfo.Longitude, lon);
        cv.put(DBAdapter.DBinfo.Profile, pro);
        long k = sq.insert(DBAdapter.DBinfo.Tablename,null,cv);         //if not in cv insert null.
        Log.d("DataBase operations", "Location Row Inserted");
    }

    public void delInfo(DBoperations dop,String id){
        SQLiteDatabase sq = dop.getReadableDatabase();
        sq.delete(DBAdapter.DBinfo.Tablename, "id = ?", new String[]{id});
    }

    public void deltimeInfo(DBoperations dop,String id){
        SQLiteDatabase sq = dop.getReadableDatabase();
        sq.delete(DBAdapter.DBtimeinfo.Tablename,"id = ?",new String[] {id});
    }

    public Cursor gettimeInfo(DBoperations dop){
        SQLiteDatabase sq = dop.getReadableDatabase();
        String[] columns = {DBAdapter.DBtimeinfo.ID,DBAdapter.DBtimeinfo.StartTimeHour,DBAdapter.DBtimeinfo.StartTimeMin, DBAdapter.DBtimeinfo.EndTimeHour, DBAdapter.DBtimeinfo.EndTimeMin, DBAdapter.DBtimeinfo.Profile};
        Cursor cr = sq.query(DBAdapter.DBtimeinfo.Tablename, columns, null, null, null, null, null);
        return cr;
    }

    public Cursor getInfo(DBoperations dop){
        SQLiteDatabase sq = dop.getReadableDatabase();
        String[] columns = {DBAdapter.DBinfo.ID,DBAdapter.DBinfo.Name,DBAdapter.DBinfo.Latitude, DBAdapter.DBinfo.Longitude, DBAdapter.DBinfo.Profile};
        Cursor cr = sq.query(DBAdapter.DBinfo.Tablename, columns, null, null, null, null, null);
        return cr;
    }

    public void updateInfo(DBoperations dop,String id,String lat,String lon,String pro){
        SQLiteDatabase sq = dop.getReadableDatabase();
        ContentValues cv = new ContentValues();
        if(!lat.equals("")) {
            cv.put(DBAdapter.DBinfo.Latitude, lat);
            cv.put(DBAdapter.DBinfo.Longitude, lon);
        }
        if(!pro.equals(""))
            cv.put(DBAdapter.DBinfo.Profile, pro);
        sq.update(DBAdapter.DBinfo.Tablename,cv,"id = ?",new String[] {id});
    }
}

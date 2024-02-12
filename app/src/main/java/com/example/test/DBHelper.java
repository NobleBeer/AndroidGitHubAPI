package com.example.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SearchHistory.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "history";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_QUERY = "query";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_QUERY + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean saveQuery(String query) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUERY, query);
        long newRowId = db.insert(TABLE_NAME, null, values);
        return newRowId != -1;
    }

    public List<String> getHistory() {
        List<String> historyList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String query = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUERY));
                historyList.add(query);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return historyList;
    }
}

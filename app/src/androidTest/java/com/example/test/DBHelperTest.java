package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class DBHelperTest {

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    @Before
    public void setUp() {
        dbHelper = new DBHelper(ApplicationProvider.getApplicationContext());
        db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_NAME, null, null); // Удаление всех строк из таблицы
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void testSaveQuery() {
        String query = "Test Query";

        dbHelper.saveQuery(query);

        Cursor cursor = db.query(DBHelper.TABLE_NAME, new String[]{DBHelper.COLUMN_QUERY},
                null, null, null, null, null);
        assertNotNull(cursor);
        assertTrue(cursor.moveToFirst());
        assertEquals(query, cursor.getString(cursor.getColumnIndex(DBHelper
                .COLUMN_QUERY)));
        cursor.close();
    }

    @Test
    public void testGetHistory() {
        String query1 = "Query 1";
        String query2 = "Query 2";

        dbHelper.saveQuery(query1);
        dbHelper.saveQuery(query2);

        List<String> historyList = dbHelper.getHistory();
        assertNotNull(historyList);
        assertFalse(historyList.isEmpty());
        assertEquals(2, historyList.size());
        assertTrue(historyList.contains(query1));
        assertTrue(historyList.contains(query2));
    }
}

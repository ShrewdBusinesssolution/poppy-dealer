package com.shrewd.poppydealers.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shrewd.poppydealers.model.Order;

import java.util.ArrayList;
import java.util.List;

public class AndroidSQLiteData extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "orderData";
    // Labels table name
    public static final String TABLE_NAME = "orders";
    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_ORDER_ID = "product_id";
    public static final String KEY_STATUS = "product_status";
    public static final String KEY_QUANTITY = "product_image";
    public static final String KEY_DATE = "product_date";
    public static final String KEY_AMOUNT = "product_rate";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db = null;

    public AndroidSQLiteData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ORDER_ID + " TEXT," + KEY_STATUS + " TEXT," + KEY_DATE + " TEXT," + KEY_QUANTITY + " TEXT," + KEY_AMOUNT + " TEXT)";
        sdb.execSQL(CREATE_CATEGORIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void deleteAll() {
        db.execSQL("delete from " + TABLE_NAME);
    }

    public void insertData(String id, String status, String date, String quantity, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ORDER_ID, id);
        values.put(KEY_STATUS, status);
        values.put(KEY_DATE, date);
        values.put(KEY_QUANTITY, quantity);
        values.put(KEY_AMOUNT, amount);


        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close();
        // Closing database connection

    }

    public List<Order> getAllData(String startdate, String enddate) {
        List<Order> data = new ArrayList<Order>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orders WHERE product_date BETWEEN ?  AND ?", new String[]{startdate, enddate});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Log.d("TAG", "Data:::" + cursor.getString(1) + ", " + cursor.getString(2) + ", " + cursor.getString(3) + ", " + cursor.getString(4) + ", " + cursor.getString(5));

                data.add(new Order(cursor.getString(1), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(2)));
            }

            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return data;
    }

    public void open() {
        if (this.db == null) {
            this.db = this.getWritableDatabase();
        }
    }
}

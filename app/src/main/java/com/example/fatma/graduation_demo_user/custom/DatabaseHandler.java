package com.example.fatma.graduation_demo_user.custom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.fatma.graduation_demo_user.Models.promo_code;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "notifications";

    // Labels table name
    private static final String TABLE_LABELS = "promos";

    // Labels Table Columns names

    private static final String KEY_ID = "id";
    private static final String KEY_promo = "promo";
    private static final String KEY_value = "value";
    private static final String KEY_text= "message_text";
    private static final String KEY_time = "time";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_LABELS + "("
                + KEY_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT ,"
                + KEY_promo + " TEXT ,"
                + KEY_text + " TEXT ,"
                + KEY_value + " INTEGER ,"
                + KEY_time + " INTEGER )";
        db.execSQL(CREATE_CATEGORIES_TABLE);




    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Inserting new lable into lables table
     * */

    public void insertphone_number(String promo,String text,int value,long time){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_promo, promo);
        values.put(KEY_text, text);
        values.put(KEY_value, value);
        values.put(KEY_time, time);

        // Inserting Row
        db.insert(TABLE_LABELS, null, values);
        db.close(); // Closing database connection

    }

    /**
     * Getting all labels
     * returns list of labels
     * */
    public List<promo_code> getAllLabels(){
        List<promo_code> labels = new ArrayList<promo_code>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LABELS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                promo_code model=new promo_code(cursor.getInt(0),cursor.getString(1)
                ,cursor.getInt(2),cursor.getString(3),cursor.getLong(4));
                labels.add(model);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

}
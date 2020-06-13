package com.example.main2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBhelper  extends SQLiteOpenHelper{
    final String LOG_TAG = "myLogs";


    public DBhelper(Context context) {
        super(context, "myDb", null, 1);
    }

    @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Log.d(LOG_TAG, "---onCreate database---");

            ContentValues contentValues = new ContentValues();

            sqLiteDatabase.execSQL("create table subject ("
                    + "id integer primary key, "
                    + "name text "
                    + ");");


            contentValues = new ContentValues();
            sqLiteDatabase.execSQL("create table DateStudent ("
                    + "id integer primary key autoincrement, "
                    + "subject integer, "
                    + "student integer, "
                    + "date integer,"
                    + "presence numeric"
                    + ");");

            contentValues = new ContentValues();
            sqLiteDatabase.execSQL("create table date ("
                    + "id integer primary key autoincrement, "
                    + "date numeric "
                    + ");");



            contentValues = new ContentValues();
            sqLiteDatabase.execSQL("create table student ("
                    + "id integer primary key autoincrement, "
                    + "name text"
                    + ");");


        }


     String[] logCursor(Cursor cursor, boolean mode) {
        String[] masString = new String[1];
         int n = 0;
         int k = 0;
         if(mode) k = 1;
         else k = 2;
        if (cursor != null) {

            if (cursor.moveToFirst()) {
                String str;

                do {

                    String[] dopmasString = new String[n+k];
                    for (int i = 0; i < n; i++) {
                        dopmasString[i] =  masString[i];
                    }
                    masString = new String[n+k];
                    for (int i = 0; i < n; i++) {
                        masString[i] =  dopmasString[i];
                    }
                    str = "";
                    for (String cn : cursor.getColumnNames()) {
                        str = str.concat(cursor.getString(cursor.getColumnIndex(cn)));
                        masString[n] = str;
                        n++;
                        str = "";
                    }


                } while (cursor.moveToNext());

            }

        } else Log.d(LOG_TAG, "Cursor is null");
         for (int i = 0; i < n; i++) {
             Log.d(LOG_TAG, masString[i]);
         }
        return masString;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists contacts");
        onCreate(db);

    }
}

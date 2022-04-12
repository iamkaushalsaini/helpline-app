package com.hlit.helplinetelecom;





import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHandler extends SQLiteOpenHelper {
    Context applicationContext;
    private  static final String dbname = "cart.db";


    public DbHandler(@Nullable Context context) {
        super(context, dbname, null,  1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        // String qry ="create table cart (id integer primary key,  name text, price REAL, updatePrice REAL, mrp text, description text, image BLOB, cusId text, qty text, status text)";
        String qry ="create table location (id integer primary key AUTOINCREMENT ,user_id integer,  date text, time text, month text, month_year text, year text, time_stamp text, location text, latitude text, longitude text, day text, note text)";
        db.execSQL(qry);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS location");
        onCreate(db);



    }


    public String addRecord( int user_id,  String date, String time, String month, String month_year, String year, String time_stamp, String location, String latitude, String longitude, String day, String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id",user_id);
        contentValues.put("date",date);
        contentValues.put("time",time);
        contentValues.put("month",month);
        contentValues.put("month_year",month_year);
        contentValues.put("year",year);
        contentValues.put("time_stamp",time_stamp);
        contentValues.put("location",location);
        contentValues.put("latitude",latitude);
        contentValues.put("longitude",longitude);
        contentValues.put("day",day);
        contentValues.put("note",note);
        // contentValues.put("rating",p5);

        long res= db.insert("location",null, contentValues);

        if(res==-1)
            return "failed";
        else
            return "successfully inserted";

    }




    public Cursor readAllData(String cusId){

        User user = SharedPrefManager.getInstance(applicationContext).getUser();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select date, time, month, month_year, year, time_stamp, location, latitude, longitude, day, note from location  where user_id = ?",new String[]{cusId});
        return cursor;
    }


    public int deleteCartAll() {
        User user = SharedPrefManager.getInstance(applicationContext).getUser();
        String cusId = String.valueOf(user.getId());
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete("location","user_id =?",new String[]{cusId});

    }



}



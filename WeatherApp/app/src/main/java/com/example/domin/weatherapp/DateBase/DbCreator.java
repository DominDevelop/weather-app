package com.example.domin.weatherapp.DateBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.util.Log;

import com.example.domin.weatherapp.DateBase.Model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik Ratajczak on 17.06.2016.
 * <p>
 * Klasa odpowiadająca za inicjację bazy danych
 */
public class DbCreator extends SQLiteOpenHelper {

    private String TAG = DbCreator.class.getName();
    private Context context;
    public static final String DB_NAME = "Weather.db";

    public static final String CITY_TABLE = "Cities";
    public static final String CITY_ID = "id";
    public static final String CITY_NAME = "name";
    public static final String CITY_LONG = "longitude";
    public static final String CITY_LATI = "latitude";
    public static final String CITY_AVERAGE_TEMP_C = "averageTempC";
    public static final String CITY_AVERAGE_TEMP_F = "averageTempF";
    public static final String CITY_DAY = "day";

    private final String queryCities = "CREATE TABLE IF NOT EXISTS " + CITY_TABLE +
            "( " + CITY_ID + " integer primary key autoincrement, "
            + CITY_NAME + " text, "
            + CITY_LONG + " real, "
            + CITY_LATI + " real, "
            + CITY_AVERAGE_TEMP_C + " real, "
            + CITY_AVERAGE_TEMP_F + " real, "
            + CITY_DAY + " text )";


    public DbCreator(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(queryCities);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + CITY_TABLE);
        db.execSQL(queryCities);
    }

    public long addCities(Address address, String city, String date) {

        ContentValues row = new ContentValues();
        row.put(CITY_LATI, address.getLatitude());
        row.put(CITY_LONG, address.getLongitude());
        row.put(CITY_AVERAGE_TEMP_C, 1.0d);
        row.put(CITY_AVERAGE_TEMP_F, 1.0d);
        row.put(CITY_NAME, city);
        row.put(CITY_DAY, date);
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;

        try {
            id = db.insert(CITY_TABLE, null, row);
            if (id == -1) {
                Log.e(TAG, "nie udany zapis do bazy");
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "nie uzyskano uchwytu do bazy " + e.getMessage());
        }
        db.close();
        return id;
    }

    public void updateCity(String date,double temp){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put(CITY_AVERAGE_TEMP_C,((temp - 32) * 5/9));
        row.put(CITY_AVERAGE_TEMP_F,temp);
        db.update(CITY_TABLE,row,CITY_DAY + "=?",new String[]{date});
        db.close();
    }

    public ArrayList<City> getAllCities() {

        ArrayList<City> list = new ArrayList<City>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CITY_TABLE, null);

        int id = c.getColumnIndex(CITY_ID);
        int name = c.getColumnIndex(CITY_NAME);
        int ce = c.getColumnIndex(CITY_AVERAGE_TEMP_C);
        int f = c.getColumnIndex(CITY_AVERAGE_TEMP_F);
        int lat = c.getColumnIndex(CITY_LATI);
        int lon = c.getColumnIndex(CITY_LONG);
        int day = c.getColumnIndex(CITY_DAY);

        while (c.moveToNext()) {

            City city = new City(
                    c.getInt(id),
                    c.getString(name),
                    c.getDouble(lat),
                    c.getDouble(lon),
                    c.getDouble(ce),
                    c.getDouble(f),
                    c.getString(day)
            );
            list.add(city);
        //    Log.e(TAG, city.getName());
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<City> getAllDetailCity(String name){

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CITY_TABLE +
                " WHERE " + CITY_NAME + "=?",new String[]{name});

        int id = c.getColumnIndex(CITY_ID);
        int cName = c.getColumnIndex(CITY_NAME);
        int ce = c.getColumnIndex(CITY_AVERAGE_TEMP_C);
        int f = c.getColumnIndex(CITY_AVERAGE_TEMP_F);
        int lat = c.getColumnIndex(CITY_LATI);
        int lon = c.getColumnIndex(CITY_LONG);
        int day = c.getColumnIndex(CITY_DAY);

        ArrayList<City> list = new ArrayList<>();
        while (c.moveToNext()){

            City city = new City(c.getInt(id),
                    c.getString(cName),
                    c.getDouble(lat),
                    c.getDouble(lon),
                    c.getDouble(ce),
                    c.getDouble(f),
                    c.getString(day));
            list.add(city);
        //    Log.e(TAG,city.getName() + " : " + city.getAverageF() + " : " + city.getLat());
        }

        c.close();
        db.close();
        return list;
    }

    public void delete(String s) {

        SQLiteDatabase db = getWritableDatabase();
        db.delete(CITY_TABLE, CITY_NAME + "=?",new String[]{s});
        db.close();
    }
}

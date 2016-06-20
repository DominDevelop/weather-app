package com.example.domin.weatherapp;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.domin.weatherapp.DateBase.DbCreator;
import com.example.domin.weatherapp.Utilities.PagerAdapter;

public class MainActivity extends AppCompatActivity {

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        /*
        getApplicationContext().deleteDatabase(DbCreator.DB_NAME);
        */
    }
}

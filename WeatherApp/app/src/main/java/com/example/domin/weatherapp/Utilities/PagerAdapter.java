package com.example.domin.weatherapp.Utilities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.domin.weatherapp.Fragments.FirstFragment;
import com.example.domin.weatherapp.Fragments.SecondFragment;

/**
 * Created by Dominik Ratajczak on 17.06.2016.
 *
 */
public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return  new FirstFragment();
            case 1:
                return new SecondFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

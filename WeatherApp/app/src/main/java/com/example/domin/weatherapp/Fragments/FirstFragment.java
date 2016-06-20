package com.example.domin.weatherapp.Fragments;

import android.database.sqlite.SQLiteException;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.domin.weatherapp.DateBase.DbCreator;
import com.example.domin.weatherapp.DateBase.Model.City;
import com.example.domin.weatherapp.MainActivity;
import com.example.domin.weatherapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.example.domin.weatherapp.RestService.JsonObjects.DataBlock;
import com.example.domin.weatherapp.RestService.JsonObjects.DataPoint;
import com.example.domin.weatherapp.RestService.JsonObjects.WeatherResponse;
import com.example.domin.weatherapp.RestService.RestUri;
import com.example.domin.weatherapp.RestService.WebService;
import com.example.domin.weatherapp.Utilities.ExpandableList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Dominik Ratajczak on 17.06.2016.
 */
public class FirstFragment extends Fragment {

    private final String TAG = FirstFragment.class.getName();
    private EditText editText;
    private ImageButton btnAdd;
    private ExpandableListView list;
    public static HashMap<String, ArrayList<City>> listDataChild;
    public static HashSet<String> listDataHeader;
    private RestAdapter retrofit;
    private WebService webService;
    public static Stack<String> dateStac;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = (View) inflater.inflate(R.layout.fragment_first, container, false);
        editText = (EditText) view.findViewById(R.id.inputCity);
        btnAdd = (ImageButton) view.findViewById(R.id.btnAdd);
        list = (ExpandableListView) view.findViewById(R.id.listId);
        listDataHeader = new HashSet<String>();
        listDataChild = new HashMap<String, ArrayList<City>>();
        dateStac = new Stack<String>();

        DbCreator db = new DbCreator(getContext());
        ArrayList<City>cl = db.getAllCities();
        for (City c : cl){
            listDataHeader.add(c.getName());
            listDataChild.put(c.getName(), db.getAllDetailCity(c.getName()));
        }
        list.setAdapter(new ExpandableList(getContext(), listDataHeader, listDataChild,list));
        db.close();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String city = editText.getText().toString();
                Address adr;
                Geocoder coder = new Geocoder(getContext());
                List<Address> address;

                try {
                    address = coder.getFromLocationName(city, 1);
                    DbCreator dbCreator = new DbCreator(getContext());
                    adr = address.get(0);
                    ArrayList<City> cities = dbCreator.getAllCities();
                    dbCreator.close();
                    for (City c : cities) {
                        if (c.getName().equals(city)) {
                            Toast.makeText(getContext(), "Wprowadzono już to miasto", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Log.e(TAG, adr.getCountryName() + " " + adr.getLongitude() + " : " + adr.getLatitude());

                    for (int i = 0; i >= -2; i--) {

                        dbCreator.addCities(adr, city, getDate(i));
                        dateStac.push(getDate(i));
                        retrofit = new RestAdapter.Builder()
                                .setEndpoint(RestUri.buildUri(adr.getLatitude(), adr.getLongitude(), getYesterdayDateString(i)))
                                .setLogLevel(RestAdapter.LogLevel.FULL)
                                .build();
                        webService = retrofit.create(WebService.class);
                        webService.postWeatherDate(new Callback<WeatherResponse>() {
                            @Override
                            public void success(WeatherResponse weatherResponse, Response response) {

                                Log.e(TAG, "status := " + response.getStatus());
                                DataBlock hourlyBlock = weatherResponse.getHourly();

                                double averageTemp = 0;
                                for (DataPoint point : hourlyBlock.getData()) {
                                    averageTemp += point.getTemperature();
                                }

                                DbCreator dbCreator = new DbCreator(MainActivity.context);
                                dbCreator.updateCity(dateStac.pop(),averageTemp/24);
                                dbCreator.close();

                                listDataChild.put(city, dbCreator.getAllDetailCity(city));
                                listDataHeader.add(editText.getText().toString());
                                list.setAdapter(new ExpandableList(getContext(), listDataHeader, listDataChild,list));

                            }

                            @Override
                            public void failure(RetrofitError error) {

                                Log.e(TAG, error.getMessage());
                            }
                        });
                        dbCreator.close();
                    }
                } catch (SQLiteException e) {

                    Log.e(TAG, e.getMessage() + e.getLocalizedMessage());
                    return;
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage() + e.getLocalizedMessage());
                    Toast.makeText(getContext(), "Źle wprowadzone miasto", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        return view;
    }

    private String getDate(int days) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        String dateStr = dateFormat.format(cal.getTime());
        return dateStr;
    }

    private String getYesterdayDateString(int dayToSub) throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, dayToSub);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR, 0);

        String str = dateFormat.format(cal.getTime());
        Date date = dateFormat.parse(str);

        SimpleDateFormat sdf = new SimpleDateFormat("Z");
        String result = String.format("%1$04d-", cal.get(Calendar.YEAR));
        result += String.format("%1$02d-", cal.get(Calendar.MONTH));
        result += String.format("%1$02dT00:00:00", cal.get(Calendar.DAY_OF_MONTH));
        result += sdf.format(date);
        //    Log.e(TAG, "string do wysłania " + result);
        return result;
    }
}

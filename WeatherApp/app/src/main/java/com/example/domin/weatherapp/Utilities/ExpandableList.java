package com.example.domin.weatherapp.Utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Address;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.domin.weatherapp.DateBase.DbCreator;
import com.example.domin.weatherapp.DateBase.Model.City;
import com.example.domin.weatherapp.Fragments.FirstFragment;
import com.example.domin.weatherapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Dominik Ratajczak on 17.06.2016.
 * <p/>
 * Klasa odpowiadająca tworzenie "listy rozszeszającej" expandablelist
 * zawiera dodane miasta
 */
public class ExpandableList extends BaseExpandableListAdapter {

    private Context context;
    private List<String> headerList;
    private HashMap<String, ArrayList<City>> itemList;
    private ExpandableListView listView;

    public ExpandableList(Context context, Set<String> listDataHeader,
                          HashMap<String, ArrayList<City>> listChildData, ExpandableListView list) {
        this.context = context;
        this.headerList = new ArrayList<String>(listDataHeader);
        this.itemList = listChildData;
        this.listView = list;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        return this.itemList.get(this.headerList.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_layout, null);
        }

        TextView dayView = (TextView) convertView.findViewById(R.id.dayText);
        dayView.setText(itemList.get(headerList.get(groupPosition)).get(childPosition).getDay());

        TextView cView = (TextView) convertView.findViewById(R.id.outC);
        double d = itemList.get(headerList.get(groupPosition)).get(childPosition).getAverageC();
        cView.setText(String.format("%.2f", d));

        TextView fView = (TextView) convertView.findViewById(R.id.outF);
        d = itemList.get(headerList.get(groupPosition)).get(childPosition).getAverageF();
        fView.setText(String.format("%.2f", d));
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.itemList.get(this.headerList.get(groupPosition)).size();
    }

    /*
    GRUPA
     */

    @Override
    public Object getGroup(int groupPosition) {
        return this.headerList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.headerList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_layout, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.header);
        textView.setText(headerList.get(groupPosition));
        textView.setTypeface(null, Typeface.BOLD);

        ImageButton btn = (ImageButton) convertView.findViewById(R.id.btnDelete);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DbCreator db = new DbCreator(context);
                db.delete(headerList.get(groupPosition));

                if (FirstFragment.listDataChild.remove(headerList.get(groupPosition)) != null) {
                    Log.e("usunieto", "usunieto dzieci");
                }
                if (FirstFragment.listDataHeader.remove(headerList.get(groupPosition))) {
                    Log.e("usunieto", "usunieto nagłówek");
                }
                itemList.remove(headerList.get(groupPosition));
                headerList.remove(groupPosition);
                db.close();
                Set<String> set = new HashSet<String>(headerList);
                listView.setAdapter(new ExpandableList(context, set, itemList, listView));

            }
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

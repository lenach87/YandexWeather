package com.example.myapplication2.app.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.example.myapplication2.app.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class ExpListViewAdapter <Country, City> extends BaseExpandableListAdapter {

    private Context context;
    private LinkedHashMap<Country, ArrayList<City>> countriesCollections;
    private ArrayList<Country> countries;

    public ExpListViewAdapter(Context context, ArrayList<Country> countries,
                              LinkedHashMap<Country, ArrayList<City>> countriesCollections) {
        this.context = context;
        this.countries = countries;
        this.countriesCollections = countriesCollections;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return countriesCollections.get(countries.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        City city = (City) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_item, parent, false);
        }
        TextView item = (TextView) convertView.findViewById(R.id.child_txt);
        item.setText(city.toString());
        return convertView;

    }

    public int getChildrenCount(int groupPosition) {
        return countriesCollections.get(countries.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return countries.get(groupPosition);
    }

    public int getGroupCount() {
        return countries.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Country country = (Country) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_item, parent, false);
        }
        TextView item = (TextView) convertView.findViewById(R.id.parent_txt);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(country.toString());
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
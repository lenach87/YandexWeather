package com.example.myapplication2.app;

import java.util.*;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by lena on 1/20/16.
 */
public class ExpListViewAdapter <Country, City> extends BaseExpandableListAdapter {

    private Activity context;
    private Map<Country, ArrayList<City>> countriesCollections;
    private ArrayList<Country> countries;

    public ExpListViewAdapter(Activity context, ArrayList<Country> countries,
                              Map<Country, ArrayList<City>> countriesCollections) {
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
        City city = getCity(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.cities);
        item.setText(city.toString());
        return convertView;

    }

    public int getChildrenCount(int groupPosition) {
        return countriesCollections.get(countries.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return countries.get(groupPosition);
    }

    public Country getCountry (int groupPosition) {
        return (Country) getGroup(groupPosition);
    }
    public City getCity (int groupPosition, int childPosition) {
        return (City) getChild(groupPosition,childPosition);
    }

    public int getGroupCount() {
        return countries.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Country country = getCountry(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.cities);
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
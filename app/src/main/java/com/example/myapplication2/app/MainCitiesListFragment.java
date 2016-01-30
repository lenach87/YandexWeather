package com.example.myapplication2.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class MainCitiesListFragment extends Fragment implements ExpandableListView.OnChildClickListener {

    private ExpandableListView expListView;
    private ExpListViewAdapter expListAdapter;
    private LinkedHashMap<Country, ArrayList<City>> countryCollection;
    private ArrayList<Country> countriesList;
    private ArrayList<City> citiesList;
    ExpandableListView.OnChildClickListener citySelectedCallback;

    public MainCitiesListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_cities_list, null);
        expListView = (ExpandableListView) view.findViewById(R.id.exListView);
        countryCollection = WeatherXmlPullParser.getCountriesAndCitiesList(MainCitiesListFragment.this.getActivity());
        countriesList = new ArrayList<Country>(countryCollection.keySet());
        expListAdapter = new ExpListViewAdapter<Country, City>(MainCitiesListFragment.this.getActivity(), countriesList, countryCollection);
        expListView.setAdapter(expListAdapter);
        createGroupList();
        createCollection();



        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            public void onGroupExpand(int groupPosition) {
                Log.d("Logformain", "onGroupExpand groupPosition = " + groupPosition);
                Toast.makeText(MainCitiesListFragment.this.getActivity(), countriesList.get(groupPosition) + "is expanded", Toast.LENGTH_LONG).show();
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            public void onGroupCollapse(int groupPosition) {
                Log.d("Logformain", "onGroupCollapse groupPosition = " + groupPosition);
                Toast.makeText(MainCitiesListFragment.this.getActivity(), countriesList.get(groupPosition) + "is collapsed", Toast.LENGTH_LONG).show();
            }
        });


        expListView.setOnChildClickListener(this);

        return view;

    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        WeatherPageFragment fragment = new WeatherPageFragment();
        City guidCity = (City) expListAdapter.getChild(groupPosition, childPosition);
        long guid = guidCity.getId();
        // pass the GUID to the fragment.
        Bundle extras = new Bundle();
        extras.putLong(MainActivity.GUID2, guid);
        fragment.setArguments(extras);

        // swap the fragment.
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment);
        transaction.commit();
        return true;
    }



    private void createGroupList() {
        countryCollection = WeatherXmlPullParser.getCountriesAndCitiesList(MainCitiesListFragment.this.getActivity());
        countriesList = new ArrayList<Country>();
        countriesList.addAll(countryCollection.keySet());
    }


    public static ArrayList<City> getCitiesList(Country country) {
        ArrayList<City> result = new ArrayList<>();
        result = country.getCities();
        return result;
    }

    private void createCollection() {
        countryCollection = WeatherXmlPullParser.getCountriesAndCitiesList(MainCitiesListFragment.this.getActivity());
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) MainCitiesListFragment.this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main_fragment_cities_list, null);
        expListView = (ExpandableListView) view.findViewById(R.id.exListView);
        countryCollection = WeatherXmlPullParser.getCountriesAndCitiesList(MainCitiesListFragment.this.getActivity());
        countriesList = new ArrayList<Country>(countryCollection.keySet());
        expListAdapter = new ExpListViewAdapter<Country, City>(MainCitiesListFragment.this.getActivity(), countriesList, countryCollection);
        expListView.setAdapter(expListAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // makes sure parent MainActivity implements
        // the callback interface. If not, it throws an exception.
        try {
            citySelectedCallback = (ExpandableListView.OnChildClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " The MainActivity activity must " +
                    "implement OnContactSelectedListener");
        }
    }

}




package com.example.myapplication2.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;


public class MainCitiesListFragment extends Fragment implements ExpandableListView.OnChildClickListener {

    private ExpandableListView expListView;
    private ExpListViewAdapter expListAdapter;
    private LinkedHashMap<Country, ArrayList<City>> countryCollection;
    private ArrayList<Country> countriesList;
    private ArrayList<City> citiesList;
    private OnFragmentInteractionListener citySelectedCallback;

    public MainCitiesListFragment() {
        super();
    }

    public static MainCitiesListFragment newInstance () {
        MainCitiesListFragment fragment = new MainCitiesListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            public void onGroupCollapse(int groupPosition) {

            }
        });


        expListView.setOnChildClickListener(this);

        return view;

    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        City guidCity = (City) expListAdapter.getChild(groupPosition, childPosition);
        long guid = guidCity.getId();
//        Bundle extras = new Bundle();
//        extras.putLong(MainActivity.GUID2, guid);
        citySelectedCallback.onFragmentInteraction(guid);
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
        Log.i("Fragment log", "Fragment  onActivityCreated");
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
            citySelectedCallback = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " The MainActivity activity must " +
                    "implement OnContactSelectedListener");
        }
    }
    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(long id);
    }


    public class DownloadXML extends AsyncTask<String, Integer, String> {

        public ProgressDialog progressDialog;
        String Url;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(true);
            progressDialog.setMessage("downloading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String content = null;
            Url = url[0];
            try {
                content = DownloadData.downloadData(url[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return content;
        }
    }
}




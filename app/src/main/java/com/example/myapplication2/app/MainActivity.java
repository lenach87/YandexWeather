package com.example.myapplication2.app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.Inflater;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ExpandableListView expListView;
    private Map<Country, ArrayList<City>> countryCollection;
    private ArrayList<Country> countriesList;
    public static final String URL_XML_FILE_YANDEX_WEATHER = "https://pogoda.yandex.ru/static/cities.xml";
    public static final String LOCAL_COPY_OF_COUNTRIES_LIST = "Countries.xml";
    private WeatherPageFragment fragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SitesDownloadTask download = new SitesDownloadTask();
//        download.execute();
        setContentView(R.layout.activity_main);

        createGroupList();
        createCollection();
        countryCollection = WeatherXmlPullParser.getCountriesAndCitiesList(this);
        countriesList.addAll(countryCollection.keySet());

        expListView = (ExpandableListView) findViewById(R.id.exListView);
        final ExpListViewAdapter expListAdapter = new ExpListViewAdapter <Country, City> (this, countriesList, countryCollection);
        expListView.setAdapter(expListAdapter);
        expListView.expandGroup(0);

        setGroupIndicatorToRight();
        final TextView textView = (TextView) findViewById(R.id.cities);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                fragment = new WeatherPageFragment();
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                final City selected = (City) expListAdapter.getCity(groupPosition, childPosition);
                selected.getId();
                transaction.replace(R.id.webview, fragment);
                transaction.addToBackStack(null);
                WebView vw = (WebView) fragment.getView();
                vw.loadUrl(WeatherXmlPullParser.getLinkToSiteForCity(MainActivity.this, selected));
                transaction.commit();
                return true;
            }
        });

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                Log.d("Logformain", "onGroupClick groupPosition = " + groupPosition +
                        " id = " + id);
                // блокируем дальнейшую обработку события для группы с позицией 1
                if (groupPosition == 1) return true;

                return false;
            }
        });

        // сворачивание группы
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            public void onGroupCollapse(int groupPosition) {
                Log.d("Logformain", "onGroupCollapse groupPosition = " + groupPosition);
                textView.setText("Свернули " + expListAdapter.getGroup(groupPosition));
            }
        });

        // разворачивание группы
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            public void onGroupExpand(int groupPosition) {
                Log.d("Logformain", "onGroupExpand groupPosition = " + groupPosition);
                textView.setText("Равзвернули " + expListAdapter.getGroup(groupPosition));
            }
        });

        // разворачиваем группу с позицией 2
    //    expListView.expandGroup(2);
    }
//        if (isNetworkAvailable()) {
//            Log.i("StackSites", "starting download Task");
//            SitesDownloadTask download = new SitesDownloadTask();
//            download.execute();
//        }

    private void createGroupList() {
        countryCollection = WeatherXmlPullParser.getCountriesAndCitiesList(this);
        countriesList = new ArrayList<Country>();
        countriesList.addAll(countryCollection.keySet());
    }


    public static ArrayList<City> getCitiesList(Country country) {
        ArrayList<City> result = new ArrayList<>();
        result = country.getCities();
        return result;
    }

    private void createCollection() {

        countryCollection = WeatherXmlPullParser.getCountriesAndCitiesList(this);

    }

//    private void loadChild(List <String> citiesOfCountry) {
//        childList = new ArrayList<String>();
//        for (String model : citiesOfCountry)
//            childList.add(model);
//    }

    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    //Helper method to determine if Internet connection is available.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
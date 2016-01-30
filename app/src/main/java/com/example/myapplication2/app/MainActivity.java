package com.example.myapplication2.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

public class MainActivity extends Activity implements ExpandableListView.OnChildClickListener {


    public static final String GUID2 = "GUID";
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();

        if (savedInstanceState == null) {
//            if it's the first time, create and display the button fragment
            MainCitiesListFragment listFragment = new MainCitiesListFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, listFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            //activity recreated from saved state
        }
    }

    //Helper method to determine if Internet connection is available.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        WeatherPageFragment weatherPageFragment = new WeatherPageFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(MainActivity.GUID2, id);
        weatherPageFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, weatherPageFragment)
                .addToBackStack(null)
                .commit();
        return true;
    }
}

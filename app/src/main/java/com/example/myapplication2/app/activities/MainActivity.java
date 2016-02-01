package com.example.myapplication2.app.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import com.example.myapplication2.app.R;
import com.example.myapplication2.app.fragments.MainCitiesListFragment;
import com.example.myapplication2.app.fragments.WeatherPageFragment;

public class MainActivity extends Activity implements MainCitiesListFragment.OnFragmentInteractionListener {


    public static final String GUID2 = "GUID";
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            fragmentManager= getFragmentManager();
//
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, MainCitiesListFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
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
    public void onFragmentInteraction(long guid) {
        Bundle extras = new Bundle();
        extras.putLong(MainActivity.GUID2, guid);
        WeatherPageFragment newFragment = WeatherPageFragment.newInstance();
        newFragment.setArguments(extras);
        // swap the fragment.
        Log.i("MainActivity log", "Fragment onChildClick 2 " + guid);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit();
    }
}

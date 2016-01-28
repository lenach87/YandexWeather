package com.example.myapplication2.app;


import android.content.Context;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.FileNotFoundException;

/**
 * Created by lena on 1/28/16.
 */
public class SitesDownloadTask extends AsyncTask <Object[], String, String> {

    public Context context;
    public City city;

    public SitesDownloadTask (Context context, City city) {

        context=this.context;
        city=this.city;
    }

    @Override
    public String doInBackground(Object[]... params) {

        try {
            DataDownloader.downloadFromUrl(WeatherXmlPullParser.getLinkToSiteForCity(context, city), context.openFileOutput("City_" + city.getId(), Context.MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return WeatherXmlPullParser.getLinkToSiteForCity(context, city);
    }


    @Override
    protected void onPostExecute(String result) {
        // The results of the above method
        // Processing the results here
        myHandler.sendEmptyMessage(0);
    }

    Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // calling to this function from other pleaces
                    // The notice call method of doing things
                    break;
                default:
                    break;
            }
        }
    };
}
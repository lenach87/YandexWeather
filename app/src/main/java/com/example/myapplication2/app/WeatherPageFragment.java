package com.example.myapplication2.app;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WeatherPageFragment extends Fragment {

        WebView wv;
        public WeatherPageFragment(){
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState, Context ctx, City city) {

            View rootView = inflater.inflate(R.layout.fragment_weather_page, container, false);
            wv=(WebView)rootView.findViewById(R.id.webview);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.loadUrl(WeatherXmlPullParser.getLinkToSiteForCity(ctx, city));
            wv.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return false;
                }
            });
            return rootView;
        }
    }

package com.example.myapplication2.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.myapplication2.app.services.DownloadData;
import com.example.myapplication2.app.activities.MainActivity;
import com.example.myapplication2.app.R;

import java.io.IOException;

import static com.example.myapplication2.app.services.WeatherXmlPullParser.*;


public class WeatherPageFragment extends Fragment {

    private WebView mWebView;
    private boolean mIsWebViewAvailable;
    private String mUrl = null;
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    private GetLinkToSite task;
    private static String LOG_TAG = "Yandex Log";


    public WeatherPageFragment () {
        super();
    }

    public static WeatherPageFragment newInstance () {
        WeatherPageFragment fragment = new WeatherPageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_weather_page, container, false);
        initInstance(view);
        mWebView.setOnKeyListener(new View.OnKeyListener(){


            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                }
                return false;
            }

        });
        mWebView.setWebViewClient(new InnerWebViewClient()); // forces it to open in app
        mWebView.loadUrl(mUrl);
        mIsWebViewAvailable = true;
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        return view;

    }

    private void initInstance (View rootview) {
        mWebView = (WebView) rootview.findViewById(R.id.webview);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "OnStart method ");
        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            long guid = args.getLong(MainActivity.GUID2);

            try {
                task = new GetLinkToSite(WeatherPageFragment.this.getActivity());
                task.execute(getLinkToCity(guid));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }



    public void loadUrl(String url) {
        if (mIsWebViewAvailable) getWebView().loadUrl(mUrl = url);
        else Log.w(LOG_TAG, "WebView cannot be found. Check the view and fragment have been loaded.");
    }


    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

//    @Override
//    public void onResume() {
//        mWebView.onResume();
//        super.onResume();
//    }


    @Override
    public void onDestroyView() {
        mIsWebViewAvailable = false;
        super.onDestroyView();
    }


    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    public WebView getWebView() {
        return mIsWebViewAvailable ? mWebView : null;
    }

    /* To ensure links open within the application */
    private class InnerWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }

    public class GetLinkToSite extends AsyncTask<String, Integer, String> {

//        public ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setCancelable(true);
//            progressDialog.setMessage("downloading...");
//            progressDialog.show();
//        }

        private Activity activityRef;

        public GetLinkToSite (Activity activityRef) {
            super();
            this.activityRef = activityRef;
        }

        @Override
        protected String doInBackground(String... url) {
            String res;

            try {
                res = DownloadData.downloadData(url[0]);
                Log.i(LOG_TAG, "url " + url[0] + " " + res);
                mUrl = getLinkToSiteForCity(res);
                Log.i(LOG_TAG, "url " + " after pull parser" + mUrl);
                return mUrl;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(LOG_TAG, "url on post execute" + " " + mUrl);
            if (mIsWebViewAvailable) getWebView().loadUrl(mUrl);
            else Log.w(LOG_TAG, "WebView cannot be found. Check the view and fragment have been loaded.");
        }
    }

    public String getLinkToCity(long id) {

        String linkToWeatherForCity = LINK_FORECAST_FOR_PARTICULAR_CITY + id + ".xml";
        Log.i(LOG_TAG, "Get link in method " + linkToWeatherForCity);
        return linkToWeatherForCity;
    }
}
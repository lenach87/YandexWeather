package com.example.myapplication2.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewFragment;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherPageFragment extends WebViewFragment {


    public WeatherPageFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // get the GUID from the managing activity.
        Bundle arguments = getArguments();
        long guid = arguments.getLong(MainActivity.GUID2);

        // open the webview to the correct page.
        WebView webView = getWebView();
        webView.loadUrl(WeatherXmlPullParser.getLinkToSiteForCity(WeatherPageFragment.this.getActivity(), guid));
        webView.getSettings().setJavaScriptEnabled(true);

        return view;
    }

    public class DownloadXML extends AsyncTask<String, Integer, String> {

        public ProgressDialog progressDialog;
        String Url;

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(true);
            progressDialog.setMessage("downloading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String ... url){
            String content = null;
            Url= url[0];
            try{
                content = downloadData(url[0]);
            }catch (IOException e){
                e.printStackTrace();
            }

            return content;
        }

        //  @Override
        public void onPostExecute(int result){

            try {
                WeatherXmlPullParser.getLinkToSiteForCity(WeatherPageFragment.this.getActivity(), result);
                progressDialog.cancel();
            }catch (Exception e) {
                e.printStackTrace();
            }
            WeatherPageFragment fragment = new WeatherPageFragment();
            WeatherPageFragment.this.getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        }
    }


    public static String downloadData(String urlCity) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(urlCity);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.connect();
            is = httpURLConnection.getInputStream();
            Log.i("Download", "Download url");
            return IOUtils.toString(is, "UTF-8");
        }
        finally {
            if (is !=null){
                is.close();
            }
        }
    }
}


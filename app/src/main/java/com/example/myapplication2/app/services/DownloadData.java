package com.example.myapplication2.app.services;

import android.util.Log;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lena on 1/31/16.
 */
public class DownloadData {

    public DownloadData() {

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
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}

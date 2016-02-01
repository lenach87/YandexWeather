package com.example.myapplication2.app;

import android.content.Context;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class WeatherXmlPullParser {

    private static final String KEY_COUNTRY = "country";
    private static final String KEY_CITY = "city";
    private static final String KEY_CITIES = "cities";
    private static String myTag = "Yandex Log";
    public static final String LINK_FORECAST_FOR_PARTICULAR_CITY = "http://export.yandex.ru/weather-ng/forecasts/";
    private static final String FORECAST_START_TAG = "forecast";

    public static LinkedHashMap <Country, ArrayList <City>> getCountriesAndCitiesList(Context ctx) {

        ArrayList<Country> countries = new ArrayList<Country>();
        LinkedHashMap <Country, ArrayList <City>> result = new LinkedHashMap<Country, ArrayList <City>>();
        Country tempCountry = null;
        City tempCity = null;
        ArrayList<City> cities = new ArrayList<City>();
        String curText = "";

        try {
            InputStream is = ctx.getResources().openRawResource(R.raw.cities);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            Log.i(myTag, "Opened File xml");

            try {

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(reader);
                int eventType = xpp.getEventType();
                // Loop through pull events until we reach END_DOCUMENT
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String tagname = "";
                    if (xpp.getName() != null) {
                        tagname = xpp.getName();
                    }
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
//                            if (tagname.equalsIgnoreCase(KEY_CITIES))
//                            {
//
//                            }
                            if (tagname.equalsIgnoreCase(KEY_COUNTRY)) {
                                tempCountry = new Country(xpp.getAttributeValue(null, "name"));
                                countries.add(tempCountry);
                            }
                            if (tagname.equalsIgnoreCase(KEY_CITY)) {
                                tempCity = new City();
                                tempCity.setId(Long.parseLong(xpp.getAttributeValue(null, "id")));
                                tempCity.setCountry(xpp.getAttributeValue(null, "country"));
                                eventType = xpp.next();
                                while ( eventType != XmlPullParser.END_TAG || 0 != KEY_CITY.compareTo(xpp.getName()) ) {

                                    // Get attributes.
                                    String  attr = xpp.getAttributeValue(null, "country");
                                    String  text = null;

                                    // Get item text if present.
                                    //  eventType = xpp.next();
                                    //  Log.i(myTag, xpp.getText());
                                    while ( eventType != XmlPullParser.END_TAG || 0 != KEY_CITY.compareTo(xpp.getName()) ) {
                                        if ( eventType == XmlPullParser.TEXT ) {
                                            text = xpp.getText();

                                            tempCity.setName(text);
                                            cities.add(tempCity);
                                        }
                                        eventType = xpp.next();
                                    }
                                }
                            }
                            break;

                        case XmlPullParser.TEXT:
                            break;

                        case XmlPullParser.END_TAG:
                            if (tagname.equalsIgnoreCase(KEY_CITIES))
                            {
                                Log.i(myTag, "Reached end of file");
                            }

                            break;

                        default:
                            break;

                    }
                    eventType = xpp.next();

                }
            }
            catch (Exception e) {
                e.printStackTrace();

            }
            finally {
                is.close();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // return the populated list.
        for (Country co: countries) {
            ArrayList<City> citiesOfCountry = new ArrayList<City>();
            for (City ci: cities) {
                if (ci.getCountry().equals(co.getName())) {
                    citiesOfCountry.add(ci);
                }
            }
            Collections.sort(citiesOfCountry);
            co.setCities(citiesOfCountry);
            result.put(co, citiesOfCountry);
            //   Log.i(myTag, "Country" + co.getName() + " has cities" + citiesOfCountry.size());
        }

        return result;
    }

    public static String getLinkToSiteForCity(String res) {

        String linkToSite = null;

        try {
            Log.i(myTag, "Try weather xml pull parser" + res);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            BufferedReader reader = new BufferedReader(new StringReader(res));
            xpp.setInput(reader);
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = "";
                if (xpp.getName() != null) {
                    tagname = xpp.getName();
                }
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (xpp.getName().equalsIgnoreCase(FORECAST_START_TAG)) {
                            linkToSite = xpp.getAttributeValue(null, "link");
                            Log.i(myTag, "Found link for city" + linkToSite);
                            return linkToSite;
                        }
                       break;


                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase(FORECAST_START_TAG))
                        {
                            Log.i(myTag, "Reached end of file");
                        }

                        break;

                    default:
                        break;

                }
                eventType = xpp.next();

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return linkToSite;
    }
}

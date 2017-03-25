package com.mobileappclass.travel.Modules;
/**
 * Created by Vishalsingh on 11/23/2016.
 */

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class PlaceFinder {
    private PlaceFinderListener listener;
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyDOge4EIZBPvf_-SvxfliLLxwAq-XtdB8A";
    private String location;
    private String radius;
    private String type;
    private static final String TAG = "MyActivity";


    public PlaceFinder(PlaceFinderListener listener, String loc, String rad, String type) {

        this.listener = listener;
        this.location = loc;
        this.radius = rad;
        this.type = type;
    }


    public void execute() throws UnsupportedEncodingException {
        listener.PlaceFinderStart(type);
        new DownloadRawData().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        String urlLocation = URLEncoder.encode(location, "utf-8");
        String urlRadius = URLEncoder.encode(radius, "utf-8");
        String urlType = URLEncoder.encode(type, "utf-8");

        return DIRECTION_URL_API + "location=" + urlLocation + "&radius=" + urlRadius + "&type=" + urlType +"&key=" + GOOGLE_API_KEY;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJSon(String data) throws JSONException {
        if (data == null)
            return;

        List<Place> spots = new ArrayList<Place>();

        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonPlaces = jsonData.getJSONArray("results");
       
       
        System.out.println(jsonPlaces.length()+" Result length *******************");


        for (int i = 0; i < jsonPlaces.length(); i++) {


            JSONObject jsonRoute = jsonPlaces.getJSONObject(i);

            Place place = new Place();

            JSONObject geometry = jsonRoute.getJSONObject("geometry");
            JSONObject loc = geometry.getJSONObject("location");

            if(jsonRoute.has("opening_hours")){
                JSONObject opennow = jsonRoute.getJSONObject("opening_hours");
                place.open_now = opennow.getBoolean("open_now");

            }
            else
            {

                place.open_now = false;


            }

            if(jsonRoute.has("rating")) {

                place.rating = jsonRoute.getDouble("rating");
            }
            else
            {

                place.rating = 1.1;
            }


            place.place_name = jsonRoute.getString("name");
            place.address =jsonRoute.getString("vicinity");

           // if(jsonRoute.getDouble("rating")!);
            //place.rating = jsonRoute.getDouble("rating");

            place.Location = new LatLng(loc.getDouble("lat"), loc.getDouble("lng"));





            spots.add(place);
        }

        listener.PlaceFinderSuccess(spots,type);
    }


}


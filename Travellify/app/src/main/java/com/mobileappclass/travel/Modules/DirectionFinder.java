package com.mobileappclass.travel.Modules;

import android.os.AsyncTask;
import android.util.Log;

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


public class DirectionFinder {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyBFjK8UInAeNGfhx8attCH8UNY6xzNjuwU";
    private DirectionFinderListener listener;
    private String origin;
    private String destination;
    private String wayPoint;
    private static final String TAG = "MyActivity";
    private static boolean isWayPoint;

    public DirectionFinder(DirectionFinderListener listener, String origin, String destination) {
        this.listener = listener;
        this.origin = origin;
        this.destination = destination;
        isWayPoint = false;
        System.out.println("NON Way point  constructor" );
    }

    public DirectionFinder(DirectionFinderListener listener, String origin, String destination, String wayPoint) {
        this.listener = listener;
        this.origin = origin;
        this.destination = destination;
        this.wayPoint = wayPoint;
        isWayPoint = true;
        System.out.println("Way point  constructor" );
    }

    public void execute() throws UnsupportedEncodingException {
        listener.onDirectionFinderStart();
        new DownloadRawData().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(origin, "utf-8");
        String urlDestination = URLEncoder.encode(destination, "utf-8");


        if (!isWayPoint) {
            System.out.println("Non Way point  constructor" );
            String url = DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination + "&key=" + GOOGLE_API_KEY;
            System.out.println("Way point url " + url);
            return url;
        }
        else {
            String urlWayPoint = URLEncoder.encode(wayPoint, "utf-8");
            String url = DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination + "&waypoints=" + urlWayPoint + "&key=" + GOOGLE_API_KEY;
            System.out.println("Way point url");
            System.out.println("Way point url " + url);
            return url;
        }
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

        List<Route> routes = new ArrayList<Route>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
System.out.println(jsonRoutes.length()+" Route length *******************");


        for (int i = 0; i < jsonRoutes.length(); i++) {


            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            if(jsonRoute != null) {
                System.out.println(jsonRoute.toString());
            }
            Route route = new Route();

            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
            JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
            JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");



            route.distance = new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value"));
            route.duration = new Duration(jsonDuration.getString("text"), jsonDuration.getInt("value"));
            if(isWayPoint){


                JSONObject jsonLeg1 = jsonLegs.getJSONObject(1);
                jsonEndLocation = jsonLeg1.getJSONObject("end_location");
                JSONObject jsonDistance1 = jsonLeg1.getJSONObject("distance");
                JSONObject jsonDuration1 = jsonLeg1.getJSONObject("duration");


                int dist;
                String _distance;
                String _Duration;
                int time;

                dist = ((jsonDistance.getInt("value")+jsonDistance1.getInt("value"))/1610);
                time = ((jsonDuration.getInt("value")+jsonDuration1.getInt("value"))/60);

                _distance = String.valueOf(dist + " mi");
                _Duration = String.valueOf(time + " mins");

                route.distance = new Distance(_distance, dist);
                route.duration = new Duration(_Duration,time);


            }

            route.endAddress = jsonLeg.getString("end_address");
            route.startAddress = jsonLeg.getString("start_address");
            route.startLocation = new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
            route.endLocation = new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
            route.points = decodePolyLine(overview_polylineJson.getString("points"));

            /////////////////////////
            Log.d(TAG, "**** "+ route.points);

            routes.add(route);
        }

        listener.onDirectionFinderSuccess(routes);
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
}

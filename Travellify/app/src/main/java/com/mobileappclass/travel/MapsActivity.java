package com.mobileappclass.travel;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mobileappclass.travel.Modules.DirectionFinder;
import com.mobileappclass.travel.Modules.DirectionFinderListener;
import com.mobileappclass.travel.Modules.PermissionUtils;
import com.mobileappclass.travel.Modules.Place;
import com.mobileappclass.travel.Modules.PlaceFinder;
import com.mobileappclass.travel.Modules.PlaceFinderListener;
import com.mobileappclass.travel.Modules.Route;
import com.mobileappclass.travel.activity.RecognizeConceptsActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MapsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        DirectionFinderListener, PlaceFinderListener

{

    private GoogleMap mMap;

    public static LatLng currentLocation;
    private GoogleApiClient mGoogleApiClient;


    String mLatitude;
    String mLongitude;
    int TAG_CODE_PERMISSION_LOCATION = 1;
    private Button btnFindPath;
    //private EditText etOrigin;
    private EditText etDestination;
    String destinationString;

    //Markers
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> restaurant_Markers = new ArrayList<>();
    private List<Marker> liquor_store_Markers = new ArrayList<>();
    private List<Marker> hospital_Markers = new ArrayList<>();
    private List<Marker> gas_station_Markers = new ArrayList<>();
    private List<Marker> resultMarkers = new ArrayList<>();


    private List<Place> spots = new ArrayList<Place>();

    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    private ProgressDialog progressDialog;
    private LatLng current;
    private static final int REQUEST_CODE = 1;

    private String wayPoint;
    private Double wayPointLat;
    private Double wayPointLong;
    private boolean isWayPoint;


    public final String Tag = "Testing";
    private String PLACES_URL = "http://travellify.freevar.com/searchPlaceID.php?";

    public static String userName;


    //navbar stuff
    String[] menu;
    DrawerLayout dLayout;
    ListView dList;
    ArrayAdapter<String> adapter;
    public static String  displayName;
    public static String displayEmail;
    public static String displayPhoto;
    static String currentLocationStringGlobal;
//    TextView displayNameTextView;
//    TextView displayEmailTextView;



    boolean isRestaurantClicked = false;
    boolean isHospitalClicked = false;
    boolean isLiquorClicked = false;
    boolean isGasClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.displayName);
        TextView nav_email = (TextView)hView.findViewById(R.id.displayEmail);

        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        etDestination = (EditText) findViewById(R.id.etDestination);
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest("new","new");
            }
        });


        checkSavedInstances(savedInstanceState);
        Intent intent = getIntent();

        String activityName = intent.getStringExtra("activityName");

        if(activityName.equals("WayPoints")){

            wayPointLat = intent.getDoubleExtra("wayPointLat", 0);
            wayPointLong = intent.getDoubleExtra("wayPointLong", 0);
            wayPoint = String.valueOf(wayPointLat) + "," + String.valueOf(wayPointLong);
            destinationString = intent.getStringExtra("destinationString");
            Double currLat = intent.getDoubleExtra("currLat",0);
            Double currLong = intent.getDoubleExtra("currLong",0);
            currentLocation = new LatLng(currLat,currLong);
            System.out.println("wayPoint " + wayPoint + destinationString + " " + currentLocation.toString());
            isWayPoint = true;
            sendWayRequest();
        }
        else if(activityName.equals("getAroundImages")){

            Double aroundLat = intent.getDoubleExtra("aroundLat",0);
            Double aroundLong = intent.getDoubleExtra("aroundLong", 0);
            String currentLocationString = intent.getStringExtra("currentLocationString");
            destinationString = String.valueOf(aroundLat) + "," + String.valueOf(aroundLong);
            System.out.println("destinationString "  + destinationString);
            sendRequest(currentLocationString,destinationString);
        }
        else if(activityName.equals("restos")) {
            Toast.makeText(MapsActivity.this, "Select using Nav Bar", Toast.LENGTH_SHORT).show();
        }
        else{
            displayName = intent.getStringExtra("displayName");
            displayEmail = intent.getStringExtra("displayEmail");
            displayPhoto = intent.getStringExtra("displayPhoto");
            userName = displayEmail.replace("@gmail.com", "");
            isWayPoint = false;
        }



        nav_user.setText(displayName);
        nav_email.setText(displayEmail);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



    }




    public void checkSavedInstances(Bundle savedInstanceState){
        if(savedInstanceState != null) {



                LatLng cL = new LatLng(savedInstanceState.getDouble("lat"), savedInstanceState.getDouble("long"));



                if(savedInstanceState.getString("etDestination") != null) {
                    if (!savedInstanceState.getString("etDestination").equals("")) {
                        destinationString = savedInstanceState.getString("etDestination");
                        etDestination.setText(destinationString);
                    //    btnFindPath.performClick();
//                        try {
//                            new DirectionFinder(this, currentLocationStringGlobal.replace("lat/lng: (", "").replace(")", ""), savedInstanceState.getString("etDestination")).execute();
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
                    }
                }

                String temp1 = "500";
                String temp2 = "";
                String temp = savedInstanceState.getString("currentLocation").replace("lat/lng: (", "").replace(")", "");

                if (savedInstanceState.getBoolean("isRestaurantClicked") == true) {
                    temp2 = "restaurant";
                }
                if (savedInstanceState.getBoolean("isHospitalClicked") == true) {
                    temp2 = "hospital";
                }
                if (savedInstanceState.getBoolean("isLiquorClicked") == true) {
                    temp2 = "liquor_store";
                }
                if (savedInstanceState.getBoolean("isGasClicked") == true) {
                    temp2 = "gas_station";
                }

                try {
                    new PlaceFinder(this, temp, temp1, temp2).execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       // mMap.addMarker(new MarkerOptions().position(new LatLng(40, -75)).title("Marker"));

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 1);

            return;

        }
        
        mMap.setMyLocationEnabled(true);

        if (mMap != null) {

            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {
                    currentLocation = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                    currentLocationStringGlobal = currentLocation.toString().replace("lat/lng: (","").replace(")","");

               //     mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("Source"));
                }
            });
            mMap.setOnMarkerClickListener(this);

        }




    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        System.out.println("marker clicked" + marker.getTitle());

        if (marker.getTitle().equals("Source"))
        {

            Intent intent = new Intent(this,DownloadImages.class);
            intent.putExtra("activityName", "MapsActivity");
            intent.putExtra("hotSpotLat", currentLocation.latitude);
            intent.putExtra("hotSpotLong", currentLocation.longitude);
            intent.putExtra("hotSpotName", "Name");
            startActivity(intent);
           // Toast.makeText(this,"Source clicked", Toast.LENGTH_LONG).show();
        }
        if (marker.getTitle().equals("Destination"))
        {
            Intent intent = new Intent(this,DownloadImages.class);
            intent.putExtra("activityName", "MapsActivity");
            intent.putExtra("hotSpotLat", marker.getPosition().latitude);
            intent.putExtra("hotSpotLong",marker.getPosition().longitude);
            intent.putExtra("hotSpotName", "Name");
            startActivity(intent);
           // Toast.makeText(this,"Destination clicked", Toast.LENGTH_LONG).show();
        }
        if (marker.getTitle().equals("SPOTS"))
        {
            List<Address> addresses = null;
            boolean addressRec = false;
            String hotSpotName = "";
            Double hotSpotLat = marker.getPosition().latitude;
            Double hotSpotLong = marker.getPosition().longitude;

            Intent intent = new Intent(this,DownloadImages.class);
            intent.putExtra("activityName", "MapsActivity");
            intent.putExtra("place_id", 2);
            intent.putExtra("destinationString", destinationString);
            intent.putExtra("hotSpotLat", hotSpotLat);
            intent.putExtra("hotSpotLong",hotSpotLong);
            intent.putExtra("hotSpotName", hotSpotName);
            intent.putExtra("currLat", currentLocation.latitude);
            intent.putExtra("currLong", currentLocation.longitude);
            startActivity(intent);
           // Toast.makeText(this,"Hot spot clicked", Toast.LENGTH_LONG).show();
        }
        else if(restaurant_Markers.contains(marker)){

            Place myplace = (Place) marker.getTag();

            Intent i = new Intent(getApplicationContext(), PlaceDetails.class);
            i.putExtra("type", "restaurant");
            i.putExtra("name",myplace.place_name);
            i.putExtra("address",myplace.address);
            i.putExtra("rating",myplace.rating);
            i.putExtra("open", (boolean) myplace.open_now);

            startActivity(i);
          //  Toast.makeText(MapsActivity.this, "Restaurant",Toast.LENGTH_SHORT).show();
        }
        else if(liquor_store_Markers.contains(marker)){
            Place myplace = (Place) marker.getTag();

            Intent i = new Intent(getApplicationContext(), PlaceDetails.class);
            i.putExtra("type", "liquor");
            i.putExtra("name",myplace.place_name);
            i.putExtra("address",myplace.address);
            i.putExtra("rating",myplace.rating);
            i.putExtra("open",(boolean) myplace.open_now);

            startActivity(i);

            //startActivity(i);
           // Toast.makeText(MapsActivity.this, "Liquor",Toast.LENGTH_SHORT).show();
        }
        else if(gas_station_Markers.contains(marker)){
            Place myplace = (Place) marker.getTag();

            Intent i = new Intent(getApplicationContext(), PlaceDetails.class);
            i.putExtra("type", "gas_station");
            i.putExtra("name",myplace.place_name);
            i.putExtra("address",myplace.address);
            i.putExtra("rating",myplace.rating);
            i.putExtra("open", (boolean) myplace.open_now);
            startActivity(i);

        }
        else if(hospital_Markers.contains(marker)){
            Place myplace = (Place) marker.getTag();
            Intent i = new Intent(getApplicationContext(), PlaceDetails.class);
            i.putExtra("type", "hospital");
            i.putExtra("name",myplace.place_name);
            i.putExtra("address",myplace.address);
            i.putExtra("rating",myplace.rating);
            i.putExtra("open", (boolean) myplace.open_now);
            startActivity(i);
        }



        return true;
    }






    private void sendRequest(String source, String dest) {
        String destination;
        String origin;
        if(dest.equals("new")){
            origin = currentLocation.toString().replace("lat/lng: (", "").replace(")", "");
            destination = etDestination.getText().toString();
        }
        else{
            destination = dest;
            origin = source;
        }


        destinationString = destination;
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

                new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    public void sendWayRequest(){
        try {
            String origin = currentLocation.toString().replace("lat/lng: (","").replace(")","");
                System.out.println("origin " + currentLocation.toString() + destinationString + wayPoint);
                System.out.println("isWayPoint " + isWayPoint);
                new DirectionFinder(this, origin, destinationString, wayPoint).execute();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
        if (resultMarkers != null) {
            for (Marker marker : resultMarkers) {
                marker.remove();
            }
        }


    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {

        Log.v(Tag, "Entered Listener" + routes.size());
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();


        destinationMarkers = new ArrayList<>();
        for (Route route : routes) {

            Log.v(Tag, "Entered routes " + routes.size());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 5));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_start))
                    .title("Source")

                    .position(route.startLocation)));


            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_destination))
                    .title("Destination")
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.argb(200,153,81,0)).

                    width(10);


            for (int i = 0; i < route.points.size(); i++) {
                polylineOptions.add(route.points.get(i));


                if (i % 17 == 0) {
                   // originMarkers.add(mMap.addMarker(new MarkerOptions()

                       //     .title("HotSpots")
                      //      .position(route.points.get(i))));

                    LatLng localLatLng = route.points.get(i);

                    System.out.println(localLatLng.latitude);
                    getPlaceFromDB(String.valueOf(route.points.get(i).latitude), String.valueOf(route.points.get(i).longitude));

                    Log.v(Tag, "poly line draw");
                }

            }
            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }


    @Override
    public void PlaceFinderStart(String type) {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding " + type + " !", true);



        if (restaurant_Markers != null) {
            for (Marker marker : restaurant_Markers) {
                marker.remove();
            }
            isRestaurantClicked = false;
        }

        if (liquor_store_Markers != null) {
            for (Marker marker : liquor_store_Markers) {
                marker.remove();
            }
            isLiquorClicked = false;
        }

        if (gas_station_Markers != null) {
            for (Marker marker : gas_station_Markers) {
                marker.remove();
            }
            isGasClicked = false;

        }
        if (hospital_Markers != null) {
            for (Marker marker : hospital_Markers) {
                marker.remove();
            }
            isHospitalClicked = false;

        }
    }

    @Override
    public void PlaceFinderSuccess(List<Place> places, String type) {
        progressDialog.dismiss();

        spots = places;

        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();

        destinationMarkers = new ArrayList<>();
        for (Place place : places) {


            if (type == "restaurant") {

                isRestaurantClicked = true;
                Marker mymarker = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant))

                        .title(place.place_name)

                        .position(place.Location));
                mymarker.setTag(place);

                restaurant_Markers.add(mymarker);


            }


            else if (type.equals("liquor_store")) {

                isLiquorClicked = true;

                Marker mymarker = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.wiskey




                        ))

                        .title(place.place_name)

                        .position(place.Location));
                mymarker.setTag(place);
                liquor_store_Markers.add(mymarker);


            }
            else if (type.equals("hospital")) {

                isHospitalClicked = true;

                Marker mymarker = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital))

                        .title(place.place_name)

                        .position(place.Location));
                mymarker.setTag(place);
                hospital_Markers.add(mymarker);


            }
            else {
                isGasClicked = true;
                Marker mymarker = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.gas_station))

                        .title(place.place_name)

                        .position(place.Location));

                mymarker.setTag(place);

                gas_station_Markers.add(mymarker);


            }


        }


    }


    /***********Get lat / long of hots spots from db*****************/

    private void getPlaceFromDB(String latitude, String longitude) {
        class GetPlace extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MapsActivity.this, "Fetching Data", "Please Wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                String got[] = s.split(",");
                if(got.length ==2) {
                    System.out.println(s + "hotspot received");
                    LatLng latLng = new LatLng(Double.parseDouble(got[0]), Double.parseDouble(got[1]));

                    Marker mymarker = mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .title("SPOTS").position(latLng));

                    resultMarkers.add(mymarker);

                    System.out.println("Hotspots " + got[0] + "," + got[1]);
                }
                else{
                    System.out.println("FAILEDD");
                }

            }

            @Override
            protected String doInBackground(String... params) {

                String uri = PLACES_URL + "latitude=" + params[0] +"&longitude=" + params[1] ;
               // System.out.println(uri + "url");
                BufferedReader bufferedReader;
                URL url;
                try {
                    System.out.println("try");
                    url = new URL(uri);

                    bufferedReader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = bufferedReader.readLine()) != null) {
                        response.append(inputLine);
                    }
                    bufferedReader.close();


                   // String latlng = response.toString();

                    return response.toString();

                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetPlace gai = new GetPlace();
        gai.execute(latitude, longitude);
    }



    /******nav bar*********/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_logout) {
            Intent intent = new Intent();
            intent.putExtra("name", "value");
            setResult(RESULT_OK, intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, RecognizeConceptsActivity.class);
            intent.putExtra("latitude", String.valueOf(currentLocation.latitude));
            intent.putExtra("longitude", String.valueOf(currentLocation.longitude));
            startActivity(intent);
        }
        else if (id == R.id.nav_profile) {
            System.out.println("Profile");
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("displayName",displayName);
            intent.putExtra("displayEmail", displayEmail);
            intent.putExtra("displayPhoto", displayPhoto);
            intent.putExtra("userID", "3");
            startActivity(intent);

        }
        else if (id == R.id.nav_restaurant) {
            String temp = currentLocation.toString().replace("lat/lng: (","").replace(")", "");
            try {
                String temp1 = "500";
                String temp2 = "restaurant";

                new PlaceFinder(this, temp, temp1, temp2).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else if (id == R.id.nav_gas) {
            String temp = currentLocation.toString().replace("lat/lng: (","").replace(")", "");
            try {

                String temp1 = "5000";
                String temp2 = "gas_station";

                new PlaceFinder(this, temp, temp1, temp2).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }

        }
        else if (id == R.id.nav_liquor) {
            String temp = currentLocation.toString().replace("lat/lng: (","").replace(")", "");
                        try {
                String temp1 = "1000";
                String temp2 = "liquor_store";
                new PlaceFinder(this, temp, temp1, temp2).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }


        }
        else if (id == R.id.nav_hospital) {
            String temp = currentLocation.toString().replace("lat/lng: (","").replace(")", "");

            try {

                String temp1 = "5000";
                String temp2 = "hospital";

                new PlaceFinder(this, temp, temp1, temp2).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }
        }
            else if (id == R.id. nav_hotspots) {

            Intent intent = new Intent(this, DownloadImages.class);
            intent.putExtra("activityName", "TagActivity");
            intent.putExtra("currentLocationString", currentLocation.toString().replace("lat/lng: (","").replace(")",""));

            intent.putExtra("hotSpotLat", currentLocation.latitude);
            intent.putExtra("hotSpotLong", currentLocation.longitude);
            startActivity(intent);
          //  Toast.makeText(this,"Search Hotspots clicked", Toast.LENGTH_LONG).show();

            }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("etDestination", destinationString);

        outState.putString("currentLocation", currentLocation.toString());
        outState.putDouble("lat", currentLocation.latitude);
        outState.putDouble("long", currentLocation.longitude);


        outState.putBoolean("isRestaurantClicked", isRestaurantClicked);
        outState.putBoolean("isHospitalClicked", isHospitalClicked);
        outState.putBoolean("isLiquorClicked", isLiquorClicked);
        outState.putBoolean("isGasClicked", isGasClicked);



    }


}

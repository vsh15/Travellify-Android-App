package com.mobileappclass.travel;



import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.mobileappclass.travel.Modules.PlaceFinder;
import com.mobileappclass.travel.activity.RecognizeConceptsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;



public class DownloadImages extends AppCompatActivity implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    private String imagesJSON;

    private static final String JSON_ARRAY = "result";
    private static final String IMAGE_URL = "url";



    private JSONArray arrayImages = null;

    private int TRACK = 0;

    private static final String IMAGES_URL = "http://travellify.freevar.com/getAllImages.php?";
    private static final String IMAGES_URL_USER = "http://travellify.freevar.com/getUserImages.php?";
    private static final String IMAGES_URL_SHARED_IMAGES = "http://travellify.freevar.com/getSharedImages.php?";
    private static final String IMAGES_URL_TAG = "http://travellify.freevar.com/getImagesAround1.php?";


    private Button buttonSearchTag;
    private Button buttonWayPoint;
    private EditText editSearchTag;
    private ImageView imageView;
    private TextView placeName;
    private Bitmap myBitMap;
    private GridView gridView;
    private CustomGrid adapter;
    //  private GridViewAdapter gridAdapter;
    private static ArrayList<ImageItem> imageItems;
    private static int imageCount =0;
    private int placeId;
    private double hotSpotLat;
    private double hotSpotLong;
    private String destinationString;
    private String wayPointString;
    private String hotSpotName = "";
    private String activityName = "";
    private String userID;
    private String tag = "";
    Double currLat;
    Double currLong;

    String currentLocationString;
   ArrayList<Double> aroundLat;
    ArrayList<Double> aroundLong;
    Boolean aroundMe = false;
    /********zoom**********/

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_images);

        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);



        imageItems = new ArrayList<>();
        aroundLat = new ArrayList<>();
        aroundLong = new ArrayList<>();

        imageView = (ImageView) findViewById(R.id.imageView);
        placeName = (TextView) findViewById(R.id.placeName);
        editSearchTag = (EditText) findViewById(R.id.editSearchTag);
        buttonSearchTag = (Button) findViewById(R.id.buttonSearchTag);
        buttonWayPoint = (Button) findViewById(R.id.buttonWayPoint);

        buttonWayPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("activityName","WayPoints");
                intent.putExtra("destinationString",destinationString);
                intent.putExtra("wayPointLat", hotSpotLat);
                intent.putExtra("wayPointLong", hotSpotLong);
                intent.putExtra("currLat", currLat);
                intent.putExtra("currLong", currLong);


                startActivity(intent);

            }
        });

        buttonSearchTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag = editSearchTag.getText().toString().trim();
                resetAll();

                if(tag.equals("")){
                    Toast.makeText(DownloadImages.this, "Please enter a tag!", Toast.LENGTH_SHORT).show();
                }
                else{
                    editSearchTag.setText("");
                    getAllImages();
                }
            }
        });

//        buttonFetchImages = (Button) findViewById(R.id.buttonFetchImages);
//        buttonFetchImages.setOnClickListener(this);

        Intent intent = getIntent();

        if(intent != null){
            activityName = intent.getStringExtra("activityName");
            if(activityName.equals("MapsActivity")) {
                System.out.println("in maps intent");

                placeId = intent.getIntExtra("place_id", 0);
                hotSpotLat = intent.getDoubleExtra("hotSpotLat", 0);
                hotSpotLong = intent.getDoubleExtra("hotSpotLong", 0);
                hotSpotName = intent.getStringExtra("hotSpotName");
                destinationString = intent.getStringExtra("destinationString");
                 currLat = intent.getDoubleExtra("currLat", 0);
                 currLong = intent.getDoubleExtra("currLong", 0);
                buttonWayPoint.setVisibility(View.VISIBLE);
                getAllImages();
            }
            else if (activityName.equals("ProfileActivity")) {

                userID = intent.getStringExtra("userID");
                System.out.println(userID);
                getAllImages();
            }
            else if(activityName.equals("sharedImages")){
                userID = intent.getStringExtra("userID");
                System.out.println(userID);
                getAllImages();
            }
            else if (activityName.equals("TagActivity")) {

                Toast.makeText(DownloadImages.this, "Long Click to view HOTSPOT", Toast.LENGTH_SHORT).show();
                aroundMe =true;
                editSearchTag.setVisibility(View.VISIBLE);
                buttonSearchTag.setVisibility(View.VISIBLE);
                hotSpotLat = intent.getDoubleExtra("hotSpotLat", 0);
                hotSpotLong = intent.getDoubleExtra("hotSpotLong", 0);
                currentLocationString = intent.getStringExtra("currentLocationString");
            }



        }


        gridView=(GridView)findViewById(R.id.grid);

        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mapsIntent = new Intent(getApplicationContext(), MapsActivity.class);
                mapsIntent.putExtra("activityName", "getAroundImages");
                mapsIntent.putExtra("aroundLat", aroundLat.get(position));
                mapsIntent.putExtra("aroundLong", aroundLong.get(position));
                mapsIntent.putExtra("currentLocationString", currentLocationString);
                startActivity(mapsIntent);

              return  true;
            }
        });


        if(savedInstanceState != null) {
            if (savedInstanceState.getString("saved").equals("saved123")) {

                imagesJSON = savedInstanceState.getString("imagesJSON", imagesJSON);
                aroundMe = savedInstanceState.getBoolean("aroundMe", aroundMe);
                System.out.println("imagesJSON" + aroundMe + "    " + imagesJSON);
                extractJSON();
                getImage("get");
            }
        }
    }


//
//    @Override
//    public void onClick(View v) {
//        if(v == buttonFetchImages) {
//            getAllImages();
//        }
//        if(v == buttonMoveNext){
//            moveNext();
//        }
//        if(v== buttonMovePrevious){
//            movePrevious();
//        }
//    }

    private void getAllImages() {
        class GetAllImages extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DownloadImages.this, "Fetching Data", "Please Wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                System.out.println("string s " + s);
                if(s.trim().equals("FAIL899")) {
                    System.out.println("No images for tag : " + tag);
                }
                else{
                    imagesJSON = s;
                    extractJSON();
                    getImage("get");
                }
                //showImage();



                //Toast.makeText(DownloadImages.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = "";
                System.out.print(activityName);
                if(activityName.equals("MapsActivity")) {
                     uri = IMAGES_URL + "latitude=" + hotSpotLat + "&longitude=" + hotSpotLong;
                    System.out.print(uri);
                }
                else if (activityName.equals("ProfileActivity")) {

                     uri = IMAGES_URL_USER + "user_id=" + userID;
                    System.out.println("uri" + uri);
                }
                else if (activityName.equals("TagActivity")) {

                    uri = IMAGES_URL_TAG + "latitude=" + hotSpotLat + "&longitude=" + hotSpotLong + "&tag=" + tag.toLowerCase();

                }
                else if(activityName.equals("sharedImages")){
                    uri = IMAGES_URL_SHARED_IMAGES + "id=" + userID;
                }


                System.out.println(uri);
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

                    String encodedImage = response.toString();
                    System.out.println(encodedImage);

                    return encodedImage;

                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetAllImages gai = new GetAllImages();
        gai.execute(IMAGES_URL);
    }



    private void getImage(String urlToImage){
        class GetImage extends AsyncTask<String,Void,Bitmap>{
          //  ProgressDialog loading;
            @Override
            protected Bitmap doInBackground(String... params) {
                URL url = null;
                Bitmap image = null;
                BufferedReader bufferedReader;
                if (arrayImages != null) {
                    for (int i = 0; i < arrayImages.length(); i++) {

                        try {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    adapter = new CustomGrid(DownloadImages.this, imageItems);
                                    gridView.setAdapter(adapter);
                                }
                            });
                        } catch (final Exception ex) {
                            System.out.println("---" + "Exception in thread");
                        }
                        //String urlToImage = params[0];
                        try {
                            JSONObject jsonObject = arrayImages.getJSONObject(i);
                            String urlToImage = jsonObject.getString(IMAGE_URL);
                            System.out.println(urlToImage);

                            if (aroundMe) {
                                System.out.println(jsonObject.getString("latitude"));
                                System.out.println(jsonObject.getString("longitude"));
                                aroundLat.add(Double.parseDouble(jsonObject.getString("latitude")));
                                aroundLong.add(Double.parseDouble(jsonObject.getString("longitude")));
                            }
                            url = new URL(urlToImage);
                            bufferedReader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));

                            String inputLine;
                            StringBuffer response = new StringBuffer();
                            while ((inputLine = bufferedReader.readLine()) != null) {
                                response.append(inputLine);
                            }
                            bufferedReader.close();

                            String encodedImage = response.toString();
                            System.out.println(encodedImage);
                            byte[] decodedString = Base64.decode(encodedImage.getBytes(), Base64.DEFAULT);
                            System.out.println(decodedString);
                            image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            System.out.println("adding into image items" + imageCount);
                            imageItems.add(new ImageItem(image, "Image#" + imageCount));
                            imageCount++;
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.image_placehoder2);
                }
                    return image;
                }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
         //       loading = ProgressDialog.show(DownloadImages.this,"Downloading Image...","Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
         //       loading.dismiss();

                if(imageItems != null){
                    System.out.println("size " + imageItems.size());
                }
                else {
                    System.out.println("is nukk");
                }
                adapter = new CustomGrid(DownloadImages.this, imageItems);
                gridView.setAdapter(adapter);
//                imageView.setImageBitmap(bitmap);
//                placeName.setText(hotSpotName);
            }
        }
        GetImage gi = new GetImage();
        gi.execute(urlToImage);
    }


    private void extractJSON(){
        try {
            JSONObject jsonObject = new JSONObject(imagesJSON);
            arrayImages = jsonObject.getJSONArray(JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

      //  ImageItem imageItem = imageItems.get(position);
        Bitmap img = imageItems.get(position).getImage();
        View thumbView = view;
        zoomImageFromThumb(thumbView, img);
        System.out.println(position);


    }


    /*********zoom into pic***************/




    private void zoomImageFromThumb(final View thumbView, Bitmap img) {
        // If there's an animation in progress, cancel it immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(R.id.expanded_image);

        expandedImageView.setImageBitmap(img);

        // Calculate the starting and ending bounds for the zoomed-in image. This step
        // involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }


        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);


        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left,
                        finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top,
                        finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down to the original bounds
        // and show the thumbnail instead of the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }


                AnimatorSet set = new AnimatorSet();
                set
                        .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }


    public void resetAll(){
        imageItems.clear();
        imageCount = 0;
    }


//    private void moveNext(){
//        if(TRACK < arrayImages.length()){
//            TRACK++;
//            showImage();
//        }
//    }
//
//    private void movePrevious(){
//        if(TRACK>0){
//            TRACK--;
//            showImage();
//        }
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        MapsActivity mapsActivity = new MapsActivity();
        int id = item.getItemId();
        if (id == R.id.nav_camera) {

            System.out.println("mapsActivity.currentLocation" + mapsActivity.currentLocation);
            Intent intent = new Intent(this, RecognizeConceptsActivity.class);
            intent.putExtra("latitude", String.valueOf(mapsActivity.currentLocation.latitude));
            intent.putExtra("longitude", String.valueOf(mapsActivity.currentLocation.longitude));
            startActivity(intent);
        }
        else if (id == R.id.nav_profile) {
            System.out.println("Profile");
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("displayName", mapsActivity.displayName);
            intent.putExtra("displayEmail", mapsActivity.displayEmail);
            intent.putExtra("displayPhoto", mapsActivity.displayPhoto);
            intent.putExtra("userID", "3");
            startActivity(intent);

        }
        else if (id == R.id.nav_restaurant) {

            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("activityName", "restos");
            startActivity(intent);
        } else if (id == R.id.nav_gas) {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("activityName", "restos");
            startActivity(intent);

        }
        else if (id == R.id.nav_liquor) {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("activityName", "restos");
            startActivity(intent);
        }
        else if (id == R.id.nav_hospital) {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("activityName", "restos");
            startActivity(intent);
        }
        else if (id == R.id. nav_hotspots) {

//            Intent intent = new Intent(this, DownloadImages.class);
//            intent.putExtra("activityName", "TagActivity");
//            intent.putExtra("currentLocationString", currentLocation.toString().replace("lat/lng: (","").replace(")",""));
//
//            intent.putExtra("hotSpotLat", currentLocation.latitude);
//            intent.putExtra("hotSpotLong", currentLocation.longitude);
//            startActivity(intent);
//            //  Toast.makeText(this,"Search Hotspots clicked", Toast.LENGTH_LONG).show();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString("saved", "saved123");
        outState.putString("imagesJSON", imagesJSON);
        outState.putBoolean("aroundMe",aroundMe);
    }
}
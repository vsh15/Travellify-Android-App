package com.mobileappclass.travel;

import android.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobileappclass.travel.activity.RecognizeConceptsActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;


import static com.mobileappclass.travel.R.id.image;
import static com.mobileappclass.travel.R.id.thing_proto;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener
        {


    public static final String UPLOAD_ADD_USER = "http://travellify.freevar.com/addUser.php";
    public static final String UPLOAD_GET_USER = "http://travellify.freevar.com/selectUser.php?";



    private SignInButton btnSignIn;
    private Button btnSignOut;
    private TextView name;
    public static GoogleApiClient googleApiClient;
    private static GoogleSignInOptions googleSignInOptions;
    private ProgressDialog mProgressDialog;
    private static final int REQ_CODE = 1233;
    private static final int MAPS_ACTIVITY_CODE = 899;
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean logout = false;
    String personName = "Arya";
    String personEmail ="Stark";
    String personPhotoUrl = "";
    public TextView textView;
    public ImageView image;
    public Animation animSlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  super.onBackPressed();
        setContentView(R.layout.activity_main);



        btnSignIn = (SignInButton) findViewById(R.id.signIn);
        btnSignOut = (Button) findViewById(R.id.signOut);
//        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
//        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
//        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
//        txtName = (TextView) findViewById(R.id.txtName);
//        txtEmail = (TextView) findViewById(R.id.txtEmail);

        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
//        btnRevokeAccess.setOnClickListener(this);

        googleSignInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

                googleApiClient = new GoogleApiClient.Builder(this)
                        .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();



/********* animations *************/
        image = (ImageView) findViewById(R.id.imageView2);
        image.setImageDrawable(getResources().getDrawable(R.drawable.logo));



        animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slides);


        image.startAnimation(animSlide);

        textView = (TextView) findViewById(R.id.textView);

        animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.alpha);

        textView.startAnimation(animSlide);

        btnSignIn = (SignInButton) findViewById(R.id.signIn);
        animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slideup);

        btnSignIn.startAnimation(animSlide);

//        //Check if connected
//        if(App.getGoogleApiHelper().isConnected())
//        {
//            //Get google api client
//            googleApiClient = App.getGoogleApiHelper().getGoogleApiClient();
//       System.out.println("Connected !!!!!!!");
//        }

        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_WIDE);
        btnSignIn.setScopes(googleSignInOptions.getScopeArray());
        btnSignIn.setColorScheme(SignInButton.COLOR_DARK);



    }



private void signIn() {
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
    startActivityForResult(signInIntent, REQ_CODE);
}


    private void signOut() {

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            personName = acct.getDisplayName();

            if(acct.getPhotoUrl() != null) {
                personPhotoUrl = acct.getPhotoUrl().toString();
            }
            personEmail = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + personEmail + "photoUrl " + personPhotoUrl);

            if(logout){
                updateUI(false);
                logout = false;
            }
            else {
                updateUI(true);
            }
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.signIn:
                signIn();
                break;

            case R.id.signOut:
                signOut();
                break;


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else if(requestCode == MAPS_ACTIVITY_CODE){


            Toast.makeText(this, "You have been logged out!",
                    Toast.LENGTH_SHORT).show();

            logout = true;
           //  signOut();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);

        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("loading.....");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {

            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
//            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//
//                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                        android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}, 1);
//
//            }
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("activityName","MainActivity");
            intent.putExtra("displayName",personName);
            intent.putExtra("displayEmail", personEmail);
            intent.putExtra("displayPhoto", personPhotoUrl);
          //  getUser();
            startActivityForResult(intent, MAPS_ACTIVITY_CODE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
        }
    }




/**********add new user to db ***********/
            private void addUser() {
                class AddUser extends AsyncTask<String, Void, String> {
                    ProgressDialog loading;
                    RequestHandler rh = new RequestHandler();
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                       // loading = ProgressDialog.show(MainActivity.this, "Uploading...", null, true, true);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                  //      loading.dismiss();
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        HashMap<String, String> data = new HashMap<>();
                        data.put("displayName", personName);
                        data.put("displayEmail", personEmail);
//                        data.put("latitude", "88");
                        String result = rh.sendPostRequest(UPLOAD_ADD_USER, data);
                        return result;
                    }
                }

                AddUser ui = new AddUser();
                ui.execute("add");
            }





            private void getUser() {
                class getUser extends AsyncTask<String, Void, String> {
                    ProgressDialog loading;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        String uri = UPLOAD_GET_USER + "email_id=" + personEmail;
                        // System.out.println(uri + "url");
                        BufferedReader bufferedReader;
                        URL url;
                        try {
                            url = new URL(uri);
                            bufferedReader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
                            String inputLine;
                            StringBuffer response = new StringBuffer();
                            while ((inputLine = bufferedReader.readLine()) != null) {
                                response.append(inputLine);
                            }
                            bufferedReader.close();
                            System.out.println(response.toString());
                            return response.toString();

                        } catch (Exception e) {
                            return null;
                        }
                    }
                }
                getUser gai = new getUser();
                gai.execute("get");
            }


        }
//package com.mobileappclass.travel.Modules;
//
//import android.content.Context;
//import android.location.Location;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationServices;
//
///**
// * Created by Careena on 12/8/16.
// */
//public class GoogleApiHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
//    private static final String TAG = GoogleApiHelper.class.getSimpleName();
//    Context context;
//    GoogleApiClient mGoogleApiClient;
//    GoogleSignInOptions googleSignInOptions;
//
//    public GoogleApiHelper(Context context) {
//        buildGoogleApiClient();
//        connect();
//        this.context = context;
//    }
//
//    public GoogleApiClient getGoogleApiClient() {
//        return this.mGoogleApiClient;
//    }
//
//    public void connect() {
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.connect();
//        }
//    }
//
//    public void disconnect() {
//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()){
//            mGoogleApiClient.disconnect();
//        }
//    }
//
//    public boolean isConnected() {
//        if (mGoogleApiClient != null) {
//            return mGoogleApiClient.isConnected();
//        } else {
//            return false;
//        }
//    }
//
//    private void buildGoogleApiClient() {
//
//        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(context)
//                .addOnConnectionFailedListener(this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
//                .build();
////        mGoogleApiClient = new GoogleApiClient.Builder(context)
////                .addConnectionCallbacks(this)
////                .addOnConnectionFailedListener(this)
////                .addApi(LocationServices.API).build();
//
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        //You are connected do what ever you want
//        //Like i get last known location
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.d(TAG, "onConnectionSuspended: googleApiClient.connect()");
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Log.d(TAG, "onConnectionFailed: connectionResult.toString() = " + connectionResult.toString());
//    }
//}
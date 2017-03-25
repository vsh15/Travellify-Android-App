//package com.mobileappclass.travel.activity;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.LayoutRes;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.NavigationView;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewStub;
//import android.widget.ImageView;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.Unbinder;
////import com.clarifai.android.starter.api.v2.R;
//import com.mikepenz.materialdrawer.Drawer;
//import com.mikepenz.materialdrawer.DrawerBuilder;
//import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
//import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
//import com.mobileappclass.travel.MapsActivity;
//import com.mobileappclass.travel.R;
//import com.tbruyelle.rxpermissions.RxPermissions;
//import rx.functions.Action1;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * A common class to set up boilerplate logic for
// */
//public abstract class BaseActivity extends AppCompatActivity
//       // implements NavigationView.OnNavigationItemSelectedListener
//{
//
//    public ImageView image;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        image = (ImageView) findViewById(R.id.imageView);
//
//        //nav bar
//
////        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
////        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
////                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
////        drawer.setDrawerListener(toggle);
////        toggle.syncState();
////
////        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
////        navigationView.setNavigationItemSelectedListener(this);
//
//    }
//
////
////    @Override
////    public void onBackPressed() {
////        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
////        if (drawer.isDrawerOpen(GravityCompat.START)) {
////            drawer.closeDrawer(GravityCompat.START);
////        } else {
////            super.onBackPressed();
////        }
////    }
////
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.main, menu);
////        return true;
////    }
////
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        // Handle action bar item clicks here. The action bar will
////        // automatically handle clicks on the Home/Up button, so long
////        // as you specify a parent activity in AndroidManifest.xml.
////        int id = item.getItemId();
////
////        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
////
////        return super.onOptionsItemSelected(item);
////    }
////
////    @SuppressWarnings("StatementWithEmptyBody")
////    @Override
////    public boolean onNavigationItemSelected(MenuItem item) {
////        // Handle navigation view item clicks here.
////        int id = item.getItemId();
////
////        if (id == R.id.nav_camera) {
////            Intent intent = new Intent(this, RecognizeConceptsActivity.class);
////            startActivity(intent);
////
////            // Handle the camera action
////        } else if (id == R.id.nav_gallery) {
//////            Intent intent = new Intent();
//////            intent.setType("image/*");
//////            intent.setAction(Intent.ACTION_GET_CONTENT);//
//////            startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
////
////
////        } else if (id == R.id.nav_slideshow) {
////            System.out.println("nav_slideshow");
////
////        } else if (id == R.id.nav_manage) {
////            System.out.println("MAPS");
////
//////            Intent intent = new Intent(this, MapsActivity.class);
//////            intent.putExtra("displayName",personName);
//////            intent.putExtra("displayEmail", email);
//////            startActivityForResult(intent, MAPS_ACTIVITY_CODE);
////
////
////        } else if (id == R.id.nav_share) {
////            System.out.println("share");
////
////        } else if (id == R.id.nav_send) {
////
////        }
////
////        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
////        drawer.closeDrawer(GravityCompat.START);
////        return true;
////    }
//
//    }

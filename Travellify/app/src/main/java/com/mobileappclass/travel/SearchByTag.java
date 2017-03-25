//package com.mobileappclass.travel;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
///**
// * Created by Careena on 12/7/16.
// */
//public class SearchByTag extends AppCompatActivity {
//
//    Button searchByTag;
//    EditText tag;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_images_tag);
//
//        Intent intent = getIntent();
//
//        final double hotSpotLat = intent.getDoubleExtra("hotSpotLat", 0);
//        final double hotSpotLong = intent.getDoubleExtra("hotSpotLong", 0);
//
//        searchByTag = (Button) findViewById(R.id.searchByTag);
//        tag = (EditText) findViewById(R.id.searchTag);
//        searchByTag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("tag received " + tag.getText().toString());
//                Intent intent = new Intent(getApplicationContext(), DownloadImages.class);
//                intent.putExtra("activityName", "TagActivity");
//                intent.putExtra("tag",tag.getText().toString() );
//                intent.putExtra("hotSpotLat", hotSpotLat);
//                intent.putExtra("hotSpotLong", hotSpotLong);
//
//                startActivity(intent);
//
//            }
//        });
//
//
//    }
//
//
//
//
//}

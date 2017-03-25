//package com.mobileappclass.travel;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class ShowImage extends AppCompatActivity {
//    ImageView imageView;
//    TextView textView;
//    ImageItem imageItem;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_show_image);
//        imageView = (ImageView) findViewById(R.id.imageView);
//        textView = (TextView) findViewById(R.id.imageTitle);
//
//        Intent intent = getIntent();
//        if(intent != null){
//            imageView.setImageBitmap((Bitmap) intent.getParcelableExtra("image"));
//            textView.setText(intent.getStringExtra("title"));
//        }
//
//    }
//
//}

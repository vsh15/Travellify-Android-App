//package com.mobileappclass.travel;
//
//import android.app.ProgressDialog;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Base64;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//
//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//public class ViewImage extends AppCompatActivity implements View.OnClickListener{
//
//    private EditText editTextId;
//    private Button buttonGetImage;
//    private ImageView imageView;
//    private final String imageURL = "http://travellify.freevar.com/getImage.php?id=";
//    Bitmap tempMap;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_image);
//
//        editTextId = (EditText) findViewById(R.id.editTextId);
//        buttonGetImage = (Button) findViewById(R.id.buttonGetImage);
//        imageView = (ImageView) findViewById(R.id.imageViewShow);
//
//        buttonGetImage.setOnClickListener(this);
//    }
//
//
//    private void getImage() {
//        String id = editTextId.getText().toString().trim();
//        class GetImage extends AsyncTask<String,Void,Bitmap> {
//
//
//            ImageView bmImage;
//            ProgressDialog loading;
//
//            public GetImage(ImageView bmImage) {
//                this.bmImage = bmImage;
//            }
//
//            @Override
//            protected void onPostExecute(Bitmap bitmap) {
//                super.onPostExecute(bitmap);
//                loading.dismiss();
//
//                bmImage.setImageBitmap(tempMap);
//            }
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(ViewImage.this, "Downloading Image", "Please wait...", true, true);
//            }
//
//            @Override
//            protected Bitmap doInBackground(String... strings) {
//
//                URL url;
//                Bitmap image = null;
//                BufferedReader bufferedReader;
//                try {
//                    url = new URL(imageURL + strings[0]);
//
//                    bufferedReader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
//                    // final InputStream imageContentInputStream = url.openConnection().getInputStream();
//                    // System.out.println(bufferedReader.readLine());
//                    //StringBuffer response = new StringBuffer();
//                    String inputLine;
//                    StringBuffer response = new StringBuffer();
//                    while ((inputLine = bufferedReader.readLine()) != null) {
//                        response.append(inputLine);
//                    }
//                    bufferedReader.close();
//
//                    String encodedImage = response.toString();
//                    System.out.println(encodedImage);
//                    byte[] decodedString = Base64.decode(encodedImage.getBytes(), Base64.DEFAULT);
//                    System.out.println(decodedString);
//                    image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                tempMap =image;
//                return image;
//            }
//
////                String url = imageURL+ strings[0];
////                Bitmap mIcon = null;
////                try {
////                    InputStream in = new java.net.URL(url).openStream();
////                    mIcon = BitmapFactory.decodeStream(in);
////                    System.out.println(mIcon);
////                    System.out.println(in);
////                } catch (Exception e) {
////                    Log.e("Error", e.getMessage());
////                }
////                return mIcon;
////            }
//            //      }
//        }
//
//        GetImage gi = new GetImage(imageView);
//        gi.execute(id);
//    }
//
//    @Override
//    public void onClick(View v) {
//        getImage();
//    }
//}
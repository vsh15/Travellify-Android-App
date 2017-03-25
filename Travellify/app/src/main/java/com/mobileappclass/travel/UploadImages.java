//package com.mobileappclass.travel;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.provider.MediaStore;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Base64;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.util.HashMap;
//
//
//public class UploadImages extends AppCompatActivity implements View.OnClickListener {
//
//
//    public static final String UPLOAD_URL = "http://travellify.freevar.com/uploadGeoImage.php";
//    public static final String UPLOAD_KEY = "image";
//    public static final String UPLOAD_KEY_USER = "user";
//    public static final String TAG = "MY MESSAGE";
//
//    private int PICK_IMAGE_REQUEST = 1;
//
//    private Button buttonChoose;
//    private Button buttonUpload;
//    private Button buttonView;
//    private Button buttonActivity;
//
//    private ImageView imageView;
//
//    private Bitmap bitmap;
//
//    private Uri filePath;
//    int globalCounter = 0;
//
////    lat/lng: (40.49517,-74.4571), lat/lng: (40.49557,-74.45734), lat/lng: (40.49557,-74.45753), lat/lng: (40.49618,-74.45694), lat/lng: (40.49699,-74.4543), lat/lng: (40.49927,-74.44688), lat/lng: (40.50016,-74.44388), lat/lng: (40.50034,-74.44315), lat/lng: (40.50032,-74.44283), lat/lng: (40.50026,-74.44259), lat/lng: (40.50002,-74.44223), lat/lng: (40.4993,-74.44146), lat/lng: (40.49917,-74.44138), lat/lng: (40.49889,-74.44132), lat/lng: (40.49856,-74.44135), lat/lng: (40.49831,-74.44138), lat/lng: (40.49829,-74.44124), lat/lng: (40.49828,-74.44079), lat/lng: (40.49826,-74.44064), lat/lng: (40.49812,-74.44043), lat/lng: (40.49757,-74.4402), lat/lng: (40.49721,-74.44011), lat/lng: (40.49582,-74.43976), lat/lng: (40.49447,-74.43935), lat/lng: (40.49272,-74.43907), lat/lng: (40.49214,-74.43888), lat/lng: (40.49137,-74.43848), lat/lng: (40.4901,-74.43768), lat/lng: (40.48938,-74.43708), lat/lng: (40.4887,-74.43635), lat/lng: (40.48791,-74.43515), lat/lng: (40.48719,-74.43351), lat/lng: (40.48697,-74.43277), lat/lng: (40.48666,-74.4313), lat/lng: (40.48569,-74.42597), lat/lng: (40.48496,-74.42101), lat/lng: (40.48481,-74.42046), lat/lng: (40.48455,-74.42002), lat/lng: (40.4838,-74.41923), lat/lng: (40.48342,-74.41875), lat/lng: (40.4831,-74.41823), lat/lng: (40.48271,-74.4172), lat/lng: (40.48247,-74.41532), lat/lng: (40.48235,-74.41405), lat/lng: (40.48222,-74.4135), lat/lng: (40.48203,-74.41299), lat/lng: (40.48177,-74.41252), lat/lng: (40.48147,-74.41212), lat/lng: (40.48112,-74.4118), lat/lng: (40.48036,-74.41136), lat/lng: (40.47839,-74.41077), lat/lng: (40.47771,-74.41058), lat/lng: (40.47745,-74.41052), lat/lng: (40.47718,-74.41057), lat/lng: (40.47712,-74.41059), lat/lng: (40.47707,-74.41061), lat/lng: (40.47699,-74.41068), lat/lng: (40.47688,-74.41087), lat/lng: (40.47688,-74.41123), lat/lng: (40.47698,-74.41141), lat/lng: (40.47713,-74.41151), lat/lng: (40.47728,-74.41151), lat/lng: (40.47743,-74.41144), lat/lng: (40.47759,-74.41112), lat/lng: (40.47776,-74.41004), lat/lng: (40.47791,-74.40871), lat/lng: (40.47825,-74.40663), lat/lng: (40.47844,-74.40548), lat/lng: (40.47845,-74.40509), lat/lng: (40.47835,-74.40416), lat/lng: (40.47821,-74.40365), lat/lng: (40.47796,-74.40313), lat/lng: (40.47763,-74.40285), lat/lng: (40.47745,-74.40281), lat/lng: (40.4773,-74.40284), lat/lng: (40.477,-74.4031), lat/lng: (40.47689,-74.40333), lat/lng: (40.47683,-74.40368), lat/lng: (40.47688,-74.40406), lat/lng: (40.47706,-74.40443), lat/lng: (40.47727,-74.40464), lat/lng: (40.47755,-74.40478), lat/lng: (40.47788,-74.40477), lat/lng: (40.47808,-74.40469), lat/lng: (40.48037,-74.40317), lat/lng: (40.48055,-74.40306), lat/lng: (40.48064,-74.40308), lat/lng: (40.48224,-74.40194), lat/lng: (40.48369,-74.40077), lat/lng: (40.48508,-74.39953), lat/lng: (40.48617,-74.39847), lat/lng: (40.48746,-74.39706), lat/lng: (40.4882,-74.39622), lat/lng: (40.49023,-74.39395), lat/lng: (40.49604,-74.38745), lat/lng: (40.50528,-74.37708), lat/lng: (40.51251,-74.36899), lat/lng: (40.51425,-74.36683), lat/lng: (40.51524,-74.36541), lat/lng: (40.51642,-74.36362), lat/lng: (40.51712,-74.3624), lat/lng: (40.51811,-74.36063), lat/lng: (40.51894,-74.3589), lat/lng: (40.5199,-74.35669), lat/lng: (40.52133,-74.35294), lat/lng: (40.52312,-74.34832), lat/lng: (40.52474,-74.34417), lat/lng: (40.52528,-74.34284), lat/lng: (40.52621,-74.34074), lat/lng: (40.5271,-74.33898), lat/lng: (40.52752,-74.33823), lat/lng: (40.5285,-74.33662), lat/lng: (40.53207,-74.3313), lat/lng: (40.53281,-74.33018), lat/lng: (40.53365,-74.32891), lat/lng: (40.53488,-74.32687), lat/lng: (40.53624,-74.32425), lat/lng: (40.53725,-74.32198), lat/lng: (40.53831,-74.31923), lat/lng: (40.53942,-74.31569), lat/lng: (40.53998,-74.31367), lat/lng: (40.5405,-74.31146), lat/lng: (40.54073,-74.31024), lat/lng: (40.54131,-74.30618), lat/lng: (40.54183,-74.30193), lat/lng: (40.54233,-74.29856), lat/lng: (40.54296,-74.29429), lat/lng: (40.54334,-74.29236), lat/lng: (40.54369,-74.29084), lat/lng: (40.54448,-74.28803), lat/lng: (40.54535,-74.2854), lat/lng: (40.54583,-7
////            12-01 01:09:24.873 4736-4736/com.mobileappclass.travel V/Testing: Entered L
//
//    //    40.49827, -74.45688
////            40.703291, -74.149733
////            40.685831,  -74.099755
////
//    double[] latitudes = {40.49827, 40.703291, 40.685831
//    };
//    //{40.49517,40.49557,40.49557,40.49618,40.49699,40.49927,40.50016,40.50034,40.50032,40.50026,40.50002,40.4993};
//    double[] longitudes = {-74.45688, -74.149733, -74.099755};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload_images);
//
//        buttonChoose = (Button) findViewById(R.id.buttonChoose);
//        buttonUpload = (Button) findViewById(R.id.buttonUpload);
////        buttonView = (Button) findViewById(R.id.buttonViewImage);
////        buttonActivity = (Button) findViewById(R.id.nextActivity);
//
//        imageView = (ImageView) findViewById(R.id.imageView);
//
//        buttonChoose.setOnClickListener(this);
//        buttonUpload.setOnClickListener(this);
////        buttonView.setOnClickListener(this);
////        buttonActivity.setOnClickListener(this);
//    }
//
//    private void showFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//            filePath = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                imageView.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public String getStringImage(Bitmap bmp) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] imageBytes = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        return encodedImage;
//    }
//
//    private void uploadImage() {
//        class UploadImage extends AsyncTask<Bitmap, Void, String> {
//
//            ProgressDialog loading;
//            RequestHandler rh = new RequestHandler();
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(UploadImages.this, "Uploading...", null, true, true);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            protected String doInBackground(Bitmap... params) {
//
//
//                Bitmap bitmap = params[0];
//
//
//                String uploadImage = getStringImage(bitmap);
//
//
//                HashMap<String, String> data = new HashMap<>();
//
//                data.put(UPLOAD_KEY, uploadImage);
//                data.put("latitude", "88");
//                data.put("longitude", String.valueOf(longitudes[globalCounter]));
//                data.put("user_id", String.valueOf(globalCounter));
//                data.put("attributes", "landscape");
//                globalCounter++;
//
//                String result = rh.sendPostRequest(UPLOAD_URL, data);
//
//                return result;
//            }
//        }
//
//        UploadImage ui = new UploadImage();
//        ui.execute(bitmap);
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v == buttonChoose) {
//            showFileChooser();
//        }
//        if (v == buttonUpload) {
//            uploadImage();
//        }
//        if (v == buttonView) {
//            viewImage();
//        }
//        if (v == buttonActivity) {
//            newActivity();
//        }
//    }
//
//    private void viewImage() {
//        System.out.println("intent .......");
//        startActivity(new Intent(this, ViewImage.class));
//    }
//
//    public void newActivity() {
//        startActivity(new Intent(this, DownloadImages.class));
//    }
//}
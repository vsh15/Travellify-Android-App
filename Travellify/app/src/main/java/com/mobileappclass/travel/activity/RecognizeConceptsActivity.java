package com.mobileappclass.travel.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import butterknife.BindView;
import butterknife.OnClick;
import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

import com.mobileappclass.travel.ClarifaiUtil;
import com.mobileappclass.travel.MapsActivity;
import com.mobileappclass.travel.Modules.AttributeBean;
import com.mobileappclass.travel.R;
import com.mobileappclass.travel.RequestHandler;
import com.mobileappclass.travel.adapter.RecognizeConceptsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public final class RecognizeConceptsActivity extends AppCompatActivity implements View.OnClickListener
        //  extends BaseActivity
{




    public static final String ALL_USERS_URL = " http://travellify.freevar.com/getAllUsers.php";
    //public static final int PICK_IMAGE = 100;
    public static final String UPLOAD_URL = "http://travellify.freevar.com/uploadGeoImage.php";
    public static final String UPLOAD_KEY = "image";
    public static final String UPLOAD_KEY_LAT = "latitude";
    public static final String UPLOAD_KEY_LONG = "longitude";
    public static final String UPLOAD_KEY_USER = "user_id";

    public String[] upload_key_attributes = {"attribute1","attribute2","attribute3","attribute4","attribute5"};
    public String[] upload_key_friends = {"friend1","friend2","friend3","friend4","friend5"};

    /********zoom**********/

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;


    public static final String TAG = "MY MESSAGE";

    private int PICK_IMAGE_REQUEST = 1;
    static final int CAMERA_REQUEST = 989;

    //    private Button buttonChoose;
//    private Button buttonUpload;
//    private Button buttonCamera;
    private String latitude;
    private String longitude;

    private Bitmap bitmap;
    private Bitmap uploadBitMap;

    private Uri filePath;
    // the list of results that were returned from the API
    GridView resultsList;
    ArrayList<String> allUsers;
    ArrayAdapter<String> usersAdapter;

    // the view where the image the user selected is displayed
    ImageView imageView;

    //Tag list
    List<Concept> tagList;
    ArrayList<String> tagListToSave;
    List<String> uploadTags;
    ArrayList<AttributeBean> attributeBeans;
    ArrayList<String> sharedFriend;
    int friendCount = 0;
    int uploadTagCount = 0;

    String currentUserEmail;

    // switches between the text prompting the user to hit the FAB, and the loading spinner
   // ViewSwitcher switcher;

    EditText editTextFriend;

    private AutoCompleteTextView actv;
    byte [] byteArray;


    // the FAB that the user clicks to select an image
    Button buttonAdd;
    Button buttonChoose;
    Button buttonCamera;
    Button buttonUpload;
    Button buttonShare;
    Button buttonShowGrid;
    @Nullable
    private ClarifaiClient client;

    @NonNull private RecognizeConceptsAdapter adapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_recognize);




            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        Intent intent = getIntent();
        if(intent != null){
            latitude = intent.getStringExtra("latitude");
            longitude = intent.getStringExtra("longitude");
        }

        editTextFriend = (EditText) findViewById(R.id.editFriend);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonCamera = (Button) findViewById(R.id.buttonCamera);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonShare = (Button) findViewById(R.id.share);
        buttonShowGrid = (Button) findViewById(R.id.showGrid);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);

        ArrayList<String> names = new ArrayList<>();
        names.add("Careena");
        names.add("Vishal");
        names.add("Vikti");
        names.add("Aneesh");

      //  String[] names = {"Vikti", "Vishal", "Vinayak", "Varun", "Vardhan"};


        MapsActivity mapsActivity = new MapsActivity();
        currentUserEmail = mapsActivity.displayEmail;
        allUsers = new ArrayList<>();

        getAllUsers();
        usersAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,allUsers);
        actv.setAdapter(usersAdapter);

        buttonChoose.setOnClickListener(this);
        buttonCamera.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        buttonShare.setOnClickListener(this);
        buttonShowGrid.setOnClickListener(this);

        imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.image_placehoder2));
       // switcher = (ViewSwitcher) findViewById(R.id.switcher);
        resultsList = (GridView) findViewById(R.id.resultsList);
        uploadTags = new ArrayList<String>();
        sharedFriend = new ArrayList<>();
        attributeBeans = new ArrayList<>();



        tagListToSave = new ArrayList<>();
        /***on saved instance**/
        if(savedInstanceState != null) {

            byte[] byteArr = savedInstanceState.getByteArray("bitmap");
            byteArray = byteArr;
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length);

            imageView.setImageBitmap(bmp);

            ArrayList<String> tags = savedInstanceState.getStringArrayList("tagListToSave");

            attributeBeans = (ArrayList<AttributeBean>) savedInstanceState.getSerializable("attributeBeans");

            if(attributeBeans != null){
                adapter = new RecognizeConceptsAdapter(RecognizeConceptsActivity.this,attributeBeans);
                resultsList.setAdapter(adapter);
//                for (int i=0; i <attributeBeans.size();i++) {
//                if (attributeBeans.get(i).isSelected()) {
//                    resultsList.getChildAt(i).setBackgroundColor(getApplicationContext().getResources().getColor(R.color.tagNotSelected));
//                    } else {
//                    resultsList.getChildAt(i).setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
//                }
//            }

            }


        }




        resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(attributeBeans.get(position).isSelected()){
                    attributeBeans.get(position).setSelected(false);
                    uploadTagCount--;
                    resultsList.getChildAt(position).setBackgroundColor(getApplicationContext().getResources().getColor(R.color.tagNotSelected));
                   // resultsList.getChildAt(position).findViewById(R.id.label).setBackgroundColor(getApplicationContext().getResources().getColor(R.color.tagNotSelected));
                   // resultsList.getChildAt(position).setBackgroundColor(getApplicationContext().getResources().getColor(R.color.tagBackColor));
                    System.out.println("Unselected " + attributeBeans.get(position).getAttribute() + " Total = "  + uploadTagCount);
                }
                else if(uploadTagCount == 5){
                    Toast.makeText(RecognizeConceptsActivity.this, "You can select upto 5 tags", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadTags.add(attributeBeans.get(position).getAttribute());
                    attributeBeans.get(position).setSelected(true);
                    uploadTagCount++;

                    resultsList.getChildAt(position).setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
                    System.out.println("Selected " + attributeBeans.get(position).getAttribute() + " Total = "  + uploadTagCount);
                }
            }
        });


        client = new ClarifaiBuilder(getString(R.string.clarifai_id), getString(R.string.clarifai_secret))
                // Optionally customize HTTP client via a custom OkHttp instance
                .client(new OkHttpClient.Builder()
                                .readTimeout(30, TimeUnit.SECONDS) // Increase timeout for poor mobile networks
                                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                                    @Override public void log(String logString) {
                                        Timber.e(logString);
                                    }
                                }).setLevel(HttpLoggingInterceptor.Level.BODY))
                                .build()
                )
                .buildSync(); // use build() instead to get a Future<ClarifaiClient>, if you don't want to block this thread
        super.onCreate(savedInstanceState);


    }



//    @Override
//        protected void onStart() {
//        super.onStart();
//        // resultsList.setLayoutManager(new LinearLayoutManager(this));
////        resultsList.setAdapter(adapter);
//    }


    private void onImagePicked(@NonNull final byte[] imageBytes) {
        // Now we will upload our image to the Clarifai API
        setBusy(true);

        // Make sure we don't show a list of old concepts while the image is being uploaded
        //adapter.setData(Collections.<Concept>emptyList());

        adapter = new RecognizeConceptsAdapter(RecognizeConceptsActivity.this,Collections.<AttributeBean>emptyList() );
        new AsyncTask<Void, Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>>() {


            @Override
            protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(Void... params) {
                // The default Clarifai model that identifies concepts in images
                final ConceptModel generalModel = clarifaiClient().getDefaultModels().generalModel();

                // Use this model to predict, with the image that the user just selected as the input
                return generalModel.predict()
                        .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(imageBytes)))
                        .executeSync();
            }

            @Override protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
                setBusy(false);
                if (!response.isSuccessful()) {
                    showErrorSnackbar(R.string.error_while_contacting_api);
                    return;
                }
                final List<ClarifaiOutput<Concept>> predictions = response.get();
                if (predictions.isEmpty()) {
                    showErrorSnackbar(R.string.no_results_from_api);
                    return;
                }

                tagList = predictions.get(0).data();

                for(int i=0; i <tagList.size(); i++){
                    attributeBeans.add(new AttributeBean(tagList.get(i).name(),false));
                }
                adapter = new RecognizeConceptsAdapter(RecognizeConceptsActivity.this,attributeBeans);
                resultsList.setAdapter(adapter);


                byteArray = imageBytes;
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));



            }

            private void showErrorSnackbar(@StringRes int errorString) {
                Toast.makeText(RecognizeConceptsActivity.this, errorString, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }



    private void setBusy(final boolean busy) {
//        runOnUiThread(new Runnable() {
//            @Override public void run() {
//                switcher.setDisplayedChild(busy ? 1 : 0);
//                imageView.setVisibility(busy ? GONE : VISIBLE);
//
//                buttonChoose.setEnabled(!busy);
//            }
//        });
    }

    @NonNull
    public ClarifaiClient clarifaiClient() {
        final ClarifaiClient client = this.client;
        if (client == null) {
            throw new IllegalStateException("Cannot use Clarifai client before initialized");
        }
        return client;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadBitMap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
                System.out.println("choose image bit map");
                System.out.println(uploadBitMap.toString());
                System.out.println(bitmap.toString());
                imageView.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap cpyMap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
                cpyMap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byteArray = stream.toByteArray() ;
            } catch (IOException e) {
                e.printStackTrace();
            }
            final byte[] imageBytes = ClarifaiUtil.retrieveSelectedImage(this, data);

            if (imageBytes != null) {
                onImagePicked(imageBytes);
                System.out.println("....515 " + imageBytes);
            }
            imageView.setImageBitmap(bitmap);
        }

       else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap  = (Bitmap) data.getExtras().get("data");
            uploadBitMap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
            System.out.println("upload image bit map");
            System.out.println(uploadBitMap.toString());
            imageView.setImageBitmap(bitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap cpyMap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
            cpyMap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray() ;
            final byte[] imageBytes = ClarifaiUtil.retrieveCameraImage(this, bitmap);
            if (imageBytes != null) {
                onImagePicked(imageBytes);
            }
            imageView.setImageBitmap(uploadBitMap);
        }


//        int bytes = bitmap.getByteCount();
////or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
////int bytes = b.getWidth()*b.getHeight()*4;
//
//        ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
//        bitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
//
//        final byte[] array = buffer.array();


        // onImagePicked(imageBytes);

    }




    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    void uploadImage() {
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RecognizeConceptsActivity.this, "Uploading...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                resetAllFields();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String, String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);
                data.put(UPLOAD_KEY_LAT, latitude);
                data.put(UPLOAD_KEY_LONG, longitude);
                data.put(UPLOAD_KEY_USER, currentUserEmail);

                int count = 0;
                int i;
                for(i=0; i < attributeBeans.size(); i++){
                    if(attributeBeans.get(i).isSelected()) {
                        data.put(upload_key_attributes[count], attributeBeans.get(i).getAttribute());
                        count++;
                    }
                }
                for(i=0; i < sharedFriend.size(); i++){
                    data.put(upload_key_friends[i],sharedFriend.get(i));
                }


//
//                for(int i =0; i < uploadTags.size(); i++){
//
//                    data.put(upload_key_attributes[i],uploadTags.get(i));
//                }



                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(uploadBitMap);
    }

    @Override
    public void onClick(View v) {

        if (v == imageView){


        }
        if (v == buttonShare) {
            actv.setVisibility(View.VISIBLE);
            buttonAdd.setVisibility(View.VISIBLE);
            resultsList.setVisibility(View.GONE);

            buttonShare.setVisibility(View.GONE);
            buttonShowGrid.setVisibility(View.VISIBLE);

        }
        if (v == buttonShowGrid) {
            actv.setVisibility(View.GONE);
            buttonAdd.setVisibility(View.GONE);
            resultsList.setVisibility(View.VISIBLE);

            buttonShare.setVisibility(View.VISIBLE);
            buttonShowGrid.setVisibility(View.GONE);


        }
        if (v == buttonChoose) {
            showFileChooser();
        }
        if (v == buttonUpload) {
            uploadImage();
        }
        if (v == buttonCamera) {
            startCamera();
        }
        if(v == buttonAdd){

            if(friendCount < 5) {
                if (!actv.getText().toString().equals("")) {
                   // sharedFriend.add(editTextFriend.getText().toString());
                    sharedFriend.add(actv.getText().toString());
                    Toast.makeText(RecognizeConceptsActivity.this, "Shared!", Toast.LENGTH_SHORT).show();
                    actv.setText("");
                    friendCount++;
                } else {
                    Toast.makeText(RecognizeConceptsActivity.this, "Please enter user name!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(RecognizeConceptsActivity.this, "Cannot select more than 5 friends", Toast.LENGTH_SHORT).show();

            }
        }
    }


    void startCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }


    public void resetAllFields(){
        tagList.clear();
        uploadTags.clear();
        sharedFriend.clear();
        attributeBeans.clear();
        friendCount = 0;
        uploadTagCount = 0;
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.image_placehoder2));
        actv.setVisibility(View.GONE);
        buttonAdd.setVisibility(View.GONE);
        resultsList.setVisibility(View.VISIBLE);

        buttonShare.setVisibility(View.VISIBLE);
        buttonShowGrid.setVisibility(View.GONE);

    }




    /*****************Zoom images***********/


    /****************get all users from db *************/


    private static final String JSON_ARRAY = "result";
    private static final String NAME_URL = "name";
    private static final String EMAIL_URL = "email";

    private JSONArray arrayUsers = null;
    private String usersJSON;

    private void extractJSON() {
        try {
            JSONObject jsonObject = new JSONObject(usersJSON);
            arrayUsers = jsonObject.getJSONArray(JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < arrayUsers.length(); i++) {
            try {
                JSONObject jsonObject = arrayUsers.getJSONObject(i);
                String name = jsonObject.getString(NAME_URL);
                String email = jsonObject.getString(EMAIL_URL);
                System.out.println("all users " + name + "   " + email);
                allUsers.add(email);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




    private void getAllUsers() {
        class GetAllUsers extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.trim().equals("FAIL899")) {
                    System.out.println("No users");
                }
                else{
                    usersJSON = s;
                    extractJSON();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = ALL_USERS_URL;
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

                    String encodedString = response.toString();
                    System.out.println(encodedString);

                    return encodedString;

                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetAllUsers gai = new GetAllUsers();
        gai.execute(ALL_USERS_URL);
    }




    /***********save instance **************/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(attributeBeans != null){
            outState.putSerializable("attributeBeans", attributeBeans);
        }

        if(byteArray != null){
            outState.putByteArray("bitmap", byteArray);
        }
        if(tagList != null){

            for(int i = 0; i< tagList.size(); i++){
                tagListToSave.add(tagList.get(i).name());
            }
            outState.putStringArrayList("tagListToSave", tagListToSave);
            System.out.println("saving tags");
        }
        if (uploadTags != null){
            outState.putStringArrayList("uploadTags", (ArrayList<String>)uploadTags);
        }

    }
}




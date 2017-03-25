package com.mobileappclass.travel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mobileappclass.travel.Modules.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Careena
 */
public class PlaceDetails extends AppCompatActivity {
    private List<Place> spots = new ArrayList<Place>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);


        TextView typename = (TextView) findViewById(R.id.typename);
        ImageView img1 = (ImageView) findViewById(R.id.item1);
        ImageView img2 = (ImageView) findViewById(R.id.item2);
        ImageView img3 = (ImageView) findViewById(R.id.item3);
        ImageView img4 = (ImageView) findViewById(R.id.item4);

        TextView spotname = (TextView) findViewById(R.id.spotname);
        TextView address = (TextView) findViewById(R.id.address);
        TextView open = (TextView) findViewById(R.id.open);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);





        Bundle extras = getIntent().getExtras();

        String type = extras.getString("type");



        if(type.equals("restaurant")){


            typename.setText("Restaurant");
            img1.setImageResource(R.drawable.pizza);
            img2.setImageResource(R.drawable.pumpkin);
            img3.setImageResource(R.drawable.burger);
            img4.setImageResource(R.drawable.doughnut);

        }

        else if(type.equals("liquor")){


            typename.setText("Liquor Store");
            img1.setImageResource(R.drawable.beer1);
            img2.setImageResource(R.drawable.wine);
            img3.setImageResource(R.drawable.mojito);
            img4.setImageResource(R.drawable.icetea);

        }

        else if(type.equals("gas_station")){


            typename.setText("Gas Station");
            img1.setImageResource(R.drawable.fuel);
            img2.setImageResource(R.drawable.gaslevel);
            img3.setImageResource(R.drawable.fuel1);
            img4.setImageResource(R.drawable.gasoline);

        }
        else if(type.equals("hospital")){


            typename.setText("Hospital");
            img1.setImageResource(R.drawable.firstaidkit);
            img2.setImageResource(R.drawable.patch);
            img3.setImageResource(R.drawable.cardiogram);
            img4.setImageResource(R.drawable.ambulance);

        }



        String value1 = extras.getString("name");
        spotname.setText(value1);

        String value2 = extras.getString("address");
        address.setText(value2);

        boolean value3 = extras.getBoolean("open");
        open.setText(String.valueOf(value3));


        Double value4 = extras.getDouble("rating");
        ratingBar.setRating(Float.parseFloat(String.valueOf(value4)));



        // String value2 = extras.getString("Location");
    }
}

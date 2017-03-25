package com.mobileappclass.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Careena on 11/28/16.
 */
public class CustomGrid extends BaseAdapter {
    private Context mContext;
    // private final String[] web;
    private final ArrayList<ImageItem> Imageid;

    public CustomGrid(Context c, ArrayList<ImageItem> Imageid) {
        mContext = c;
        this.Imageid = Imageid;
        // this.web = web;
    }

    @Override
    public int getCount() {
        return Imageid.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(290, 290));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(Imageid.get(position).getImage());

        return imageView;
    }
}

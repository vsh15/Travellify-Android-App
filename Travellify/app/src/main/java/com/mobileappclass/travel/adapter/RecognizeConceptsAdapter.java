package com.mobileappclass.travel.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import clarifai2.dto.prediction.Concept;

import com.mobileappclass.travel.ImageItem;
import com.mobileappclass.travel.Modules.AttributeBean;
import com.mobileappclass.travel.R;

import java.util.ArrayList;
import java.util.List;

public class RecognizeConceptsAdapter extends BaseAdapter {

    private List<AttributeBean> concepts = new ArrayList<>();
    private Context context;
    TextView textView;

    public RecognizeConceptsAdapter(Context c, List<AttributeBean> concepts) {
        this.concepts = concepts;
        this.context = c;
        notifyDataSetChanged();
       // return this;
    }

    @Override
    public int getCount() {
        return concepts.size();
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

        final AttributeBean concept = concepts.get(position);

        if (convertView == null) {
            textView = new TextView(context);

        } else {
            textView = (TextView) convertView;
        }
      //  textView.setText(concept.name() != null ? concept.name() : concept.id());

        textView.setText(concept.getAttribute());
        textView.setBackgroundColor(context.getResources().getColor(R.color.tagNotSelected));
        textView.setTextColor(context.getResources().getColor(R.color.white));
        textView.setTextSize(context.getResources().getDimension(R.dimen.textsize1));
        textView.setPadding(1, 1, 1, 1);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }



}

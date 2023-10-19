package com.eagleapps.beautycam.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;

import com.bumptech.glide.Glide;
import com.eagleapps.beautycam.otherView.SquaredImageView;

import java.util.ArrayList;

public class MyCreationAdapt extends BaseAdapter {
    private Context context;
    private ArrayList<String> listItem;

    public MyCreationAdapt(ArrayList<String> arrayList, Context context2) {
        this.listItem = arrayList;
        this.context = context2;
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public int getCount() {
        return this.listItem.size();
    }

    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        SquaredImageView squaredImageView;
        if (view == null) {
            squaredImageView = new SquaredImageView(this.context);
            squaredImageView.setScaleType(ScaleType.CENTER_CROP);
            squaredImageView.setLayoutParams(new LayoutParams(-1, -1));
            squaredImageView.setPadding(3, 3, 3, 3);
        } else {
            squaredImageView = (SquaredImageView) view;
        }
        Glide.with(this.context).load((String) this.listItem.get(i)).into((ImageView) squaredImageView);
        return squaredImageView;
    }
}

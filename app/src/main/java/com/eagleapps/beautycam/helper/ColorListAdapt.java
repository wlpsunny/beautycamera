package com.eagleapps.beautycam.helper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.eagleapps.beautycam.R;

public class ColorListAdapt extends Adapter<ColorListAdapt.ViewHolder> {
    public int selected = 0;
    private boolean IsStickerSeleted = false;
    private int[] colorSet;

    public ColorListAdapt(Context context, int[] iArr, boolean z) {
        this.colorSet = iArr;
        this.IsStickerSeleted = z;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.circle_imageview, null));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        if (this.IsStickerSeleted) {
            viewHolder.imageView.paint.setColor(Color.parseColor("#00ffffff"));
            viewHolder.imageView.setBackgroundResource(this.colorSet[i]);
        } else {
            viewHolder.imageView.paint.setColor(this.colorSet[i]);
        }
        viewHolder.imageView.invalidate();
        if (i == this.selected) {
            viewHolder.selectedImageView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.selectedImageView.setVisibility(View.GONE);
        }
        viewHolder.filterRoot.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ColorListAdapt.this.selected != i) {
                    int access$000 = ColorListAdapt.this.selected;
                    ColorListAdapt.this.selected = i;
                    ColorListAdapt.this.notifyItemChanged(access$000);
                    ColorListAdapt.this.notifyItemChanged(i);
                }
            }
        });
    }

    public int getItemCount() {
        return this.colorSet.length;
    }

    public static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        public RelativeLayout filterRoot;
        public CircleView imageView;
        public ImageView selectedImageView;

        public ViewHolder(View view) {
            super(view);
            this.imageView = (CircleView) view.findViewById(R.id.color_picker);
            this.selectedImageView = (ImageView) view.findViewById(R.id.imageSelection);
            this.filterRoot = (RelativeLayout) view.findViewById(R.id.filterRoot);
        }
    }
}

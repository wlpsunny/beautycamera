package com.eagleapps.beautycam.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.eagleapps.beautycam.R;
import com.eagleapps.beautycam.otherView.ColorCircleDraw;
import com.eagleapps.beautycam.otherView.DisplayUtil;

public class ColorsAdapt2 extends Adapter<ColorsAdapt2.ViewHolder> implements OnClickListener {
    private Context context;
    private String[] listItem;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private int margin;
    private int width;

    public ColorsAdapt2(String[] strArr, Context context2, int i) {
        this.listItem = strArr;
        this.context = context2;
        this.width = i;
        this.margin = DisplayUtil.dip2px(context2, 10.0f);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item_gallery, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        inflate.setOnClickListener(this);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        int i2 = this.width - (this.margin * 2);
        LayoutParams layoutParams = new LayoutParams(i2, i2);
        int i3 = this.margin;
        layoutParams.setMargins(i3, i3, 0, i3);
        viewHolder.image.setLayoutParams(layoutParams);
        if (VERSION.SDK_INT < 16) {
            viewHolder.image.setBackgroundDrawable(new ColorCircleDraw(Color.parseColor(this.listItem[i])));
        } else {
            viewHolder.image.setBackground(new ColorCircleDraw(Color.parseColor(this.listItem[i])));
        }
        viewHolder.itemView.setTag(this.listItem[i]);
    }

    public void onClick(View view) {
        OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = this.mOnItemClickListener;
        if (onRecyclerViewItemClickListener != null) {
            onRecyclerViewItemClickListener.onItemClick(view, (String) view.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.mOnItemClickListener = onRecyclerViewItemClickListener;
    }

    public int getItemCount() {
        return this.listItem.length;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String str);
    }

    public static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        public ImageView image;

        public ViewHolder(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.item_sticker);
        }
    }
}

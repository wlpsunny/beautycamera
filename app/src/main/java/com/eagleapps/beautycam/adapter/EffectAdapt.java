package com.eagleapps.beautycam.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.eagleapps.beautycam.R;
import com.eagleapps.beautycam.bitmap.BitmapLoad;
import com.eagleapps.beautycam.otherView.DisplayUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EffectAdapt extends Adapter<EffectAdapt.ViewHolder> implements OnClickListener {
    private Context context;
    private List<String> listColor;
    private String[] listItem;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private Typeface typeface;
    private int width;

    public EffectAdapt(String[] strArr, Context context2, int i) {
        this.listItem = strArr;
        this.context = context2;
        this.width = i - DisplayUtil.dip2px(context2, 15.0f);
        this.typeface = Typeface.createFromAsset(this.context.getAssets(), "fonts/rubik_regular.ttf");
        this.listColor = new ArrayList(Arrays.asList(new String[]{"#5508c8", "#3f97e9", "#e4c120", "#c82970", "#e49820", "#949494", "#e46f20", "#ba0bc0", "#3fa3c3", "#3fc3b2", "#3fc390", "#d2434d", "#7fa31e", "#3f6ec3", "#8f08c8", "#a3951e", "#c00b98", "#1ea340"}));
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item_effect, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        inflate.setOnClickListener(this);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.image.setImageBitmap(BitmapLoad.loadFromAsset(this.context, new int[]{200, 200}, this.listItem[i]));
        int i2 = this.width;
        LayoutParams layoutParams = new LayoutParams(i2, i2 / 4);
        layoutParams.addRule(12);
//        viewHolder.rlcontext.setLayoutParams(layoutParams);
//        RelativeLayout relativeLayout = viewHolder.rlcontext;
        List<String> list = this.listColor;
//        relativeLayout.setBackgroundColor(Color.parseColor((String) list.get(i % list.size())));
        viewHolder.itemView.setTag(this.listItem[i]);
        if (i == 0) {
//            viewHolder.text.setText("NO");
        } else {
//            TextView textView = viewHolder.text;
            StringBuilder sb = new StringBuilder();
            sb.append(ExifInterface.LONGITUDE_EAST);
            sb.append(i);
//            textView.setText(sb.toString());
        }
//        viewHolder.text.setTypeface(this.typeface);
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
//        public RelativeLayout rlcontext;
//        public TextView text;

        public ViewHolder(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.image);
//            this.rlcontext = (RelativeLayout) view.findViewById(R.id.rlcontext);
//            this.text = (TextView) view.findViewById(R.id.text);
        }
    }
}

package com.eagleapps.beautycam.act;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.eagleapps.beautycam.R;
import com.eagleapps.beautycam.adapter.MyCreationAdapt;
import com.eagleapps.beautycam.otherView.Utils;
import com.eagleapps.beautycam.remote.SupporterClass;
import com.yalantis.ucrop.UCrop.Options;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MyCreationAct extends BaseAct {
    public static File[] listFile;
    public ArrayList<String> images;
    private ImageView ivnophoto;
    private int mToolbarColor;
    private String mToolbarTitle;
    private int mToolbarWidgetColor;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_creation);
        setupViews(getIntent());
        RelativeLayout adViewBanner = findViewById(R.id.rel_banner_ads);
        SupporterClass.loadBannerAds(adViewBanner, MyCreationAct.this);
        images = getAllShownImagesPath();
        ivnophoto = (ImageView) findViewById(R.id.ivnophoto);
        if (images.isEmpty()) {
            ivnophoto.setVisibility(View.VISIBLE);
            return;
        }
        ivnophoto.setVisibility(View.GONE);
        GridView gridView = (GridView) findViewById(R.id.gvalbum);
        gridView.setAdapter(new MyCreationAdapt(images, this));
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (images != null && !images.isEmpty()) {
                    Intent intent = new Intent().setClass(MyCreationAct.this, ShareImageAct.class);
                    intent.setData(Uri.parse((String) images.get(i)));
                    startActivity(intent);
                }
            }
        });
    }

    private void setupViews(Intent intent) {
        mToolbarColor = intent.getIntExtra(Options.EXTRA_TOOL_BAR_COLOR, ContextCompat.getColor(this, R.color.white));
        mToolbarWidgetColor = intent.getIntExtra(Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, ContextCompat.getColor(this, R.color.black));
        String stringExtra = intent.getStringExtra(Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR);
        mToolbarTitle = stringExtra;
        mToolbarTitle = !TextUtils.isEmpty(stringExtra) ? mToolbarTitle : getResources().getString(R.string.mycreation);
        setupAppBar();
    }

    private void setupAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textView.setTextColor(mToolbarWidgetColor);
        textView.setText(mToolbarTitle);
        textView.setTypeface(Typeface.createFromAsset(getAssets(), Utils.FONT_MAIN));
        Drawable mutate = ContextCompat.getDrawable(this, R.drawable.ic_back).mutate();
//        mutate.setColorFilter(mToolbarWidgetColor, Mode.SRC_ATOP);
        toolbar.setNavigationIcon(mutate);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private ArrayList<String> getAllShownImagesPath() {
        ArrayList<String> arrayList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
        sb.append("/Beauty Camera");
        File file = new File(sb.toString());
        if (file.isDirectory()) {
            listFile = file.listFiles();

            for (File fileNext : new File(sb.toString()).listFiles()) {
                try {
                    if (fileNext.isFile()) {
                        arrayList.add(fileNext.getAbsolutePath());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Collections.reverse(arrayList);
        }
        return arrayList;
    }
}

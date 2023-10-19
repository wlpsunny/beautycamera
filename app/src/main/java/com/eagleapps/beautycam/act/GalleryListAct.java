package com.eagleapps.beautycam.act;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;


import com.eagleapps.beautycam.R;
import com.eagleapps.beautycam.frag.PhotoFragment;

import java.util.ArrayList;


public class GalleryListAct extends FragmentActivity implements OnClickListener {
    private static GalleryListAct galleryActivity;
    public Activity mContext;
    public TextView tv_title;
    private ImageView iv_close;
    private long mLastClickTime = 0;

    public static GalleryListAct getGalleryActivity() {
        return galleryActivity;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_gallery_album);
        galleryActivity = this;
        mContext = this;
        initView();
        initViewAction();

    }
    private void initView() {
        tv_title =  findViewById(R.id.tv_title);
        iv_close =  findViewById(R.id.iv_close);
    }

    private void initViewAction() {
        iv_close.setOnClickListener(this);
        iv_close.setColorFilter(R.color.black);
        updateFragment(PhotoFragment.newInstance());
    }

    public void updateFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.simpleFrameLayout, fragment);
        beginTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        beginTransaction.commit();
    }

    public static ArrayList<String> lst_album_image = new ArrayList<>();

    public void onBackPressed() {
        lst_album_image.clear();
        finish();
    }

    public void onClick(View view) {
        if (SystemClock.elapsedRealtime() - mLastClickTime >= 1000) {
            mLastClickTime = SystemClock.elapsedRealtime();
            int id = view.getId();
            if (id == R.id.iv_close) {
                onBackPressed();
            }
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);
    }

    public void onDestroy() {
        super.onDestroy();
    }
}

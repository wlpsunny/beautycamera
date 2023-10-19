package com.eagleapps.beautycam.act;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.eagleapps.beautycam.MyAplication;
import com.eagleapps.beautycam.R;
import com.eagleapps.beautycam.remote.AppOpenManager;
import com.eagleapps.beautycam.remote.SupporterClass;

public class SplashAct extends BaseAct {
    private static int SPLASH_TIME_OUT = 500;
    ImageView iv_backImg;
    Bitmap bitmapLocal;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        iv_backImg = findViewById(R.id.iv_backImg);
        SupporterClass.setUserInSplashIntro(true);
        startTimer();

        bitmapLocal = decodeSampledBitmapFromResource(getResources(), R.drawable.splash, 500, 500);
        iv_backImg.setImageBitmap(bitmapLocal);

    }

    public void startTimer() {
        new Handler().postDelayed(new Runnable() {
            public final void run() {
                voidToNext();
            }
        }, (long) SPLASH_TIME_OUT);
    }

    private void voidToNext() {
        MyAplication.getApplication().loadAdmobNativeAd();
        AppOpenManager appLifecycleObserver = new AppOpenManager(MyAplication.getApplication());
        ProcessLifecycleOwner.get().getLifecycle().addObserver(appLifecycleObserver);
        startActivity(new Intent(this, MainAct.class));
        finish();
    }

    public void onDestroy() {
        super.onDestroy();
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }




}

package com.eagleapps.beautycam.remote;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.eagleapps.beautycam.R;

public class SupporterClass {

    public static boolean isUserInSplash = false;
    public static boolean isFullScreenInView = false;

    public static boolean getFullScreenIsInView() {
        return isFullScreenInView;
    }

    public static void setFullScreenIsInView(boolean value) {
        isFullScreenInView = value;
    }

    public static boolean getUserInSplashIntro() {
        return isUserInSplash;
    }

    public static void setUserInSplashIntro(boolean value) {
        isUserInSplash = value;
    }

    public static AdSize getAdSize(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    public static void loadBannerAds(RelativeLayout adContainerView, Activity activity) {
        adContainerView.setVisibility(View.GONE);

        if (SupporterClass.checkConnection(activity)) {
            AdView mAdView = new AdView(activity);
            String adId = "";
            adId = activity.getString(R.string.admob_banner_ads_id_test);
            if (adId == null) {
                return;
            }
            if (adId.isEmpty()) {
                return;
            }
            mAdView.setAdUnitId(adId);

            adContainerView.removeAllViews();
            adContainerView.addView(mAdView);

            AdSize adSize = getAdSize(activity);
            mAdView.setAdSize(adSize);

            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    Log.e("ADSTAG", "Banner onAdFailedToLoad()" + loadAdError.getCode());
                }
                @Override
                public void onAdLoaded() {
                    Log.e("ADSTAG", "Banner onAdLoaded()");
                    adContainerView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public static boolean checkConnection(Context mContext) {
        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            return false;
        } else {
            return true;
        }
    }
}

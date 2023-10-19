package com.eagleapps.beautycam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.eagleapps.beautycam.otherView.Utils;
import com.eagleapps.beautycam.remote.SupporterClass;
import com.eagleapps.beautycam.remote.notifier.EventNotifier;
import com.eagleapps.beautycam.remote.notifier.EventTypes;
import com.eagleapps.beautycam.remote.notifier.NotifierFactory;
import com.eagleapps.beautycam.utilsApp.TypefaceUtils;

import java.util.ArrayList;
import java.util.List;

public class MyAplication extends MultiDexApplication {
    private static MyAplication mInstance;
    private static MyAplication myApplication;
    public final String TAG = MyAplication.class.getSimpleName();
    public Context sContext = null;
    public boolean isAdNativeAdEnable = false;

    public InterstitialAd mInterstitialAd;
    private List<NativeAd> mNativeAdsGHome = new ArrayList<>();
    private List<String> mNativeAdsId = new ArrayList<>();

    public static MyAplication getApplication() {
        return myApplication;
    }

    public static void setApplication(MyAplication application) {
        myApplication = application;
    }

    public static synchronized MyAplication getInstance() {

        synchronized (MyAplication.class) {
            synchronized (MyAplication.class) {
                myApplication = mInstance;
            }
        }
        return myApplication;
    }

    public List<NativeAd> getGNativeHome() {
        return mNativeAdsGHome;
    }

    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        mInstance = this;
        sContext = getApplicationContext();
        setApplication(this);

        TypefaceUtils.overrideFont(getApplicationContext(), "serif", "fonts/rubik_regular.ttf");

        mInstance = this;

        List<String> testDeviceIds = new ArrayList<>();
        testDeviceIds.add(AdRequest.DEVICE_ID_EMULATOR);
        testDeviceIds.add(Utils.getDeviceID(this));
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        checkAppReplacingState();
    }

    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    private void checkAppReplacingState() {
        Log.d(this.TAG, "app start...");
        if (getResources() == null) {
            Log.d(this.TAG, "app is replacing...kill");
            Process.killProcess(Process.myPid());
        }
    }

    public void loadAdmobNativeAd() {
        isAdNativeAdEnable = true;


        if (isAdNativeAdEnable && SupporterClass.checkConnection(getApplication())) {
            loadGNativeIntermediate(0);
        }
    }

    public void loadGNativeIntermediate(int adCount) {


        if (adCount == 0) {
            mNativeAdsGHome = new ArrayList<>();
            mNativeAdsId.clear();
            mNativeAdsId.add(String.valueOf(R.string.admob_native_Ad_test));
        }
        AdLoader.Builder builder;


        builder = new AdLoader.Builder(this, getString(R.string.admob_native_Ad_test));
        Log.e("NativeAd", "adUnitId:" + getString(R.string.admob_native_Ad_test));
        Log.e("Ads ", "NativeAd adUnitId:  " + getString(R.string.admob_native_Ad_test));


        int native_ads_count = 1;

        builder.forNativeAd(nativeAd -> {
            mNativeAdsGHome.add(nativeAd);
            int nextConunt = adCount + 1;
            if (nextConunt < native_ads_count) {
                Log.e("Ads ", "NativeAd nextConunt: " + nextConunt);
                loadGNativeIntermediate(nextConunt);
            }

            if (nextConunt == native_ads_count) {
                Log.e("Ads ", "NativeAd " + nextConunt + ":Last");
                Log.e("NativeAds: ", "last == ");
                EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_AD_STATUS);
                notifier.eventNotify(EventTypes.EVENT_AD_LOADED_NATIVE, null);
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                Log.e("Ads ", "NativeAd onAdFailedToLoad: " + adError.getMessage());
            }
        }).build();

        AdRequest.Builder builerRe = new AdRequest.Builder();
        adLoader.loadAd(builerRe.build());
    }


    public boolean isAdLoaded() {
        if (mInterstitialAd != null) {
            return true;
        } else {
            return false;
        }
    }

    public void loadFullScreenAdInsider() {
        if (SupporterClass.checkConnection(sContext)) {
            AdRequest adRequest = new AdRequest.Builder().build();
            String adUnitId = "";

            adUnitId = getString(R.string.admob_interstitial_ads_id_test);

            if (adUnitId == null) {
                mInterstitialAd = null;
                return;
            }
            Log.e("Ads ", "FullScreenAd Load adUnitId:  " + adUnitId);
            if (TextUtils.isEmpty(adUnitId)) {
                mInterstitialAd = null;
                return;
            }

            InterstitialAd.load(sContext, adUnitId, adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    Log.e("Ads ", "FullScreenAd: onAdLoaded");
                    mInterstitialAd = interstitialAd;
                    Log.i(TAG, "onAdLoaded");
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    Log.e("Ads ", "FullScreenAd: onAdFailedToLoad: " + loadAdError.getMessage());
                    Log.i(TAG, loadAdError.getMessage());
                    mInterstitialAd = null;
                }
            });
        }
    }


    public void showInterstitial(final Activity act, final Intent intent, final boolean isFinished) {

        if (!SupporterClass.checkConnection(sContext)) {
            SupporterClass.setFullScreenIsInView(false);
            if (intent != null)
                act.startActivity(intent);
            if (isFinished) {
                if (act != null && !act.isFinishing())
                    act.finish();
            }
            return;
        }

        if (mInterstitialAd != null) {
            mInterstitialAd.show(act);
            SupporterClass.setFullScreenIsInView(true);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    SupporterClass.setFullScreenIsInView(false);
                    // Called when fullscreen content is dismissed.
                    Log.d("TAG", "The ad was dismissed.");
                    doNextInterLoad(act, intent, isFinished);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    SupporterClass.setFullScreenIsInView(false);
                    Log.d("TAG", "The ad failed to show.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    SupporterClass.setFullScreenIsInView(true);
                    mInterstitialAd = null;
                    Log.d("TAG", "The ad was shown.");
                }
            });
        } else {
            SupporterClass.setFullScreenIsInView(false);
            if (intent != null)
                act.startActivity(intent);
            if (isFinished) {
                if (act != null && !act.isFinishing())
                    act.finish();
            }
        }
    }

    public void doNextInterLoad(final Activity act, final Intent intent, final boolean isFinished) {
        loadFullScreenAdInsider();
        SupporterClass.setFullScreenIsInView(false);
        if (intent != null)
            act.startActivity(intent);
        if (isFinished) {
            if (act != null && !act.isFinishing())
                act.finish();
        }
    }

}

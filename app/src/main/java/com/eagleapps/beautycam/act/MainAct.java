package com.eagleapps.beautycam.act;

import static android.Manifest.permission.CAMERA;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.kobakei.ratethisapp.RateThisApp;
import com.kobakei.ratethisapp.RateThisApp.Config;
import com.eagleapps.beautycam.BuildConfig;
import com.eagleapps.beautycam.MyAplication;
import com.eagleapps.beautycam.R;
import com.eagleapps.beautycam.remote.SupporterClass;
import com.eagleapps.beautycam.remote.notifier.EventNotifier;
import com.eagleapps.beautycam.remote.notifier.EventState;
import com.eagleapps.beautycam.remote.notifier.EventTypes;
import com.eagleapps.beautycam.remote.notifier.IEventListener;
import com.eagleapps.beautycam.remote.notifier.ListenerPriority;
import com.eagleapps.beautycam.remote.notifier.NotifierFactory;

import java.io.File;
import java.io.IOException;


public class MainAct extends AppCompatActivity implements IEventListener {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM2 = 104;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM3 = 105;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM = 103;
    public boolean IsClicked = false;
    public String imagePath = "";
    int flagOpenNext = 0;
    String outputPath;
    FrameLayout fl_adplaceholder;
    ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // There are no request codes
                    Intent data = result.getData();
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = new Intent(MainAct.this, FaceEditorMainAct.class);
                        intent.putExtra("imagePath", imagePath);
                        startActivity(intent);
                    }

                }
            });

    public static boolean requestCameraPermissionApp(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(activity, CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM);
                return false;
            }
        } else {
            if (ActivityCompat.checkSelfPermission(activity, CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM);
                return false;
            }
        }
        return true;
    }

    public static boolean checkForCameraPermission(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public static boolean permissionCheckCreation(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(activity, CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM3);
                return false;
            }
        } else {
            if (ActivityCompat.checkSelfPermission(activity, CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM3);
                return false;
            }
        }
        return true;

    }

    @Override
    public int eventNotify(int eventType, final Object eventObject) {
        Log.e("Update: ", "eventNotify");
        int eventState = EventState.EVENT_IGNORED;
        switch (eventType) {
            case EventTypes.EVENT_AD_LOADED_NATIVE:
                Log.e("Update: ", "Case");
                eventState = EventState.EVENT_PROCESSED;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadNativeAds();
                            }
                        }, 500);
                    }
                });
        }
        return eventState;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        SupporterClass.setUserInSplashIntro(false);
        registerAdsListener();
        RateThisApp.init(new Config(3, 3));
        RateThisApp.onCreate(this);
        RateThisApp.showRateDialogIfNeeded(this);
        fl_adplaceholder = findViewById(R.id.fl_adplaceholder);
        loadNativeAds();
        ((LinearLayout) findViewById(R.id.ivgallery)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                if (requestCameraPermissionApp2(MainAct.this)) {
                    toGallery();
                }

            }
        });
        ((LinearLayout) findViewById(R.id.ivcamera)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                if (requestCameraPermissionApp(MainAct.this)) {
                    toOpenCamera();
                }
            }
        });
        ((LinearLayout) findViewById(R.id.ivmycreation)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                if (permissionCheckCreation(MainAct.this)) {
                    if (!IsClicked) {
                        IsClicked = true;
                        view.postDelayed(new Runnable() {
                            public void run() {
                                IsClicked = false;
                            }
                        }, 1000);
                        try {
                            flagOpenNext = 1;
//                                showAd();
                            openNext();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });
        ((LinearLayout) findViewById(R.id.ivshareapp)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    StringBuilder sb = new StringBuilder();
                    sb.append("Hello friends, look at this awesome face beauty app --> https://play.google.com/store/apps/details?id=");
                    sb.append(getPackageName());
                    intent.putExtra("android.intent.extra.TEXT", sb.toString());
                    Intent createChooser = Intent.createChooser(intent, "Share");
                    createChooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(createChooser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void toOpenCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", createImageFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraResultLauncher.launch(intent);
    }

    private File createImageFile() {
        File storageDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + BuildConfig.APPLICATION_ID + "/CamPic/");
        storageDir.mkdirs();
        File image = null;
        try {
            image = new File(storageDir, getString(R.string.temp));
            if (image.exists())
                image.delete();
            image.createNewFile();

            imagePath = image.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public boolean requestCameraPermissionApp2(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(activity, CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM2);
                return false;
            }
        } else {
            if (ActivityCompat.checkSelfPermission(activity, CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM2);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM: {
                if (!checkForCameraPermission(this)) {
                    Toast.makeText(this, R.string.phone_camera_permission_not_granted, Toast.LENGTH_SHORT).show();
                } else if (!checkForStoragePermission(this)) {
                    Toast.makeText(this, R.string.storage_permission_not_granted, Toast.LENGTH_SHORT).show();
                } else if (checkForCameraPermission(this) && checkForStoragePermission(this)) {
                    toOpenCamera();
                    return;
                }
                requestCameraPermissionApp(MainAct.this);
                return;
            }

            case MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM2: {
                if (!checkForStoragePermission(this))
                    Toast.makeText(this, R.string.storage_permission_not_granted, Toast.LENGTH_SHORT).show();
                else {
                    toGallery();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM3: {
                if (!permissionCheckCreation(this))
                    Toast.makeText(this, R.string.storage_permission_not_granted, Toast.LENGTH_SHORT).show();
                else {
                    if (!IsClicked) {
                        IsClicked = true;
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            public void run() {
                                IsClicked = false;
                            }
                        }, 1000);
                        try {
                            flagOpenNext = 1;
//                                showAd();
                            openNext();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
//                    openNext();
                }
                return;
            }

        }
    }

    private void toGallery() {
        Intent intent = new Intent(MainAct.this, GalleryListAct.class);
        startActivity(intent);
    }

    public boolean checkForStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        }
    }

    private void registerAdsListener() {
        EventNotifier
                notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_AD_STATUS);
        notifier.registerListener(this, ListenerPriority.PRIORITY_HIGH);
    }

    private void loadNativeAds() {
        if (MyAplication.getApplication().getGNativeHome() != null && MyAplication.getApplication().getGNativeHome().size() > 0 && MyAplication.getApplication().getGNativeHome().get(0) != null) {
            NativeAd nativeAd = MyAplication.getApplication().getGNativeHome().get(0);
            NativeAdView adView = (NativeAdView) LayoutInflater.from(MainAct.this).inflate(R.layout.admob_native_ads, null);
            populateUnifiedNativeAdView(nativeAd, adView);
            fl_adplaceholder.removeAllViews();
            fl_adplaceholder.addView(adView);
            fl_adplaceholder.setVisibility(View.VISIBLE);
        } else {
            fl_adplaceholder.setVisibility(View.GONE);
        }
    }

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        try {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.getStoreView().setVisibility(View.GONE);
        adView.getPriceView().setVisibility(View.GONE);

        adView.setNativeAd(nativeAd);

        VideoController vc = nativeAd.getMediaContent().getVideoController();
        if (vc.hasVideoContent()) {
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        } else {
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString("picker_path", outputPath);
        super.onSaveInstanceState(bundle);
    }

    public void onRestoreInstanceState(Bundle bundle) {
        if (bundle != null) {
            String str = "picker_path";
            if (bundle.containsKey(str)) {
                outputPath = bundle.getString(str);
            }
        }
        super.onRestoreInstanceState(bundle);
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public void openNext() {
        if (flagOpenNext == 1) {
            startActivity(new Intent(this, MyCreationAct.class));
            return;
        }
        Intent intent = new Intent(this, FaceEditorMainAct.class);
        intent.putExtra("imagePath", imagePath);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}

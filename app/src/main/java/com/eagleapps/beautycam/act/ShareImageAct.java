package com.eagleapps.beautycam.act;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ShareCompat.IntentBuilder;
import androidx.core.content.FileProvider;

import com.eagleapps.beautycam.R;
import com.eagleapps.beautycam.remote.SupporterClass;

import java.io.File;
import java.util.Iterator;

public class ShareImageAct extends BaseAct implements OnClickListener {
    private static final String TAG = "ShareImageActivity";
    private int REQUEST_FOR_GOOGLE_PLUS = 0;
    private ImageView btnBack;
    private ImageView btnMoreApp;
    private ImageView btnShareFacebook;
    private ImageView btnSharePinterest;
    private ImageView btnShareHike;
    private ImageView btnShareIntagram;
    private ImageView btnShareMore;
    private ImageView btnShareMoreImage;
    private ImageView btnShareTwitter;
    private ImageView btnShareWhatsapp;
    private ImageView btnSharewMessanger;
    private ImageView imageView;
    private Uri phototUri = null;
    private File pictureFile;
    private TextView txtToolbar;

    public void onCreate(Bundle bundle) {
        getWindow().setFlags(1024, 1024);
        super.onCreate(bundle);
        setContentView(R.layout.activity_share_image);

        RelativeLayout adViewBanner = findViewById(R.id.rel_banner_ads);
        SupporterClass.loadBannerAds(adViewBanner, ShareImageAct.this);

        findView();
        initUI();
    }

    private void findView() {
        btnBack = (ImageView) findViewById(R.id.btn_back);
        txtToolbar = (TextView) findViewById(R.id.txt_toolbar);
        btnShareMore = (ImageView) findViewById(R.id.btnShareMore);
        btnMoreApp = (ImageView) findViewById(R.id.btnMoreApp);
        btnShareFacebook = (ImageView) findViewById(R.id.btnShareFacebook);
        btnShareIntagram = (ImageView) findViewById(R.id.btnShareIntagram);
        btnShareWhatsapp = (ImageView) findViewById(R.id.btnShareWhatsapp);
        btnSharePinterest = (ImageView) findViewById(R.id.btnSharePinterest);
        btnSharewMessanger = (ImageView) findViewById(R.id.btnSharewMessanger);
        btnShareTwitter = (ImageView) findViewById(R.id.btnShareTwitter);
        btnShareHike = (ImageView) findViewById(R.id.btnShareHike);
        btnShareMoreImage = (ImageView) findViewById(R.id.btnShareMoreImage);
        btnBack.setOnClickListener(this);
        btnShareMore.setOnClickListener(this);
        btnMoreApp.setOnClickListener(this);
        btnShareFacebook.setOnClickListener(this);
        btnShareIntagram.setOnClickListener(this);
        btnShareWhatsapp.setOnClickListener(this);
        btnSharePinterest.setOnClickListener(this);
        btnSharewMessanger.setOnClickListener(this);
        btnShareTwitter.setOnClickListener(this);
        btnShareHike.setOnClickListener(this);
        btnShareMoreImage.setOnClickListener(this);
    }

    public void initUI() {
        imageView = (ImageView) findViewById(R.id.image);
        File file = new File(getIntent().getData().getPath());
        pictureFile = file;
        Uri fromFile = Uri.fromFile(file);
        phototUri = fromFile;
        imageView.setImageURI(fromFile);
    }

    public void sendToPinterest(String str) {
        String str2 = "com.pinterest";
        if (getApplicationContext().getPackageManager().getLaunchIntentForPackage(str2) != null) {
            try {
                IntentBuilder type = IntentBuilder.from(this).setType("image/jpeg");
                StringBuilder sb = new StringBuilder();
                sb.append(getApplicationContext().getPackageName());
                sb.append(".provider");
                startActivityForResult(type.setStream(FileProvider.getUriForFile(this, sb.toString(), new File(str))).getIntent().setPackage(str2), REQUEST_FOR_GOOGLE_PLUS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Pinterest not installed", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMoreApp:
                Intent intent = new Intent(this, MainAct.class);
                intent.addFlags(536870912);
                intent.addFlags(67108864);
                startActivity(intent);
                return;
            case R.id.btnShareFacebook:
                shareToFacebook(pictureFile.getPath());
                return;
            case R.id.btnSharePinterest:
                sendToPinterest(pictureFile.getPath());
                return;
            case R.id.btnShareHike:
                shareToHike(pictureFile.getPath());
                return;
            case R.id.btnShareIntagram:
                shareToInstagram(pictureFile.getPath());
                return;
            case R.id.btnShareMore:
                shareImage(pictureFile.getPath());
                return;
            case R.id.btnShareMoreImage:
                shareImage(pictureFile.getPath());
                return;
            case R.id.btnShareTwitter:
                shareToTwitter(pictureFile.getPath());
                return;
            case R.id.btnShareWhatsapp:
                sendToWhatsaApp(pictureFile.getPath());
                return;
            case R.id.btnSharewMessanger:
                shareToMessanger(pictureFile.getPath());
                return;
            case R.id.btn_back:
                onBackPressed();
                return;
            default:
                return;
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainAct.class));
    }

    public void onResume() {
        super.onResume();
    }

    public void sendToWhatsaApp(String str) {
        String str2 = "com.whatsapp";
        if (getApplicationContext().getPackageManager().getLaunchIntentForPackage(str2) != null) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(getApplicationContext().getPackageName());
                sb.append(".provider");
                Uri uriForFile = FileProvider.getUriForFile(this, sb.toString(), new File(str));
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.setType("image/*");
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                intent.setPackage(str2);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }

    public void shareToHike(String str) {
        String str2 = "com.bsb.hike";
        if (getApplicationContext().getPackageManager().getLaunchIntentForPackage(str2) != null) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(getApplicationContext().getPackageName());
                sb.append(".provider");
                Uri uriForFile = FileProvider.getUriForFile(this, sb.toString(), new File(str));
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/*");
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                intent.setPackage(str2);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Hike not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareToTwitter(String str) {
        try {
            if (getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.twitter.android") != null) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(getApplicationContext().getPackageName());
                    sb.append(".provider");
                    Uri uriForFile = FileProvider.getUriForFile(this, sb.toString(), new File(str));
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.putExtra("android.intent.extra.STREAM", uriForFile);
                    intent.setType("image/*");
                    Iterator it = getPackageManager().queryIntentActivities(intent, 65536).iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        ResolveInfo resolveInfo = (ResolveInfo) it.next();
                        if (resolveInfo.activityInfo.name.contains("twitter")) {
                            intent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                            startActivity(intent);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Twitter not installed", Toast.LENGTH_SHORT).show();
            }
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(getApplicationContext(), "You don't seem to have twitter installed on this device", Toast.LENGTH_SHORT).show();
        }
    }

    public void shareToFacebook(String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        String str2 = "com.facebook.katana";
        intent.setPackage(str2);
        if (getApplicationContext().getPackageManager().getLaunchIntentForPackage(str2) != null) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(getApplicationContext().getPackageName());
                sb.append(".provider");
                intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(this, sb.toString(), new File(str)));
                intent.setType("image/*");
                intent.addFlags(1);
                startActivity(Intent.createChooser(intent, "Share Photo."));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Facebook not installed", Toast.LENGTH_SHORT).show();
        }
    }

    public void shareToMessanger(String str) {
        String str2 = "com.facebook.orca";
        if (getPackageManager().getLaunchIntentForPackage(str2) != null) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(getApplicationContext().getPackageName());
                sb.append(".provider");
                Uri uriForFile = FileProvider.getUriForFile(this, sb.toString(), new File(str));
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/*");
                intent.setPackage(str2);
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                intent.addFlags(524288);
                startActivity(Intent.createChooser(intent, "Test"));
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(getApplicationContext(), "You don't seem to have twitter installed on this device", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Facebook Messanger not installed", Toast.LENGTH_SHORT).show();
        }
    }

    public void shareToInstagram(String str) {
        String str2 = "com.instagram.android";
        if (getApplicationContext().getPackageManager().getLaunchIntentForPackage(str2) != null) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(getApplicationContext().getPackageName());
                sb.append(".provider");
                Uri uriForFile = FileProvider.getUriForFile(this, sb.toString(), new File(str));
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/*");
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                intent.setPackage(str2);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Instagram not installed", Toast.LENGTH_SHORT).show();
        }
    }

    public void shareImage(String str) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            Uri uriForFile = FileProvider.getUriForFile(this, sb.toString(), new File(str));
            Intent intent = new Intent("android.intent.action.SEND");
            intent.addFlags(524288);
            intent.setType("image/*");
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getResources().getString(R.string.share_text));
            sb2.append("\nhttps://play.google.com/store/apps/details?id=");
            sb2.append(getPackageName());
            intent.putExtra("android.intent.extra.TEXT", sb2.toString());
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            startActivity(Intent.createChooser(intent, "Share via"));
        } catch (Exception e) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("shareImage: ");
            sb3.append(e);
            Log.e(TAG, sb3.toString());
        }
    }
}

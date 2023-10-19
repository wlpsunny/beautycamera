package com.eagleapps.beautycam.act;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdRequest.Builder;
//import com.kbeanie.multipicker.api.CacheLocation;
import com.eagleapps.beautycam.MyAplication;
import com.eagleapps.beautycam.R;
import com.eagleapps.beautycam.helper.Constant;
import com.eagleapps.beautycam.helper.Landmark;
import com.eagleapps.beautycam.helper.ResizeImages;
import com.eagleapps.beautycam.remote.SupporterClass;
import com.eagleapps.beautycam.utilsApp.ScanFileClass;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;
import com.yalantis.ucrop.view.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FaceEditorMainAct extends BaseAct implements OnClickListener {
    public static List<Landmark> faceLandmarks;
    public static int[] faceRects;
    public static Rect rect;
    public Context context;
    public int currentapiVersion;
    public String imagePath;
    public Uri imageuri = null;
    public Bitmap originalBmp;
//    AdRequest adRequest;
    Animation animation;
    int coutAdFlag = 0;
    String datFilePath;
//    private int BLEMISH_RESULT_CODE = CacheLocation.EXTERNAL_CACHE_DIR;
    private int BLUR_RESULT_CODE = 400;
    private int EYE_RESULT_CODE = 100;
    private int LIPS_RESULT_CODE = 200;
    private int WHITTEN_RESULT_CODE = CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION;
    private ImageView editorImageview;
    private LinearLayout editor_done;
    private ImageView imgBlur;
    private ImageView imgBlush;
    private ImageView imgEye;
    private ImageView imgFoundation;
    private ImageView imgLips;
    private ImageView imgWhitten;
    private LinearLayout layoutBlur;
    private LinearLayout layoutBlush;
    private LinearLayout layoutEyes;
    private LinearLayout layoutFoundation;
    private LinearLayout layoutLips;
    private LinearLayout layoutWhitten;
    private String savePath;
    private TextView txtBlur;
    private TextView txtBlush;
    private TextView txtEye;
    private TextView txtFoundation;
    private TextView txtLips;
    private TextView txtWhitten;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_face_edit);

        RelativeLayout adViewBanner = findViewById(R.id.rel_banner_ads);
        SupporterClass.loadBannerAds(adViewBanner, FaceEditorMainAct.this);

        context = this;
        faceLandmarks = new ArrayList();
        faceRects = new int[4];
        rect = new Rect();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        currentapiVersion = VERSION.SDK_INT;
        ResizeImages resizeImage = new ResizeImages();
        imagePath = getIntent().getStringExtra("imagePath");

        //  try {
        originalBmp = resizeImage.getScaledBitamp(imagePath, displayMetrics.widthPixels);
        try {
            imagePath = resizeImage.saveBitmap(context, Constant.TEMP_FOLDER_NAME, originalBmp);
        } catch (FileNotFoundException e) {
            Toast.makeText(FaceEditorMainAct.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        editorImageview = (ImageView) findViewById(R.id.editor_image_view);
        editor_done = (LinearLayout) findViewById(R.id.facetuneditor_done_btn);
        editorImageview.setImageBitmap(originalBmp);
        init();
        admobNativeAdinit();
        layoutLips = (LinearLayout) findViewById(R.id.layoutLips);
        layoutEyes = (LinearLayout) findViewById(R.id.layoutEyes);
        layoutBlush = (LinearLayout) findViewById(R.id.layoutBlush);
        layoutWhitten = (LinearLayout) findViewById(R.id.layoutWhitten);
        layoutBlur = (LinearLayout) findViewById(R.id.layoutBlur);
        layoutFoundation = (LinearLayout) findViewById(R.id.layoutFoundation);

        layoutLips.setOnClickListener(this);
        layoutEyes.setOnClickListener(this);
        layoutBlush.setOnClickListener(this);
        layoutWhitten.setOnClickListener(this);
        layoutBlur.setOnClickListener(this);
        layoutFoundation.setOnClickListener(this);
        new FaceDetectTask().execute(new Void[0]);
//        adRequest = new Builder().build();
        editor_done.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                showAd();
            }
        });
    }

    public void showAd() {
        openNext();
    }

    public void openNext() {

        if (imagePath != null) {

            Intent intent = new Intent(FaceEditorMainAct.this, EditLastAct.class);
            intent.putExtra("imagePath", imagePath);
            if (MyAplication.getApplication().isAdLoaded()) {
                MyAplication.getApplication().showInterstitial(this, intent, false);
            } else {
                startActivity(intent);
            }
        }

    }

    private void admobNativeAdinit() {

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);

        LayoutParams layoutParams = new LayoutParams(-2, -2);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.done);
        layoutParams.width = (int) (((float) decodeResource.getWidth()) * 1.2f);
        layoutParams.height = (int) (((float) decodeResource.getWidth()) * 1.2f);

        animation.setRepeatCount(-1);
    }

    private void init() {
        imgEye = (ImageView) findViewById(R.id.img_eyes);
        imgLips = (ImageView) findViewById(R.id.img_lips);
        imgBlush = (ImageView) findViewById(R.id.img_blush);
        imgBlur = (ImageView) findViewById(R.id.img_blur);
        imgWhitten = (ImageView) findViewById(R.id.img_whitten);
        imgFoundation = (ImageView) findViewById(R.id.img_foundation);
        txtEye = (TextView) findViewById(R.id.txt_eyes);
        txtLips = (TextView) findViewById(R.id.txt_lips);
        txtBlush = (TextView) findViewById(R.id.txt_blush);
        txtBlur = (TextView) findViewById(R.id.txt_blur);
        txtWhitten = (TextView) findViewById(R.id.txt_whitten);
//        txtEditor = (TextView) findViewById(R.id.txt_editor);
        txtFoundation = (TextView) findViewById(R.id.txt_foundation);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (/*i2 == BLEMISH_RESULT_CODE ||*/ i2 == BLUR_RESULT_CODE || i2 == LIPS_RESULT_CODE || i2 == EYE_RESULT_CODE || i2 == WHITTEN_RESULT_CODE) {
            Bitmap bitmap = originalBmp;
            if (bitmap != null && !bitmap.isRecycled()) {
                originalBmp.recycle();
                originalBmp = null;
                System.gc();
            }
            Bitmap GetImageImageRemaker = GetImageImageRemaker(intent, i2, i);
            originalBmp = GetImageImageRemaker;
            editorImageview.setImageBitmap(GetImageImageRemaker);
        }
        if (i2 == 0) {
            editorImageview.setImageBitmap(originalBmp);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        try {
            faceLandmarks.clear();
            if (originalBmp != null && !originalBmp.isRecycled()) {
                originalBmp.recycle();
                originalBmp = null;
                System.gc();
            }
            File file = new File(Constant.TEMP_FOLDER_NAME);
            if (file.exists()) {
                DeleteRecursive(file);
            }
            startActivity(new Intent(this, MainAct.class));
        } catch (Exception unused) {
        }
    }

    public void DeleteRecursive(File file) {
        File[] listFiles;
        currentapiVersion = VERSION.SDK_INT;
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                DeleteRecursive(file2);
                deleteFromGallery(file2.getPath());
            }
        }
        file.delete();
    }

    private void deleteFromGallery(String str) {
        String[] strArr = {str};
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        String str2 = "_id";
        ContentResolver contentResolver2 = contentResolver;
        Cursor query = contentResolver2.query(uri, new String[]{str2}, "_data = ?", strArr, null);
        if (query.moveToFirst()) {
            contentResolver.delete(ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, query.getLong(query.getColumnIndexOrThrow(str2))), null, null);
        } else {
            new File(str).delete();
            new ScanFileClass().refreshGallery(this, str, currentapiVersion);
        }
        query.close();
    }

    private Bitmap GetImageImageRemaker(Intent intent, int i, int i2) {
        Bitmap bitmap;
        try {
            Uri data = intent.getData();
//            File file = new File(intent.getStringExtra("filePath"));
//            if (file.exists()){
//                imagePath = String.valueOf(file);
//                bitmap = BitmapFactory.decodeFile(imagePath);
//            }
            if (data != null) {
                bitmap = Media.getBitmap(getContentResolver(), data);
                imageuri = data;
                imagePath = data.getPath().toString();
            } else {
                bitmap = null;
            }
            return bitmap;
        } catch (Exception unused) {
            StringBuilder sb = new StringBuilder();
            sb.append(intent.getStringExtra("path"));
//            sb.append(intent.getStringExtra("filePath"));
//            sb.append("temp.jpg");
            String sb2 = sb.toString();
            imagePath = sb2;
            File file = new File(sb2);
            return BitmapFactory.decodeFile(file.getAbsolutePath(), new BitmapFactory.Options());
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        String str = "isBlushChecked";
        String str2 = "imagePath";
        switch (id) {
            case R.id.layoutBlur:
                activeBlur();
                Intent intent2 = new Intent(this, FaceBlurEditAct.class);
                intent2.putExtra(str2, imagePath);
                startActivityForResult(intent2, BLUR_RESULT_CODE);
                coutAdFlag++;
                break;
            case R.id.layoutBlush:
                activeBlush();
                Intent intent3 = new Intent(this, LipEditAct.class);
                intent3.putExtra(str2, imagePath);
                intent3.putExtra(str, Options.BLUSH);
                startActivityForResult(intent3, LIPS_RESULT_CODE);
                coutAdFlag++;
                break;
            default:
                switch (id) {
                    case R.id.layoutEyes:
                        activeEyes();
                        Intent intent4 = new Intent(this, EyeEditAct.class);
                        intent4.putExtra(str2, imagePath);
                        startActivityForResult(intent4, EYE_RESULT_CODE);
                        coutAdFlag++;
                        break;
                    case R.id.layoutFoundation:
                        activeFoundation();
                        Intent intent5 = new Intent(this, LipEditAct.class);
                        intent5.putExtra(str2, imagePath);
                        intent5.putExtra(str, Options.FOUNDATION);
                        startActivityForResult(intent5, LIPS_RESULT_CODE);
                        coutAdFlag++;
                        break;
                    case R.id.layoutLips:
                        activeLips();
                        Intent intent6 = new Intent(this, LipEditAct.class);
                        intent6.putExtra(str2, imagePath);
                        intent6.putExtra(str, Options.LIPCOLOR);
                        startActivityForResult(intent6, LIPS_RESULT_CODE);
                        coutAdFlag++;
                        break;
                    case R.id.layoutWhitten:
                        activeWhitten();
                        Intent intent7 = new Intent(this, WhittenFaceEditAct.class);
                        intent7.putExtra(str2, imagePath);
                        startActivityForResult(intent7, WHITTEN_RESULT_CODE);
                        coutAdFlag++;
                        break;
                }
        }

        animation.cancel();
        animation.reset();
    }

    private void activeWhitten() {
        imgEye.setImageResource((R.drawable.ic_eye));
        imgLips.setImageResource((R.drawable.ic_lip));
        imgBlur.setImageResource((R.drawable.ic_blur));
        imgBlush.setImageResource((R.drawable.ic_cheek));
        imgWhitten.setImageResource((R.drawable.ic_whitten_selected));
        imgFoundation.setImageResource((R.drawable.ic_face));
    }

    private void activeBlur() {

        imgEye.setImageResource((R.drawable.ic_eye));
        imgLips.setImageResource((R.drawable.ic_lip));
        imgBlush.setImageResource((R.drawable.ic_cheek));
        imgBlur.setImageResource((R.drawable.ic_blur_selected));
        imgWhitten.setImageResource((R.drawable.ic_whitten));
        imgFoundation.setImageResource((R.drawable.ic_face));
    }

    private void activeEyes() {

        imgEye.setImageResource((R.drawable.ic_eye_selected));
        imgLips.setImageResource((R.drawable.ic_lip));
        imgBlush.setImageResource((R.drawable.ic_cheek));
        imgBlur.setImageResource((R.drawable.ic_blur));
        imgWhitten.setImageResource((R.drawable.ic_whitten));
        imgFoundation.setImageResource((R.drawable.ic_face));
    }

    private void activeBlush() {

        imgEye.setImageResource((R.drawable.ic_eye));
        imgLips.setImageResource((R.drawable.ic_lip));
        imgBlush.setImageResource((R.drawable.ic_cheek_selected));
        imgBlur.setImageResource((R.drawable.ic_blur));
        imgFoundation.setImageResource((R.drawable.ic_face));
        imgWhitten.setImageResource((R.drawable.ic_whitten));
    }

    private void activeLips() {

        imgEye.setImageResource((R.drawable.ic_eye));
        imgLips.setImageResource((R.drawable.ic_lip_selected));
        imgBlush.setImageResource((R.drawable.ic_cheek));
        imgBlur.setImageResource((R.drawable.ic_blur));
        imgWhitten.setImageResource((R.drawable.ic_whitten));
        imgFoundation.setImageResource((R.drawable.ic_face));
    }

    private void activeFoundation() {

        imgEye.setImageResource((R.drawable.ic_eye));
        imgLips.setImageResource((R.drawable.ic_lip));
        imgBlush.setImageResource((R.drawable.ic_cheek));
        imgBlur.setImageResource((R.drawable.ic_blur));
        imgWhitten.setImageResource((R.drawable.ic_whitten));
        imgFoundation.setImageResource((R.drawable.ic_face_selected));
    }

    public void copyDatToSdcardSIRA() {
        try {
            InputStream in = getResources().openRawResource(R.raw.shape_predictor_68_face_landmarks);
            FileOutputStream out = new FileOutputStream(datFilePath);

            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
            }

        } catch (Exception e) {
            Toast.makeText(context, "No Transfer", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public enum Options {
        LIPCOLOR,
        BLUSH,
        FOUNDATION
    }

    private class FaceDetectTask extends AsyncTask<Void, Void, Void> {
        public Dialog progressDialog;
        private FaceDetectTask() {
            progressDialog = new Dialog(FaceEditorMainAct.this);
        }

        public void onPreExecute() {
            super.onPreExecute();
            progressDialog.setContentView(R.layout.download_popup);
            progressDialog.setCancelable(false);
            Window window = progressDialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progressDialog.show();

        }

        public Void doInBackground(Void... voidArr) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(getFilesDir().getAbsolutePath());
                sb.append("/shape_detector.dat");
                datFilePath = new File(sb.toString()).toString();
                if (!new File(datFilePath).exists()) {
                    copyDatToSdcardSIRA();
                }
                for (VisionDetRet visionDetRet : new FaceDet(datFilePath).detect(originalBmp)) {
                    FaceEditorMainAct.faceRects[0] = visionDetRet.getLeft();
                    FaceEditorMainAct.faceRects[1] = visionDetRet.getTop();
                    FaceEditorMainAct.faceRects[2] = visionDetRet.getRight();
                    FaceEditorMainAct.faceRects[3] = visionDetRet.getBottom();
                    FaceEditorMainAct.rect.left = FaceEditorMainAct.faceRects[0];
                    FaceEditorMainAct.rect.top = FaceEditorMainAct.faceRects[1];
                    FaceEditorMainAct.rect.right = FaceEditorMainAct.faceRects[2];
                    FaceEditorMainAct.rect.bottom = FaceEditorMainAct.faceRects[3];
                    ArrayList faceLandmarks = visionDetRet.getFaceLandmarks();
                    for (int i = 0; i != faceLandmarks.size(); i++) {
                        PointF pointF = new PointF();
                        pointF.set((float) ((Point) faceLandmarks.get(i)).x, (float) ((Point) faceLandmarks.get(i)).y);
                        FaceEditorMainAct.faceLandmarks.add(new Landmark(pointF, i));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, "Could not find face...", Toast.LENGTH_SHORT).show();
                        FaceDetectTask.this.progressDialog.dismiss();
                    }
                });
            } catch (Throwable th) {
                th.printStackTrace();
            }
            return null;
        }

        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            progressDialog.dismiss();
        }
    }
}



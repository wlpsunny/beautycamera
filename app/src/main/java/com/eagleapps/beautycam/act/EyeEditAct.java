package com.eagleapps.beautycam.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path.FillType;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eagleapps.beautycam.R;
import com.eagleapps.beautycam.helper.BeautyCameraHelper;
import com.eagleapps.beautycam.helper.ColorListAdapt;
import com.eagleapps.beautycam.helper.Face;
import com.eagleapps.beautycam.helper.Landmark;
import com.eagleapps.beautycam.helper.MakeUpView;
import com.eagleapps.beautycam.helper.MakeUpView.BEAUTY_MODE;
import com.eagleapps.beautycam.helper.RecyclerItemClickListener;
import com.eagleapps.beautycam.helper.RecyclerItemClickListener.OnItemClickListener;
import com.eagleapps.beautycam.remote.SupporterClass;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class EyeEditAct extends BaseAct implements OnClickListener, OnTouchListener {
    final String TAG = EditLastAct.class.getSimpleName();
    public BEAUTY_MODE beautyMode = BEAUTY_MODE.NONE;
    public BEAUTY_MODE beautyModePrev = BEAUTY_MODE.NONE;
    public Context context = this;
    public MakeUpView makeUpView;
    public Bitmap sourceBitmap;
    int EYE_RESULT_CODE = 100;
    int INDEX_AUTO_FILTER = 3;
    int FOOTER_MODE_PANEL_ACNE = INDEX_AUTO_FILTER;
    int INDEX_COLOR = 5;
    int INDEX_EDIT = 1;
    int FOOTER_MODE_PANEL = INDEX_EDIT;
    int INDEX_FX = 2;
    int MAX_PROGRESS = 35;
    Activity activity = this;
    LinearLayout beautyLayout;
    ImageView beforeAfterEyeLayout;
    LinearLayout bottmLayout;
    LinearLayout cancelbtn;
    int colorindex = 0;
    int currentProgress = 23;
    LinearLayout doneBtn;
    LinearLayout doneFinal;
    LinearLayout eyeBagLayout;
    LinearLayout eyeBrightLayout;
    int eyeClick = 0;
    LinearLayout eyeColorLayout;
    LinearLayout eyeEnlargeLayout;
    LinearLayout eyeModes;
    SeekBar eye_seekBar;
    List<Landmark> faceLandmarks = new ArrayList();
    int[] faceRect;
    boolean isFaceDetected = true;
    boolean isFaceDetecting = true;
    LinearLayout recycleViewLayout;
    String resultPath;
    LinearLayout seekBarLayout;
    Animation slide_down;
    Animation slide_right_in;
    Animation slide_up;
    private ImageView imgBrighten;
    private ImageView imgEnlarge;
    private ImageView imgEyeBag;
    private ImageView imgEyeColor;
    private ColorListAdapt mAdapter;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private TextView txtBrighten;
    private TextView txtEnlarge;
    private TextView txtEyeBag;
    private TextView txtEyeColor;
    private TextView txtEyeEdit;

    public void onCreate(Bundle bundle) {
        requestWindowFeature(INDEX_EDIT);
        super.onCreate(bundle);
        setContentView(R.layout.activity_eye_edit);

        RelativeLayout adViewBanner = findViewById(R.id.rel_banner_ads);
        SupporterClass.loadBannerAds(adViewBanner, EyeEditAct.this);

        getWindowManager();
        mContext = this;
        String stringExtra = getIntent().getStringExtra("imagePath");
        beautyLayout = (LinearLayout) findViewById(R.id.beautyLayout);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.eyeColorLayout);
        eyeColorLayout = linearLayout;
        linearLayout.setOnClickListener(this);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.eyeEnlargeLayout);
        eyeEnlargeLayout = linearLayout2;
        linearLayout2.setOnClickListener(this);
        LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.eyeBrightLayout);
        eyeBrightLayout = linearLayout3;
        linearLayout3.setOnClickListener(this);
        LinearLayout linearLayout4 = (LinearLayout) findViewById(R.id.eyeBagLayout);
        eyeBagLayout = linearLayout4;
        linearLayout4.setOnClickListener(this);
        ImageView imageView = (ImageView) findViewById(R.id.beforeAfterEyeLayout);
        beforeAfterEyeLayout = imageView;
        imageView.setOnTouchListener(this);
        doneBtn = (LinearLayout) findViewById(R.id.doneBtn);
        doneFinal = (LinearLayout) findViewById(R.id.doneFinal);
        cancelbtn = (LinearLayout) findViewById(R.id.closebtn);
        doneBtn.setOnClickListener(this);
        cancelbtn.setOnClickListener(this);
        doneFinal.setOnClickListener(this);
        init();
        slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        slide_right_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_in);
        try {
            sourceBitmap = BitmapFactory.decodeStream(new FileInputStream(new File(stringExtra)));
            LayoutParams layoutParams = new LayoutParams(sourceBitmap.getWidth(), sourceBitmap.getHeight());
            layoutParams.addRule(13, -1);
            beautyLayout.setLayoutParams(layoutParams);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            mRecyclerView = recyclerView;
            recyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            ColorListAdapt colorListAdapter = new ColorListAdapt(mContext, BeautyCameraHelper.colorPaletteEyeColor, false);
            mAdapter = colorListAdapter;
            mRecyclerView.setAdapter(colorListAdapter);
            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new OnItemClickListener() {
                public void onItemClick(View view, int i) {
                    colorindex = i;
                    if (eyeClick == 1) {
                        EyeEditAct eyeActivity = EyeEditAct.this;
                        new EyeColorAsyncTask(eyeActivity.makeUpView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                    }
                }
            }));
            MakeUpView makeUpView2 = new MakeUpView(this, sourceBitmap);
            makeUpView = makeUpView2;
            beautyLayout.addView(makeUpView2);
            new FaceDetectionTask(makeUpView, false).execute(new Void[0]);
            eye_seekBar = (SeekBar) findViewById(R.id.seek_bar);
            seekBarLayout = (LinearLayout) findViewById(R.id.seekBarLayout);
            recycleViewLayout = (LinearLayout) findViewById(R.id.recycleViewLayout);
            bottmLayout = (LinearLayout) findViewById(R.id.bottmLayout);
            eyeModes = (LinearLayout) findViewById(R.id.eyeModes);
            eye_seekBar.setMax(MAX_PROGRESS);
            eye_seekBar.setProgress(currentProgress);
            eye_seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
                public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                    currentProgress = i;
                }
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int i = eyeClick;
                    if (i == 1) {
                        EyeEditAct eyeActivity = EyeEditAct.this;
                        new EyeColorAsyncTask(eyeActivity.makeUpView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                    } else if (i == 2) {
                        EyeEditAct eyeActivity2 = EyeEditAct.this;
                        new EnlargeEyeAsyncTask(eyeActivity2.makeUpView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                    } else if (i == 3) {
                        EyeEditAct eyeActivity3 = EyeEditAct.this;
                        new BrightenEyeAsyncTask(eyeActivity3.makeUpView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                    } else if (i == 4) {
                        EyeEditAct eyeActivity4 = EyeEditAct.this;
                        new DarkCircleTask(eyeActivity4.makeUpView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Image not supported ", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void init() {
        imgEyeColor = (ImageView) findViewById(R.id.img_eyeColor);
        imgEnlarge = (ImageView) findViewById(R.id.img_eyeEnlarge);
        imgBrighten = (ImageView) findViewById(R.id.img_eyBrighten);
        imgEyeBag = (ImageView) findViewById(R.id.img_eyeBag);
        txtEyeColor = (TextView) findViewById(R.id.txt_eyeColor);
        txtEnlarge = (TextView) findViewById(R.id.txt_eyeEnlarge);
        txtBrighten = (TextView) findViewById(R.id.txt_eyBrighten);
        txtEyeEdit = (TextView) findViewById(R.id.txt_eyeEdit);
        txtEyeBag = (TextView) findViewById(R.id.txt_eyeBag);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1) {
                if (eyeModes.getVisibility() != 0) {
                    makeUpView.filterCanvas.drawBitmap(makeUpView.sessionBitmap, 0.0f, 0.0f, null);
                    makeUpView.invalidate();
                } else {
                    makeUpView.filterCanvas.drawBitmap(makeUpView.savedSessionBitmap, 0.0f, 0.0f, null);
                    makeUpView.invalidate();
                }
            }
        } else if (eyeModes.getVisibility() != 0) {
            makeUpView.filterCanvas.drawBitmap(makeUpView.savedSessionBitmap, 0.0f, 0.0f, null);
            makeUpView.invalidate();
        } else {
            makeUpView.filterCanvas.drawBitmap(sourceBitmap, 0.0f, 0.0f, null);
            makeUpView.invalidate();
        }
        return true;
    }

    public void onClick(View view) {
        if (beforeAfterEyeLayout.getVisibility() == View.INVISIBLE) {
            beforeAfterEyeLayout.setVisibility(View.VISIBLE);
            beforeAfterEyeLayout.startAnimation(slide_right_in);
        }
        int id = view.getId();
        if (id != R.id.closebtn) {
            switch (id) {
                case R.id.doneBtn:
                    MakeUpView makeUpView2 = makeUpView;
                    makeUpView2.savedSessionBitmap = BitmapFactory.decodeFile(makeUpView2.saveBitmap());
                    bottmLayout.setVisibility(View.INVISIBLE);
                    eyeModes.setVisibility(View.VISIBLE);
                    eyeModes.startAnimation(slide_up);
                    eye_seekBar.setMax(MAX_PROGRESS);
                    return;
                case R.id.doneFinal:
                    eye_seekBar.setMax(MAX_PROGRESS);
                    resultPath = makeUpView.saveBitmap();
                    Intent intent = new Intent();
                    intent.putExtra("filePath", resultPath);
                    intent.setData(Uri.fromFile(new File(resultPath)));
                    setResult(EYE_RESULT_CODE, intent);
                    finish();
                    return;
                default:
                    switch (id) {
                        case R.id.eyeBagLayout:
                            activeEyeBag();
                            eye_seekBar.setProgress(15);
                            eyeClick = 4;
                            if (mRecyclerView.getVisibility() == View.VISIBLE) {
                                mRecyclerView.setVisibility(View.GONE);
                            }
                            bottmLayout.setVisibility(View.VISIBLE);
                            if (eye_seekBar.getVisibility() == View.INVISIBLE) {
                                eye_seekBar.setVisibility(View.VISIBLE);
                            }
                            eyeModes.setVisibility(View.INVISIBLE);
                            bottmLayout.startAnimation(slide_up);
                            new DarkCircleTask(makeUpView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                            return;
                        case R.id.eyeBrightLayout:
                            activeEyeBrighten();
                            eye_seekBar.setProgress(15);
                            if (mRecyclerView.getVisibility() == View.VISIBLE) {
                                mRecyclerView.setVisibility(View.GONE);
                            }
                            if (eye_seekBar.getVisibility() == View.INVISIBLE) {
                                eye_seekBar.setVisibility(View.VISIBLE);
                            }
                            bottmLayout.setVisibility(View.VISIBLE);
                            eyeModes.setVisibility(View.INVISIBLE);
                            bottmLayout.startAnimation(slide_up);
                            eyeClick = 3;
                            new BrightenEyeAsyncTask(makeUpView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                            return;
                        case R.id.eyeColorLayout:
                            activeEyeColor();
                            eye_seekBar.setProgress(15);
                            if (mRecyclerView.getVisibility() == View.GONE || mRecyclerView.getVisibility() == View.INVISIBLE) {
                                mRecyclerView.setVisibility(View.VISIBLE);
                            }
                            ColorListAdapt colorListAdapter = new ColorListAdapt(mContext, BeautyCameraHelper.colorPaletteEyeColor, false);
                            mAdapter = colorListAdapter;
                            mRecyclerView.setAdapter(colorListAdapter);
                            eyeModes.setVisibility(View.INVISIBLE);
                            bottmLayout.setVisibility(View.VISIBLE);
                            if (eye_seekBar.getVisibility() == View.INVISIBLE) {
                                eye_seekBar.setVisibility(View.VISIBLE);
                            }
                            bottmLayout.startAnimation(slide_up);
                            beautyMode = BEAUTY_MODE.APPLY;
                            eyeClick = 1;
                            colorindex = 5;
                            new EyeColorAsyncTask(makeUpView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                            return;
                        case R.id.eyeEnlargeLayout:
                            activeEyeEnlarge();
                            eye_seekBar.setProgress(15);
                            if (mRecyclerView.getVisibility() == View.VISIBLE) {
                                mRecyclerView.setVisibility(View.GONE);
                            }
                            bottmLayout.setVisibility(View.VISIBLE);
                            if (eye_seekBar.getVisibility() == View.INVISIBLE) {
                                eye_seekBar.setVisibility(View.VISIBLE);
                            }
                            eyeModes.setVisibility(View.INVISIBLE);
                            bottmLayout.startAnimation(slide_up);
                            eyeClick = 2;
                            new EnlargeEyeAsyncTask(makeUpView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                            return;
                        default:
                            return;
                    }
            }
        } else {
            eye_seekBar.setMax(MAX_PROGRESS);
            if (eyeModes.getVisibility() == View.VISIBLE) {
                finish();
                return;
            }
            makeUpView.filterCanvas.drawBitmap(makeUpView.savedSessionBitmap, 0.0f, 0.0f, null);
            bottmLayout.setVisibility(View.INVISIBLE);
            eyeModes.setVisibility(View.VISIBLE);
            eyeModes.startAnimation(slide_up);
        }
    }

    private void activeEyeEnlarge() {
        imgEyeColor.setImageResource(R.drawable.eye_color);
        imgEnlarge.setImageResource(R.drawable.eye_enlarge_selected);
        imgBrighten.setImageResource(R.drawable.ic_brightness);
        imgEyeBag.setImageResource(R.drawable.eyebag);
    }

    private void activeEyeBrighten() {
        imgEyeColor.setImageResource(R.drawable.eye_color);
        imgEnlarge.setImageResource(R.drawable.eye_enlarge);
        imgBrighten.setImageResource(R.drawable.ic_brightness_selected);
        imgEyeBag.setImageResource(R.drawable.eyebag);
    }

    private void activeEyeColor() {
        imgEyeColor.setImageResource(R.drawable.eye_color_selected);
        imgEnlarge.setImageResource(R.drawable.eye_enlarge);
        imgBrighten.setImageResource(R.drawable.ic_brightness);
        imgEyeBag.setImageResource(R.drawable.eyebag);
    }

    private void activeEyeBag() {
        imgEyeColor.setImageResource(R.drawable.eye_color);
        imgEnlarge.setImageResource(R.drawable.eye_enlarge);
        imgBrighten.setImageResource(R.drawable.ic_brightness);
        imgEyeBag.setImageResource(R.drawable.eyebag_selected);
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public class BrightenEyeAsyncTask extends AsyncTask<Void, Void, Void> {
        MakeUpView f208bv;
        public BrightenEyeAsyncTask(MakeUpView makeUpView) {
            f208bv = makeUpView;
        }
        public Void doInBackground(Void... voidArr) {
            f208bv.progress = currentProgress;
            f208bv.sessionCanvas.drawBitmap(f208bv.savedSessionBitmap, 0.0f, 0.0f, null);
            if (f208bv.eyeLeftPath != null) {
                BeautyCameraHelper.brightenEyes(f208bv.sessionCanvas, f208bv.tmpCanvas, f208bv.savedSessionBitmap, f208bv.eyeLeftPath, f208bv.brightLeftRect, f208bv.BpaintPath, f208bv.BrectPaint, f208bv.paintBtm, f208bv.progress * INDEX_FX);
            }
            if (f208bv.eyeRightPath != null) {
                BeautyCameraHelper.brightenEyes(f208bv.sessionCanvas, f208bv.tmpCanvas, f208bv.savedSessionBitmap, f208bv.eyeRightPath, f208bv.brightRightRect, f208bv.BpaintPath, f208bv.BrectPaint, f208bv.paintBtm, f208bv.progress * INDEX_FX);
            }
            return null;
        }
        public void onPostExecute(Void voidR) {
            f208bv.applyFilter();
            f208bv.invalidate();
        }
    }

    public class DarkCircleTask extends AsyncTask<Void, Void, Void> {
        MakeUpView darkMuv;
        public DarkCircleTask(MakeUpView makeUpView) {
            darkMuv = makeUpView;
        }
        public Void doInBackground(Void... voidArr) {
            darkMuv.sessionCanvas.drawBitmap(darkMuv.savedSessionBitmap, 0.0f, 0.0f, null);
            Bitmap removeDarkCircles = BeautyCameraHelper.removeDarkCircles(darkMuv.tmpCanvas, darkMuv.leftEyeBag, darkMuv.leftEyeBagPath, darkMuv.BpaintPath, darkMuv.BrectPaint, currentProgress);
            if (removeDarkCircles != null && !removeDarkCircles.isRecycled()) {
                darkMuv.sessionCanvas.drawBitmap(removeDarkCircles, (float) darkMuv.leftRectBag.left, (float) darkMuv.leftRectBag.top, null);
                Bitmap removeDarkCircles2 = BeautyCameraHelper.removeDarkCircles(darkMuv.tmpCanvas, darkMuv.rightEyeBag, darkMuv.rightEyeBagPath, darkMuv.BpaintPath, darkMuv.BrectPaint, currentProgress);
                if (removeDarkCircles2 != null && !removeDarkCircles2.isRecycled()) {
                    darkMuv.sessionCanvas.drawBitmap(removeDarkCircles2, (float) darkMuv.rightRectBag.left, (float) darkMuv.rightRectBag.top, null);
                    removeDarkCircles2.recycle();
                }
            }
            return null;
        }
        public void onPostExecute(Void voidR) {
            darkMuv.applyFilter();
            darkMuv.invalidate();
        }
    }
    public class EnlargeEyeAsyncTask extends AsyncTask<Void, Void, Void> {
        MakeUpView eyeMuv;
        public EnlargeEyeAsyncTask(MakeUpView makeUpView) {
            eyeMuv = makeUpView;
        }
        public Void doInBackground(Void... voidArr) {
            eyeMuv.progress = currentProgress;
            eyeMuv.sessionCanvas.drawBitmap(eyeMuv.savedSessionBitmap, 0.0f, 0.0f, null);
            if (!eyeMuv.enlargeLeftRect.isEmpty()) {
                BeautyCameraHelper.enlargeEyes(eyeMuv.sessionCanvas, eyeMuv.sessionBitmap, eyeMuv.eyeLeftLandmarks, eyeMuv.enlargeLeftRect, eyeMuv.progress, eyeMuv.paintBtm);
            }
            if (!eyeMuv.enlargeRightRect.isEmpty()) {
                BeautyCameraHelper.enlargeEyes(eyeMuv.sessionCanvas, eyeMuv.sessionBitmap, eyeMuv.eyeRightLandmarks, eyeMuv.enlargeRightRect, eyeMuv.progress, eyeMuv.paintBtm);
            }
            return null;
        }
        public void onPostExecute(Void voidR) {
            eyeMuv.applyFilter();
            eyeMuv.invalidate();
        }
    }
    public class EyeColorAsyncTask extends AsyncTask<Void, Void, Void> {
        MakeUpView eyeColorMuv;
        public EyeColorAsyncTask(MakeUpView makeUpView) {
            eyeColorMuv = makeUpView;
        }
        public Void doInBackground(Void... voidArr) {
            eyeColorMuv.sessionCanvas.drawBitmap(eyeColorMuv.savedSessionBitmap, 0.0f, 0.0f, null);
            if (eyeColorMuv.eyeLensMaskPix == null) {
                return null;
            }
            BeautyCameraHelper.eyeColorBlendMultiply(eyeColorMuv.eyeLensMaskPix, BeautyCameraHelper.colorPaletteEyeColor[colorindex]);
            eyeColorMuv.eyeLensMask.setPixels(eyeColorMuv.eyeLensMaskPix, 0, eyeColorMuv.eyeLensMask.getWidth(), 0, 0, eyeColorMuv.eyeLensMask.getWidth(), eyeColorMuv.eyeLensMask.getHeight());
            if (eyeColorMuv.eyeLens != null && !eyeColorMuv.eyeLens.isRecycled()) {
                eyeColorMuv.eyeLensCanvas = new Canvas(eyeColorMuv.eyeLens);
                new Paint().setXfermode(new PorterDuffXfermode(Mode.OVERLAY));
                if (eyeColorMuv.eyeLensMask != null && !eyeColorMuv.eyeLensMask.isRecycled()) {
                    eyeColorMuv.eyeLensCanvas.drawBitmap(eyeColorMuv.eyeLensMask, 0.0f, 0.0f, null);
                    if (eyeColorMuv.eyeColorLeftBp != null && !eyeColorMuv.eyeColorLeftBp.isRecycled()) {
                        Canvas canvas = new Canvas(eyeColorMuv.eyeColorLeftBp);
                        canvas.drawBitmap(eyeColorMuv.savedSessionBitmap, eyeColorMuv.leftMaskRect, new Rect(0, 0, eyeColorMuv.leftMaskRect.width(), eyeColorMuv.leftMaskRect.height()), null);
                        if (!(eyeColorMuv.eyeLeftLandmarks == null || eyeColorMuv.eyeLeftLandmarks.size() == 0)) {
                            MakeUpView makeUpView = eyeColorMuv;
                            makeUpView.eyeLeftPath = BeautyCameraHelper.drawPath(makeUpView.eyeLeftLandmarks);
                            eyeColorMuv.eyeLeftPath.offset((float) (-eyeColorMuv.leftMaskRect.left), (float) (-eyeColorMuv.leftMaskRect.top));
                            eyeColorMuv.eyeLeftPath.setFillType(FillType.INVERSE_EVEN_ODD);
                            Paint paint = new Paint();
                            paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
                            canvas.drawPath(eyeColorMuv.eyeLeftPath, paint);
                            Paint paint2 = new Paint();
                            paint2.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
                            if (!(eyeColorMuv.eyeLens == null || eyeColorMuv.eyeLens.isRecycled() || eyeColorMuv.pupilLeft == null)) {
                                canvas.drawBitmap(eyeColorMuv.eyeLens, (float) ((eyeColorMuv.pupilLeft.x - (makeUpView.eyeRadius / INDEX_FX)) - eyeColorMuv.leftMaskRect.left), (float) ((eyeColorMuv.pupilLeft.y - (makeUpView.eyeRadius / INDEX_FX)) - eyeColorMuv.leftMaskRect.top), paint2);
                                eyeColorMuv.sessionCanvas.drawBitmap(eyeColorMuv.savedSessionBitmap, 0.0f, 0.0f, null);
                                Paint paint3 = new Paint();
                                eyeColorMuv.progress = currentProgress;
                                paint3.setAlpha(eyeColorMuv.progress * INDEX_COLOR);
                                paint3.setMaskFilter(new BlurMaskFilter(2.0f, Blur.NORMAL));
                                if (eyeColorMuv.eyeColorLeftBp != null && !eyeColorMuv.eyeColorLeftBp.isRecycled()) {
                                    eyeColorMuv.sessionCanvas.drawBitmap(eyeColorMuv.eyeColorLeftBp, (float) eyeColorMuv.leftMaskRect.left, (float) eyeColorMuv.leftMaskRect.top, paint3);
                                    if (eyeColorMuv.eyeColorRightBp != null && !eyeColorMuv.eyeColorRightBp.isRecycled()) {
                                        Canvas canvas2 = new Canvas(eyeColorMuv.eyeColorRightBp);
                                        canvas2.drawBitmap(eyeColorMuv.savedSessionBitmap, eyeColorMuv.rightMaskRect, new Rect(0, 0, eyeColorMuv.rightMaskRect.width(), eyeColorMuv.rightMaskRect.height()), null);
                                        if (!(eyeColorMuv.eyeRightLandmarks == null || eyeColorMuv.eyeRightLandmarks.size() == 0)) {
                                            MakeUpView makeUpView2 = eyeColorMuv;
                                            makeUpView2.eyeRightPath = BeautyCameraHelper.drawPath(makeUpView2.eyeRightLandmarks);
                                            eyeColorMuv.eyeRightPath.offset((float) (-eyeColorMuv.rightMaskRect.left), (float) (-eyeColorMuv.rightMaskRect.top));
                                            eyeColorMuv.eyeRightPath.setFillType(FillType.INVERSE_EVEN_ODD);
                                            canvas2.drawPath(eyeColorMuv.eyeRightPath, paint);
                                            if (!(eyeColorMuv.eyeLens == null || eyeColorMuv.eyeLens.isRecycled() || eyeColorMuv.pupilRight == null)) {
                                                canvas2.drawBitmap(eyeColorMuv.eyeLens, (float) ((eyeColorMuv.pupilRight.x - (makeUpView.eyeRadius / INDEX_FX)) - eyeColorMuv.rightMaskRect.left), (float) ((eyeColorMuv.pupilRight.y - (makeUpView.eyeRadius / INDEX_FX)) - eyeColorMuv.rightMaskRect.top), paint2);
                                                if (eyeColorMuv.eyeColorRightBp != null && !eyeColorMuv.eyeColorRightBp.isRecycled()) {
                                                    eyeColorMuv.sessionCanvas.drawBitmap(eyeColorMuv.eyeColorRightBp, (float) eyeColorMuv.rightMaskRect.left, (float) eyeColorMuv.rightMaskRect.top, paint3);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return null;
        }
        public void onPostExecute(Void voidR) {
            eyeColorMuv.applyFilter();
            eyeColorMuv.invalidate();
        }
    }
    public class FaceDetectionTask extends AsyncTask<Void, Void, Boolean> {
        MakeUpView faceMuv;
        boolean state;
        public FaceDetectionTask(MakeUpView makeUpView, boolean z) {
            faceMuv = makeUpView;
            state = z;
        }
        public Boolean doInBackground(Void... voidArr) {
            try {
                faceLandmarks = FaceEditorMainAct.faceLandmarks;
                int[] iArr = FaceEditorMainAct.faceRects;
                faceRect = new int[4];
                int i = 0;
                for (int i2 = 0; i2 < iArr.length / 4; i2++) {
                    int i3 = i2 * 4;
                    int i4 = i3 + 2;
                    int i5 = i3 + 3;
                    int i6 = i3 + 1;
                    int i7 = (iArr[i4] - iArr[i3]) * (iArr[i5] - iArr[i6]);
                    if (i7 > i) {
                        faceRect[0] = Math.max(0, iArr[i3]);
                        faceRect[1] = Math.min(iArr[i6], sourceBitmap.getWidth());
                        faceRect[2] = Math.max(0, iArr[i4]);
                        faceRect[3] = Math.min(iArr[i5], sourceBitmap.getHeight());
                        i = i7;
                    }
                }
                faceMuv.face = new Face(faceLandmarks, faceRect);
                faceMuv.eyeLeftLandmarks.addAll(faceMuv.face.getLeftEyeLandmarks());
                faceMuv.eyeRightLandmarks.addAll(faceMuv.face.getRightEyeLandmarks());
                faceMuv.faceRect = faceMuv.face.getFaceRect();
                faceMuv.faceRect.left = Math.max(0, faceMuv.faceRect.left - (faceMuv.faceRect.width() / 8));
                faceMuv.faceRect.top = Math.max(0, faceMuv.faceRect.top - (faceMuv.faceRect.height() / INDEX_FX));
                faceMuv.faceRect.right = Math.max(0, Math.min(sourceBitmap.getWidth(), faceMuv.faceRect.right + (faceMuv.faceRect.width() / 8)));
                faceMuv.faceRect.bottom = Math.max(0, Math.min(sourceBitmap.getHeight(), faceMuv.faceRect.bottom + (faceMuv.faceRect.height() / 8)));
            } catch (Exception unused) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, "Could not find eyes...", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
            return Boolean.valueOf(true);
        }
        public void onPreExecute() {
            isFaceDetecting = true;
        }
        public void onPostExecute(Boolean bool) {
            if (bool.booleanValue()) {
                isFaceDetected = true;
                isFaceDetecting = false;
                isFaceDetected = false;
                if (!makeUpView.isInitEyeColor) {
                    makeUpView.EyeColorInit();
                }
                if (!makeUpView.isInitEnlarge) {
                    makeUpView.EnlargeEyesInit();
                }
                if (!makeUpView.isInitBrighten) {
                    makeUpView.BrightenEyesInit();
                }
                if (!makeUpView.isInitDarkCircle) {
                    makeUpView.DarkCirclesInit();
                }
            }
        }
    }
}

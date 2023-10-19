package com.eagleapps.beautycam.act;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eagleapps.beautycam.R;
import com.eagleapps.beautycam.draw.ImgViewTouch;
import com.eagleapps.beautycam.draw.ImgViewTouchAndDraw;
import com.eagleapps.beautycam.draw.ImgViewTouchAndDraw.TouchMode;
import com.eagleapps.beautycam.helper.Constant;
import com.eagleapps.beautycam.remote.SupporterClass;
import com.eagleapps.beautycam.utilsApp.GPUImgFilterTool;
import com.yalantis.ucrop.view.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.co.cyberagent.android.gpuimage.GPUImage;

public class WhittenFaceEditAct extends BaseAct implements OnTouchListener {
    public static Bitmap whittencolorBitmap;
    public Bitmap bitmap = null;
    public Boolean btnOnclick = Boolean.valueOf(false);
    public String imagePath;
    public boolean isZoomRequired = false;
    protected float f220mX;
    protected float f221mY;
    protected Path tmpPath;
    Context context;
    private Bitmap Colorized = null;
    private int WHITTEN_RESULT_CODE = CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION;
    private LinearLayout btnzoom;
    private int currentMode = 1;
    private LinearLayout erasor;
    private int imageViewHeight = 0;
    private int imageViewWidth = 0;
    private ImageView imgWhittenApply;
    private ImageView imgWhittenErase;
    private ImageView imgWhittenZoom;
    private boolean isFirstTimeLaunch = false;
    private File isfile;
    private ImgViewTouch mImageView;
    private ImgViewTouch mImageView1;
    private Paint mPaint;
    private Canvas myCombineCanvas = null;
    private Bitmap myCombinedBitmap = null;
    private Context myContext;
    private Canvas pcanvas;
    private Bitmap topImage = null;
    private TextView txtWhittenApply;
    private TextView txtWhittenEdit;
    private TextView txtWhittenErase;
    private TextView txtWhittenZoom;
    private LinearLayout whitten;
    private LinearLayout whittenTeethDone;

    public static float[] getMatrixValues(Matrix matrix) {
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        return fArr;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_whitten_face_edit);

        RelativeLayout adViewBanner = findViewById(R.id.rel_banner_ads);
        SupporterClass.loadBannerAds(adViewBanner, WhittenFaceEditAct.this);

        context = this;
        String stringExtra = getIntent().getStringExtra("imagePath");
        imagePath = stringExtra;
        try {
            Bitmap decodeFile = BitmapFactory.decodeFile(stringExtra);
            topImage = decodeFile;
            imageViewWidth = decodeFile.getWidth();
            imageViewHeight = topImage.getHeight();
            btnzoom = (LinearLayout) findViewById(R.id.whitten_button_zoom_img);
            whitten = (LinearLayout) findViewById(R.id.whitten_button_apply_whitten);
            erasor = (LinearLayout) findViewById(R.id.whitten_button_erasor_img);
            mImageView = (ImgViewTouchAndDraw) findViewById(R.id.whittenimg);
            mImageView1 = (ImgViewTouchAndDraw) findViewById(R.id.whittenimg1);
            whittenTeethDone = (LinearLayout) findViewById(R.id.whittenteeth_done_btn);
            mImageView1.setOnTouchListener(this);
            myContext = getBaseContext();
            Colorized = toColor(topImage);
            initFunction();
            init();
            whitten.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeWhittenApply();
                    btnOnclick = Boolean.valueOf(true);
                    isZoomRequired = false;
                    drawMode(1, 1);
                }
            });
            erasor.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeWhittenErase();
                    btnOnclick = Boolean.valueOf(true);
                    isZoomRequired = false;
                    drawMode(1, 2);
                }
            });
            whittenTeethDone.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (btnOnclick.booleanValue()) {
                        WhittenFaceEditAct whittenFaceActivity = WhittenFaceEditAct.this;
                        whittenFaceActivity.bitmap = whittenFaceActivity.createFinalImage();
                        WhittenFaceEditAct whittenFaceActivity2 = WhittenFaceEditAct.this;
                        whittenFaceActivity2.imagePath = whittenFaceActivity2.saveBitmap(whittenFaceActivity2.bitmap);
//                        file = new File(whittenFaceActivity2.saveBitmap(whittenFaceActivity2.bitmap));
                    }
                    WhittenFaceEditAct whittenFaceActivity3 = WhittenFaceEditAct.this;
                    whittenFaceActivity3.SendURL(whittenFaceActivity3.imagePath);
//                    whittenFaceActivity3.SendURL(file);
                }
            });
            btnzoom.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeWhittenZoom();
                    if (!isZoomRequired) {
                        isZoomRequired = true;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Image not supported ", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void activeWhittenErase() {
        imgWhittenZoom.setImageResource(R.drawable.ic_zoom);
        imgWhittenApply.setImageResource(R.drawable.ic_whitten);
        imgWhittenErase.setImageResource(R.drawable.ic_eraser_selected);
    }

    public void activeWhittenZoom() {
        imgWhittenZoom.setImageResource(R.drawable.ic_zoom_selected);
        imgWhittenApply.setImageResource(R.drawable.ic_whitten);
        imgWhittenErase.setImageResource(R.drawable.ic_eraser);
    }

    public void activeWhittenApply() {
        imgWhittenZoom.setImageResource(R.drawable.ic_zoom);
        imgWhittenApply.setImageResource(R.drawable.ic_whitten_selected);
        imgWhittenErase.setImageResource(R.drawable.ic_eraser);
    }

    private void init() {
        imgWhittenZoom = (ImageView) findViewById(R.id.img_whittenZoom);
        imgWhittenApply = (ImageView) findViewById(R.id.img_whittenApply);
        imgWhittenErase = (ImageView) findViewById(R.id.img_whittenErase);
        txtWhittenZoom = (TextView) findViewById(R.id.txt_whittenZoom);
        txtWhittenApply = (TextView) findViewById(R.id.txt_whittenApply);
        txtWhittenErase = (TextView) findViewById(R.id.txt_whittenErase);
        txtWhittenEdit = (TextView) findViewById(R.id.txt_whittenEdit);
    }

    public void SendURL(String str) {
        File file = new File(str);
        if (file.exists()) {
            Uri fromFile = Uri.fromFile(file);
            Intent intent = new Intent();
            intent.setData(fromFile);
            if (btnOnclick.booleanValue()) {
                setResult(WHITTEN_RESULT_CODE, intent);
            } else {
                setResult(0, intent);
            }
            btnOnclick = Boolean.valueOf(false);
            finish();
        }
    }

    public void combinedTopImage(Bitmap bitmap2, Bitmap bitmap3) {
        Bitmap bitmap4 = myCombinedBitmap;
        if (bitmap4 != null) {
            bitmap4.recycle();
            myCombinedBitmap = null;
        }
        myCombinedBitmap = Bitmap.createBitmap(imageViewWidth, imageViewHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas();
        myCombineCanvas = canvas;
        canvas.setBitmap(myCombinedBitmap);
        myCombineCanvas.drawBitmap(bitmap2, 0.0f, 0.0f, null);
        myCombineCanvas.drawBitmap(bitmap3, 0.0f, 0.0f, null);
        Bitmap bitmap5 = bitmap;
        if (bitmap5 != null) {
            bitmap5.recycle();
            bitmap = null;
            bitmap = Bitmap.createBitmap(imageViewWidth, imageViewHeight, Config.ARGB_8888);
        }
        Canvas canvas2 = new Canvas();
        pcanvas = canvas2;
        canvas2.setBitmap(bitmap);
        pcanvas.drawBitmap(myCombinedBitmap, 0.0f, 0.0f, null);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        btnOnclick = Boolean.valueOf(true);
        ImgViewTouchAndDraw imageViewTouchAndDraw = (ImgViewTouchAndDraw) view;
        if (isZoomRequired) {
            imageViewTouchAndDraw.setScaleType(ScaleType.MATRIX);
            mImageView.setScaleType(ScaleType.MATRIX);
            try {
                ((ImgViewTouchAndDraw) mImageView).setDrawMode(TouchMode.IMAGE);
                ((ImgViewTouchAndDraw) mImageView1).setDrawMode(TouchMode.IMAGE);
                mImageView.onTouchEvent(motionEvent);
                return imageViewTouchAndDraw.onTouchEvent(motionEvent);
            } catch (IllegalArgumentException unused) {
                return false;
            }
        } else {
            Matrix matrix = new Matrix();
            Matrix matrix2 = new Matrix(imageViewTouchAndDraw.getImageMatrix());
            matrix.reset();
            float[] matrixValues = getMatrixValues(matrix2);
            matrix2.invert(matrix2);
            float[] matrixValues2 = getMatrixValues(matrix2);
            matrix.postTranslate(-matrixValues[2], -matrixValues[5]);
            matrix.postScale(matrixValues2[0], matrixValues2[4]);
            pcanvas.setMatrix(matrix);
            int action = motionEvent.getAction();
            String str = "onTouchEvent";
            if (action == 0) {
                touch_start(motionEvent.getX(), motionEvent.getY());
                Log.e(str, "000000000000000");
            } else if (action == 1) {
                touch_up();
                Log.e(str, "1111111111111");
            } else if (action == 2) {
                Log.e(str, "2222222222222");
                touch_move(motionEvent.getX(), motionEvent.getY());
            }
            imageViewTouchAndDraw.setImageBitmap(bitmap);
            return true;
        }
    }

    public void drawMode(int i, int i2) {
        if (isFirstTimeLaunch) {
            isFirstTimeLaunch = false;
            if (i2 == 1) {
                mImageView.setImageBitmapReset(Colorized, true);
                mImageView1.setImageBitmapReset(bitmap, true);
            } else if (i2 == 2) {
                mImageView.setImageBitmapReset(topImage, true);
                mImageView1.setImageBitmapReset(bitmap, true);
            }
            return;
        }
        if (i != 0) {
            combinedTopImage(((BitmapDrawable) mImageView.getDrawable()).getBitmap(), ((BitmapDrawable) mImageView1.getDrawable()).getBitmap());
        }
        if (i2 == 1) {
            mImageView.setImageBitmap(Colorized);
            mImageView1.setImageBitmap(bitmap);
            mImageView1.setOnTouchListener(this);
        } else if (i2 == 2) {
            mImageView.setImageBitmap(topImage);
            mImageView1.setImageBitmap(bitmap);
            mImageView1.setOnTouchListener(this);
        }
    }

    public void initFunction() {
        Paint paint = new Paint();
        mPaint = paint;
        paint.setAlpha(0);
        mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeJoin(Join.ROUND);
        mPaint.setStrokeCap(Cap.ROUND);
        mPaint.setStrokeWidth(30.0f);
        mPaint.setMaskFilter(new BlurMaskFilter(15.0f, Blur.NORMAL));
        mPaint.setFilterBitmap(false);
        tmpPath = new Path();
        bitmap = Bitmap.createBitmap(imageViewWidth, imageViewHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas();
        pcanvas = canvas;
        canvas.setBitmap(bitmap);
        pcanvas.drawBitmap(topImage, 0.0f, 0.0f, null);
        isFirstTimeLaunch = true;
        drawMode(0, currentMode);
    }

    public Bitmap toColor(Bitmap bitmap2) {
        try {
            GPUImgFilterTool gPUImageFilterTools = new GPUImgFilterTool();
            GPUImage gPUImage = new GPUImage(this);
            gPUImage.setFilter(gPUImageFilterTools.ApplyColorBlending(context));
            gPUImage.setImage(bitmap2);
            return gPUImage.getBitmapWithFilterApplied(bitmap2);
        } catch (Exception | OutOfMemoryError unused) {
            return null;
        }
    }

    private void touch_start(float f, float f2) {
        tmpPath.reset();
        tmpPath.moveTo(f, f2);
        f220mX = f;
        f221mY = f2;
        mPaint.setStrokeWidth(55.0f);
    }

    private void touch_move(float f, float f2) {
        float abs = Math.abs(f - f220mX);
        float abs2 = Math.abs(f2 - f221mY);
        if (abs >= 1.0f || abs2 >= 1.0f) {
            Path path = tmpPath;
            float f3 = f220mX;
            float f4 = f221mY;
            path.quadTo(f3, f4, (f3 + f) / 2.0f, (f4 + f2) / 2.0f);
            pcanvas.drawPath(tmpPath, mPaint);
            tmpPath.reset();
            tmpPath.moveTo((f220mX + f) / 2.0f, (f221mY + f2) / 2.0f);
            f220mX = f;
            f221mY = f2;
        }
    }

    private void touch_up() {
        tmpPath.reset();
    }


    public Bitmap createFinalImage() {
        Bitmap createBitmap = Bitmap.createBitmap(topImage.getWidth(), topImage.getHeight(), Config.ARGB_8888);
        Bitmap bitmap2 = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
        Canvas canvas = new Canvas();
        canvas.setBitmap(createBitmap);
        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, null);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        return createBitmap;
    }

    public void onDestroy() {
        super.onDestroy();
        Bitmap bitmap2 = bitmap;
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
            System.gc();
        }
        Bitmap bitmap3 = whittencolorBitmap;
        if (bitmap3 != null && !bitmap3.isRecycled()) {
            whittencolorBitmap.recycle();
            whittencolorBitmap = null;
            System.gc();
        }
        Bitmap bitmap4 = myCombinedBitmap;
        if (bitmap4 != null && !bitmap4.isRecycled()) {
            myCombinedBitmap.recycle();
            myCombinedBitmap = null;
            System.gc();
        }
        Bitmap bitmap5 = Colorized;
        if (bitmap5 != null && !bitmap5.isRecycled()) {
            Colorized.recycle();
            Colorized = null;
            System.gc();
        }
        Bitmap bitmap6 = topImage;
        if (bitmap6 != null && !bitmap6.isRecycled()) {
            topImage.recycle();
            topImage = null;
            System.gc();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        setResult(0, new Intent());
        btnOnclick = Boolean.valueOf(false);
        finish();
    }

    public String saveBitmap(Bitmap bitmap2) {
        File file = null;
        String folder = Constant.TEMP_FOLDER_NAME;

        ContextWrapper cw = new ContextWrapper(context);
        File pictureFileDir = cw.getDir(folder, Context.MODE_PRIVATE);


        try {
            StringBuilder sb = new StringBuilder();
            sb.append(folder);
            sb.append(".jpg");
            imagePath = sb.toString();
            file = new File(pictureFileDir, imagePath);
            isfile = file;
            if (file.exists()) {
                isfile.delete();
                isfile.createNewFile();
            } else {
                isfile.createNewFile();
            }
            FileOutputStream oStream = new FileOutputStream(file);
            bitmap2.compress(CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        return String.valueOf(file);
    }

    public void onBack(View view) {
        onBackPressed();
    }
}

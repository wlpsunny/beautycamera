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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import com.eagleapps.beautycam.R;
import com.eagleapps.beautycam.draw.ImgViewTouch;
import com.eagleapps.beautycam.draw.ImgViewTouchAndDraw;
import com.eagleapps.beautycam.draw.ImgViewTouchAndDraw.TouchMode;
import com.eagleapps.beautycam.helper.Constant;
import com.eagleapps.beautycam.remote.SupporterClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;

public class FaceBlurEditAct extends BaseAct implements OnTouchListener, OnSeekBarChangeListener {
    private int BLUR_RESULT_CODE = 400;
    public Boolean BtnClick = Boolean.valueOf(false);
    private Bitmap Colorized = null;
    public Bitmap bitmap = null;
    public SeekBar blrSeekBar;
    private int blrValue = 0;
    private LinearLayout blurDone;
    private ImgViewTouch blurImg;
    private ImgViewTouch blurImg1;
    private LinearLayout blur_apply_btn;
    private LinearLayout blur_btnzoom;
    private LinearLayout blur_erasor;
    private int currentMode = 1;
    public String imagePath;
    private int imageViewHeight = 0;
    private int imageViewWidth = 0;
    private ImageView imgBlurApply;
    private ImageView imgBlurErase;
    private ImageView imgBlurZoom;
    private boolean isFirstTimeLaunch = false;
    boolean isZoomRequired = false;
    private File isfile;
    private Paint mPaint;
    private float f213mX;
    private float f214mY;
    private Canvas myCombineCanvas = null;
    private Bitmap myCombinedBitmap = null;
    private Canvas pcanvas;
    private Path tmpPath;
    private Bitmap topImage = null;
    private TextView txtBluEdit;
    private TextView txtBlurApply;
    private TextView txtBlurErase;
    private TextView txtBlurZoom;
    File file = null;

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_face_blur_edit);

        RelativeLayout adViewBanner = findViewById(R.id.rel_banner_ads);
        SupporterClass.loadBannerAds(adViewBanner, FaceBlurEditAct.this);

        String stringExtra = getIntent().getStringExtra("imagePath");
        imagePath = stringExtra;
        try {
            Bitmap decodeFile = BitmapFactory.decodeFile(stringExtra);
            topImage = decodeFile;
            imageViewWidth = decodeFile.getWidth();
            imageViewHeight = topImage.getHeight();
            blur_btnzoom = (LinearLayout) findViewById(R.id.blur_zoom);
            blur_apply_btn = (LinearLayout) findViewById(R.id.blur_apply_blur);
            blur_erasor = (LinearLayout) findViewById(R.id.blur_eraser);
            blurImg = (ImgViewTouchAndDraw) findViewById(R.id.blur_image);
            blurImg1 = (ImgViewTouchAndDraw) findViewById(R.id.blur_image1);
            blurDone = (LinearLayout) findViewById(R.id.blur_done_btn);
            blurImg1.setOnTouchListener(this);
            SeekBar seekBar = (SeekBar) findViewById(R.id.blur_seekbar);
            blrSeekBar = seekBar;
            seekBar.setOnSeekBarChangeListener(this);
            blrSeekBar.setProgress(1);
            blrSeekBar.setMax(70);
            blrSeekBar.setVisibility(View.GONE);
            Colorized = fastblur(topImage, 70);
            initFunction();
            init();
            blrSeekBar.setVisibility(View.VISIBLE);
            blur_apply_btn.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeBlurApply();
                    BtnClick = Boolean.valueOf(true);
                    isZoomRequired = false;
                    blrSeekBar.setVisibility(View.VISIBLE);
                    drawMode(1, 1);
                }
            });
            blur_erasor.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeBlurErase();
                    isZoomRequired = false;
                    BtnClick = Boolean.valueOf(true);
                    drawMode(1, 2);
                    blrSeekBar.setVisibility(View.GONE);
                }
            });
            blurDone.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    blrSeekBar.setVisibility(View.GONE);
                    if (BtnClick.booleanValue()) {
                        bitmap = createFinalImage();
                        imagePath = saveBitmap(bitmap);
//                        file = new File(faceBlurActivity2.saveBitmap(faceBlurActivity2.bitmap));
                    }
                    SendURL(imagePath);
//                    faceBlurActivity3.SendURL(file);
                }
            });
            blur_btnzoom.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeBlurZoom();
                    blrSeekBar.setVisibility(View.GONE);
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
    public void activeBlurErase() {
        imgBlurZoom.setImageResource(R.drawable.ic_zoom);
        imgBlurApply.setImageResource(R.drawable.ic_blur);
        imgBlurErase.setImageResource(R.drawable.ic_eraser_selected);
    }

    public void activeBlurZoom() {
        imgBlurZoom.setImageResource(R.drawable.ic_zoom_selected);
        imgBlurApply.setImageResource(R.drawable.ic_blur);
        imgBlurErase.setImageResource(R.drawable.ic_eraser);
    }

    public void activeBlurApply() {
        imgBlurZoom.setImageResource(R.drawable.ic_zoom);
        imgBlurApply.setImageResource(R.drawable.ic_blur_selected);
        imgBlurErase.setImageResource(R.drawable.ic_eraser);
    }

    private void init() {
        imgBlurZoom = (ImageView) findViewById(R.id.img_blurZoom);
        imgBlurApply = (ImageView) findViewById(R.id.img_blurApply);
        imgBlurErase = (ImageView) findViewById(R.id.img_blurErase);
        txtBlurZoom = (TextView) findViewById(R.id.txt_blurZoom);
        txtBlurApply = (TextView) findViewById(R.id.txt_blurApply);
        txtBlurErase = (TextView) findViewById(R.id.txt_blurErase);
        txtBluEdit = (TextView) findViewById(R.id.txt_blurEdit);
    }

    public void SendURL(String str) {
        File file = new File(str);
        if (file.exists()) {
            Uri fromFile = Uri.fromFile(file);
            Intent intent = new Intent();
            intent.setData(fromFile);
            intent.putExtra("filePath",file);
            intent.putExtra("path", imagePath);
            if (BtnClick.booleanValue()) {
                setResult(BLUR_RESULT_CODE, intent);
            } else {
                setResult(0, intent);
            }
            BtnClick = Boolean.valueOf(false);
            finish();
        }
    }

    public Bitmap fastblur(Bitmap bitmap2, int i) {
        int[] iArr;
        int i2 = i;
        Bitmap copy = bitmap2.copy(bitmap2.getConfig(), true);
        if (i2 < 1) {
            return null;
        }
        int width = copy.getWidth();
        int height = copy.getHeight();
        int i3 = width * height;
        int[] iArr2 = new int[i3];
        StringBuilder sb = new StringBuilder();
        sb.append(width);
        String str = " ";
        sb.append(str);
        sb.append(height);
        sb.append(str);
        sb.append(i3);
        String str2 = "pix";
        Log.e(str2, sb.toString());
        String str3 = str2;
        copy.getPixels(iArr2, 0, width, 0, 0, width, height);
        int i4 = width - 1;
        int i5 = height - 1;
        int i6 = i2 + i2 + 1;
        int[] iArr3 = new int[i3];
        int[] iArr4 = new int[i3];
        int[] iArr5 = new int[i3];
        int[] iArr6 = new int[Math.max(width, height)];
        int i7 = (i6 + 1) >> 1;
        int i8 = i7 * i7;
        int i9 = i8 * 256;
        Bitmap bitmap3 = copy;
        int[] iArr7 = new int[i9];
        int i10 = i3;
        for (int i11 = 0; i11 < i9; i11++) {
            iArr7[i11] = i11 / i8;
        }
        int[][] iArr8 = (int[][]) Array.newInstance(Integer.TYPE, new int[]{i6, 3});
        int i12 = i2 + 1;
        int i13 = 0;
        int i14 = 0;
        int i15 = 0;
        while (i13 < height) {
            String str4 = str;
            int i16 = -i2;
            int i17 = 0;
            int i18 = 0;
            int i19 = 0;
            int i20 = 0;
            int i21 = 0;
            int i22 = 0;
            int i23 = 0;
            int i24 = 0;
            int i25 = 0;
            while (i16 <= i2) {
                int i26 = i5;
                int i27 = height;
                int i28 = iArr2[i14 + Math.min(i4, Math.max(i16, 0))];
                int[] iArr9 = iArr8[i16 + i2];
                iArr9[0] = (i28 & 16711680) >> 16;
                iArr9[1] = (i28 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                iArr9[2] = i28 & 255;
                int abs = i12 - Math.abs(i16);
                i17 += iArr9[0] * abs;
                i18 += iArr9[1] * abs;
                i19 += iArr9[2] * abs;
                if (i16 > 0) {
                    i23 += iArr9[0];
                    i24 += iArr9[1];
                    i25 += iArr9[2];
                } else {
                    i20 += iArr9[0];
                    i21 += iArr9[1];
                    i22 += iArr9[2];
                }
                i16++;
                height = i27;
                i5 = i26;
            }
            int i29 = i5;
            int i30 = height;
            int i31 = i2;
            int i32 = 0;
            while (i32 < width) {
                iArr3[i14] = iArr7[i17];
                iArr4[i14] = iArr7[i18];
                iArr5[i14] = iArr7[i19];
                int i33 = i17 - i20;
                int i34 = i18 - i21;
                int i35 = i19 - i22;
                int[] iArr10 = iArr8[((i31 - i2) + i6) % i6];
                int i36 = i20 - iArr10[0];
                int i37 = i21 - iArr10[1];
                int i38 = i22 - iArr10[2];
                if (i13 == 0) {
                    iArr = iArr7;
                    iArr6[i32] = Math.min(i32 + i2 + 1, i4);
                } else {
                    iArr = iArr7;
                }
                int i39 = iArr2[i15 + iArr6[i32]];
                iArr10[0] = (i39 & 16711680) >> 16;
                iArr10[1] = (i39 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                iArr10[2] = i39 & 255;
                int i40 = i23 + iArr10[0];
                int i41 = i24 + iArr10[1];
                int i42 = i25 + iArr10[2];
                i17 = i33 + i40;
                i18 = i34 + i41;
                i19 = i35 + i42;
                i31 = (i31 + 1) % i6;
                int[] iArr11 = iArr8[i31 % i6];
                i20 = i36 + iArr11[0];
                i21 = i37 + iArr11[1];
                i22 = i38 + iArr11[2];
                i23 = i40 - iArr11[0];
                i24 = i41 - iArr11[1];
                i25 = i42 - iArr11[2];
                i14++;
                i32++;
                iArr7 = iArr;
            }
            int[] iArr12 = iArr7;
            i15 += width;
            i13++;
            str = str4;
            height = i30;
            i5 = i29;
        }
        int[] iArr13 = iArr7;
        int i43 = i5;
        int i44 = height;
        String str5 = str;
        int i45 = 0;
        while (i45 < width) {
            int i46 = -i2;
            int i47 = i46 * width;
            int i48 = 0;
            int i49 = 0;
            int i50 = 0;
            int i51 = 0;
            int i52 = 0;
            int i53 = 0;
            int i54 = 0;
            int i55 = 0;
            int i56 = 0;
            while (i46 <= i2) {
                int[] iArr14 = iArr6;
                int max = Math.max(0, i47) + i45;
                int[] iArr15 = iArr8[i46 + i2];
                iArr15[0] = iArr3[max];
                iArr15[1] = iArr4[max];
                iArr15[2] = iArr5[max];
                int abs2 = i12 - Math.abs(i46);
                i53 += iArr3[max] * abs2;
                i52 += iArr4[max] * abs2;
                i51 += iArr5[max] * abs2;
                if (i46 > 0) {
                    i50 += iArr15[0];
                    i48 += iArr15[1];
                    i49 += iArr15[2];
                } else {
                    i54 += iArr15[0];
                    i55 += iArr15[1];
                    i56 += iArr15[2];
                }
                int i57 = i43;
                if (i46 < i57) {
                    i47 += width;
                }
                i46++;
                i43 = i57;
                iArr6 = iArr14;
            }
            int[] iArr16 = iArr6;
            int i58 = i43;
            int i59 = i2;
            int i60 = i45;
            int i61 = i44;
            int i62 = 0;
            while (i62 < i61) {
                iArr2[i60] = (iArr2[i60] & ViewCompat.MEASURED_STATE_MASK) | (iArr13[i53] << 16) | (iArr13[i52] << 8) | iArr13[i51];
                int i63 = i53 - i54;
                int i64 = i52 - i55;
                int i65 = i51 - i56;
                int[] iArr17 = iArr8[((i59 - i2) + i6) % i6];
                int i66 = i54 - iArr17[0];
                int i67 = i55 - iArr17[1];
                int i68 = i56 - iArr17[2];
                if (i45 == 0) {
                    iArr16[i62] = Math.min(i62 + i12, i58) * width;
                }
                int i69 = iArr16[i62] + i45;
                iArr17[0] = iArr3[i69];
                iArr17[1] = iArr4[i69];
                iArr17[2] = iArr5[i69];
                int i70 = i50 + iArr17[0];
                int i71 = i48 + iArr17[1];
                int i72 = i49 + iArr17[2];
                i53 = i63 + i70;
                i52 = i64 + i71;
                i51 = i65 + i72;
                i59 = (i59 + 1) % i6;
                int[] iArr18 = iArr8[i59];
                i54 = i66 + iArr18[0];
                i55 = i67 + iArr18[1];
                i56 = i68 + iArr18[2];
                i50 = i70 - iArr18[0];
                i48 = i71 - iArr18[1];
                i49 = i72 - iArr18[2];
                i60 += width;
                i62++;
                i2 = i;
            }
            i45++;
            i2 = i;
            i44 = i61;
            i43 = i58;
            iArr6 = iArr16;
        }
        int i73 = i44;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(width);
        String str6 = str5;
        sb2.append(str6);
        sb2.append(i73);
        sb2.append(str6);
        sb2.append(i10);
        Log.e(str3, sb2.toString());
        bitmap3.setPixels(iArr2, 0, width, 0, 0, width, i73);
        return bitmap3;
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

    public static float[] getMatrixValues(Matrix matrix) {
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        return fArr;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        BtnClick = Boolean.valueOf(true);
        ImgViewTouchAndDraw imageViewTouchAndDraw = (ImgViewTouchAndDraw) view;
        if (isZoomRequired) {
            imageViewTouchAndDraw.setScaleType(ScaleType.MATRIX);
            blurImg.setScaleType(ScaleType.MATRIX);
            try {
                ((ImgViewTouchAndDraw) blurImg).setDrawMode(TouchMode.IMAGE);
                ((ImgViewTouchAndDraw) blurImg1).setDrawMode(TouchMode.IMAGE);
                blurImg.onTouchEvent(motionEvent);
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
            if (action == 0) {
                touch_start(motionEvent.getX(), motionEvent.getY());
            } else if (action == 1) {
                touch_up();
            } else if (action == 2) {
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
                blurImg.setImageBitmapReset(Colorized, true);
                blurImg1.setImageBitmapReset(bitmap, true);
            } else if (i2 == 2) {
                blurImg.setImageBitmap(topImage);
                blurImg1.setImageBitmap(bitmap);
            }
            return;
        }
        if (i != 0) {
            combinedTopImage(((BitmapDrawable) blurImg.getDrawable()).getBitmap(), ((BitmapDrawable) blurImg1.getDrawable()).getBitmap());
        }
        if (i2 == 1) {
            blurImg.setImageBitmap(Colorized);
            blurImg1.setImageBitmap(bitmap);
            blurImg1.setOnTouchListener(this);
        } else if (i2 == 2) {
            blurImg.setImageBitmap(topImage);
            blurImg1.setImageBitmap(bitmap);
            blurImg1.setOnTouchListener(this);
        }
    }

    public void initFunction() {
        Paint paint = new Paint();
        mPaint = paint;
        paint.setAlpha(0);
        mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(55.0f);
        mPaint.setStrokeJoin(Join.ROUND);
        mPaint.setStrokeCap(Cap.ROUND);
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

    private void touch_start(float f, float f2) {
        tmpPath.reset();
        tmpPath.moveTo(f, f2);
        f213mX = f;
        f214mY = f2;
        mPaint.setStrokeWidth(55.0f);
    }

    private void touch_move(float f, float f2) {
        float abs = Math.abs(f - f213mX);
        float abs2 = Math.abs(f2 - f214mY);
        if (abs >= 1.0f || abs2 >= 1.0f) {
            Path path = tmpPath;
            float f3 = f213mX;
            float f4 = f214mY;
            path.quadTo(f3, f4, (f3 + f) / 2.0f, (f4 + f2) / 2.0f);
            pcanvas.drawPath(tmpPath, mPaint);
            tmpPath.reset();
            tmpPath.moveTo((f213mX + f) / 2.0f, (f214mY + f2) / 2.0f);
            f213mX = f;
            f214mY = f2;
        }
    }

    private void touch_up() {
        tmpPath.reset();
    }


    public Bitmap createFinalImage() {
        Bitmap createBitmap = Bitmap.createBitmap(topImage.getWidth(), topImage.getHeight(), Config.ARGB_8888);
        Bitmap bitmap2 = ((BitmapDrawable) blurImg.getDrawable()).getBitmap();
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
        Bitmap bitmap3 = myCombinedBitmap;
        if (bitmap3 != null && !bitmap3.isRecycled()) {
            myCombinedBitmap.recycle();
            myCombinedBitmap = null;
            System.gc();
        }
        Bitmap bitmap4 = Colorized;
        if (bitmap4 != null && !bitmap4.isRecycled()) {
            Colorized.recycle();
            Colorized = null;
            System.gc();
        }
        Bitmap bitmap5 = topImage;
        if (bitmap5 != null && !bitmap5.isRecycled()) {
            topImage.recycle();
            topImage = null;
            System.gc();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        setResult(0, new Intent());
        finish();
    }

    public String saveBitmap(Bitmap bitmap2) {
        File file = null;
        String folder = Constant.TEMP_FOLDER_NAME;

        ContextWrapper cw = new ContextWrapper(this);
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

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        blrValue = i;
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        if (blrValue == 0) {
            blrValue = 1;
        }
        Colorized = fastblur(topImage, blrValue).copy(Config.ARGB_8888, true);
        drawMode(1, 1);
    }

    public void onBack(View view) {
        onBackPressed();
    }
}

package com.eagleapps.beautycam.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.graphics.IBitmapDrawable;

public class ImageViewSingleTap extends ImageViewTouch implements AnimatorListener {
    public static final double BRUSH_SIZE_ANIMATION_SCALE = 1.3d;
    protected float curX;
    protected float curY;
    protected float mCurrentScale;
    protected Matrix mInvertedMatrix;
    protected float mStartX;
    protected float mStartY;
    protected TouchMode mTouchMode;
    protected float f224mX;
    protected float f225mY;
    boolean IsBeforeAndAfter;
    AnimatorSet mAnimator;
    boolean mCanceled;
    boolean mDrawFadeCircle;
    Paint paint;
    Paint shadowPaint;
    private boolean IsDone;
    private ArrayList<Bitmap> blemishSpots;
    private ArrayList<Integer> brushSizes;
    private Bitmap canvasBitmap;
    private float mBrushSize;
    private Paint mShapePaint;
    private OnTapListener mTapListener;
    private ArrayList<Path> paths;
    private ArrayList<Point> points;
    private float radius;
    private Bitmap srcBitmap;

    public ImageViewSingleTap(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
        this.paths = new ArrayList<>();
        this.points = new ArrayList<>();
        this.blemishSpots = new ArrayList<>();
        this.brushSizes = new ArrayList<>();
        init();
    }

    public ImageViewSingleTap(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCurrentScale = 1.0f;
        this.mInvertedMatrix = new Matrix();
        this.mTouchMode = TouchMode.DRAW;
        this.f224mX = 0.0f;
        this.f225mY = 0.0f;
        this.curX = 0.0f;
        this.curY = 0.0f;
        this.mDrawFadeCircle = true;
        this.mCanceled = false;
        this.mBrushSize = 10.0f;
        this.radius = 0.0f;
        this.mShapePaint = new Paint();
        this.IsDone = false;
        this.canvasBitmap = null;
        this.srcBitmap = null;
        onCreate(context);
        this.paths = new ArrayList<>();
        this.points = new ArrayList<>();
        this.blemishSpots = new ArrayList<>();
        this.brushSizes = new ArrayList<>();
        init();
    }

    public static float[] getMatrixValues(Matrix matrix) {
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        return fArr;
    }

    public void onAnimationCancel(Animator animator) {
    }

    public void setBitmap(Bitmap bitmap) {
        this.canvasBitmap = bitmap.copy(Config.ARGB_8888, true);
        StringBuilder sb = new StringBuilder();
        sb.append(" width  : ");
        sb.append(this.canvasBitmap.getWidth());
        sb.append(" height : ");
        sb.append(this.canvasBitmap.getHeight());
        Log.d("bitmap width and height", sb.toString());
        invalidate();
    }

    private void init() {
        Paint paint2 = new Paint();
        this.paint = paint2;
        paint2.setColor(-1);
        this.paint.setStrokeWidth(10.0f);
        this.paint.setMaskFilter(new BlurMaskFilter(10.0f, Blur.NORMAL));
        this.paint.setAlpha(80);
        Paint paint3 = new Paint();
        this.shadowPaint = paint3;
        paint3.setColor(-1);
        this.shadowPaint.setMaskFilter(new BlurMaskFilter(20.0f, Blur.NORMAL));
        this.shadowPaint.setAlpha(50);
    }

    private void onCreate(Context context) {
        AnimatorSet animatorSet = new AnimatorSet();
        this.mAnimator = animatorSet;
        animatorSet.addListener(this);
        this.mShapePaint.setAntiAlias(true);
        this.mShapePaint.setStyle(Style.STROKE);
        this.mShapePaint.setColor(-1);
        this.mShapePaint.setStrokeWidth(6.0f);
        setLongClickable(false);
    }

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float f) {
        this.radius = f;
        invalidate();
    }

    public void onAnimationStart(Animator animator) {
        invalidate();
    }

    public void onAnimationEnd(Animator animator) {
        invalidate();
    }

    public void onAnimationRepeat(Animator animator) {
        invalidate();
    }

    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.concat(getImageMatrix());
        if (this.IsDone) {
            this.paint.setMaskFilter(new BlurMaskFilter(15.0f, Blur.NORMAL));
            this.paint.setAlpha(180);
            this.shadowPaint.setAlpha(140);
            this.shadowPaint.setMaskFilter(new BlurMaskFilter(30.0f, Blur.NORMAL));
        }
        boolean z = this.IsBeforeAndAfter;
        if (z) {
            if (z) {
                Bitmap bitmap = this.srcBitmap;
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, new Matrix(), null);
                }
            }
        } else if (this.paths.size() != 0) {
            for (int i = 0; i < this.paths.size(); i++) {
                Bitmap croppedBitmap = getCroppedBitmap(Bitmap.createScaledBitmap((Bitmap) this.blemishSpots.get(i), ((Bitmap) this.blemishSpots.get(i)).getWidth() + (((Integer) this.brushSizes.get(i)).intValue() / 2), ((Bitmap) this.blemishSpots.get(i)).getHeight() + (((Integer) this.brushSizes.get(i)).intValue() / 2), false));
                canvas.drawBitmap(croppedBitmap, (float) (((Point) this.points.get(i)).x - (croppedBitmap.getWidth() / 2)), (float) (((Point) this.points.get(i)).y - (croppedBitmap.getHeight() / 2)), this.shadowPaint);
                canvas.drawBitmap((Bitmap) this.blemishSpots.get(i), (float) (((Point) this.points.get(i)).x - (((Bitmap) this.blemishSpots.get(i)).getWidth() / 2)), (float) (((Point) this.points.get(i)).y - (((Bitmap) this.blemishSpots.get(i)).getHeight() / 2)), this.paint);
                this.curX = (float) ((Point) this.points.get(i)).x;
                this.curY = (float) ((Point) this.points.get(i)).y;
            }
        }
        if (this.mDrawFadeCircle && !this.IsDone && this.radius > 0.0f) {
            this.mShapePaint.setStrokeWidth(6.0f / this.mCurrentScale);
            canvas.drawCircle(this.curX, this.curY, this.radius, this.mShapePaint);
        }
    }

    private Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint2 = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint2.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint2.setColor(-1);
        canvas.drawCircle(((float) (bitmap.getWidth() / 2)) + 0.7f, ((float) (bitmap.getHeight() / 2)) + 0.7f, ((float) (bitmap.getWidth() / 2)) + 0.1f, paint2);
        paint2.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        paint2.setAntiAlias(true);
        paint2.setStyle(Style.STROKE);
        paint2.setStrokeJoin(Join.ROUND);
        paint2.setStrokeCap(Cap.ROUND);
        paint2.setStrokeWidth(10.0f);
        paint2.setFilterBitmap(false);
        canvas.drawBitmap(bitmap, rect, rect, paint2);
        return createBitmap;
    }

    public void setOnTapListener(OnTapListener onTapListener) {
        this.mTapListener = onTapListener;
    }

    public void init(Context context, AttributeSet attributeSet, int i) {
        super.init(context, attributeSet, i);
    }

    public OnScaleGestureListener getScaleListener() {
        return new TapScaleListener();
    }

    public void onLayoutChanged(int i, int i2, int i3, int i4) {
        super.onLayoutChanged(i, i2, i3, i4);
        if (getDrawable() != null) {
            onDrawModeChanged();
        }
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        if (this.mTouchMode != TouchMode.DRAW) {
            return super.onSingleTapConfirmed(motionEvent);
        }
        this.mDrawFadeCircle = true;
        startAnimation();
        if (this.mTapListener != null) {
            float[] fArr = {motionEvent.getX(), motionEvent.getY()};
            this.curX = fArr[0];
            this.curY = fArr[1];
            this.mInvertedMatrix.mapPoints(fArr);
            this.mTapListener.onTap(fArr, this.mBrushSize / this.mCurrentScale);
        }
        return true;
    }

    public void isBeforeAndAfter(boolean z, Bitmap bitmap) {
        this.IsBeforeAndAfter = z;
        this.srcBitmap = bitmap;
        invalidate();
    }

    private void startAnimation() {
        String str = "radius";
        try {
            this.radius = 0.0f;
            this.mShapePaint.setAlpha(255);
            ObjectAnimator.ofFloat((Object) this, str, 0.0f, this.mBrushSize).setDuration(200);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setInterpolator(new DecelerateInterpolator(1.0f));
            animatorSet.setDuration(200);
            Animator[] animatorArr = new Animator[2];
            float[] fArr = new float[2];
            fArr[0] = this.mBrushSize;
            double d = (double) this.mBrushSize;
            Double.isNaN(d);
            fArr[1] = (float) ((int) (d * 1.3d));
            animatorArr[0] = ObjectAnimator.ofFloat((Object) this, str, fArr);
            animatorArr[1] = ObjectAnimator.ofInt((Object) this.mShapePaint, "alpha", 255, 0);
            animatorSet.playTogether(animatorArr);
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.mAnimator = animatorSet2;
            animatorSet2.addListener(this);
            this.mAnimator.playSequentially(null, animatorSet);
            this.mAnimator.setInterpolator(new AccelerateInterpolator(1.0f));
            this.mAnimator.start();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (this.mTouchMode != TouchMode.DRAW) {
            return super.onScroll(motionEvent, motionEvent2, f, f2);
        }
        this.f224mX = motionEvent2.getX();
        this.f225mY = motionEvent2.getY();
        this.mCanceled = true;
        postInvalidate();
        return false;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (this.mTouchMode == TouchMode.DRAW) {
            return false;
        }
        return super.onFling(motionEvent, motionEvent2, f, f2);
    }

    public boolean onDown(MotionEvent motionEvent) {
        if (this.mTouchMode == TouchMode.DRAW) {
            float x = motionEvent.getX();
            this.f224mX = x;
            this.mStartX = x;
            float y = motionEvent.getY();
            this.f225mY = y;
            this.mStartY = y;
            this.mDrawFadeCircle = false;
        }
        return super.onDown(motionEvent);
    }

    public boolean onUp(MotionEvent motionEvent) {
        this.mCanceled = false;
        postInvalidate();
        return super.onUp(motionEvent);
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        if (this.mTouchMode == TouchMode.DRAW) {
            return false;
        }
        return super.onSingleTapUp(motionEvent);
    }

    public TouchMode getDrawMode() {
        return this.mTouchMode;
    }

    public void setDrawMode(TouchMode touchMode) {
        if (touchMode != this.mTouchMode) {
            this.mTouchMode = touchMode;
            onDrawModeChanged();
        }
    }

    public void onDrawModeChanged() {
        boolean z = false;
        if (this.mTouchMode == TouchMode.DRAW) {
            Matrix matrix = new Matrix(getImageMatrix());
            this.mInvertedMatrix.reset();
            float[] matrixValues = getMatrixValues(matrix);
            matrix.invert(matrix);
            float[] matrixValues2 = getMatrixValues(matrix);
            this.mInvertedMatrix.postTranslate(-matrixValues[2], -matrixValues[5]);
            this.mInvertedMatrix.postScale(matrixValues2[0], matrixValues2[4]);
            this.mCurrentScale = getScale() * getBaseScale();
        }
        setDoubleTapEnabled(this.mTouchMode == TouchMode.IMAGE);
        if (this.mTouchMode == TouchMode.IMAGE) {
            z = true;
        }
        setScaleEnabled(z);
    }

    public void onDrawableChanged(Drawable drawable) {
        super.onDrawableChanged(drawable);
        if (drawable != null && (drawable instanceof IBitmapDrawable)) {
            onDrawModeChanged();
        }
    }

    public RectF getImageRect() {
        if (getDrawable() != null) {
            return new RectF(0.0f, 0.0f, (float) getDrawable().getIntrinsicWidth(), (float) getDrawable().getIntrinsicHeight());
        }
        return null;
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    public void setBrushSize(float f) {
        this.mBrushSize = f;
    }

    public void removeBlemish(boolean z, ArrayList<Path> arrayList, ArrayList<Point> arrayList2, ArrayList<Bitmap> arrayList3, ArrayList<Integer> arrayList4) {
        this.IsDone = z;
        this.paths = arrayList;
        this.points = arrayList2;
        this.blemishSpots = arrayList3;
        this.brushSizes = arrayList4;
        StringBuilder sb = new StringBuilder();
        sb.append(" Paths : ");
        sb.append(this.paths.size());
        sb.append(" Points : ");
        sb.append(this.points.size());
        sb.append(" bitmaps : ");
        sb.append(arrayList3.size());
        Log.d("list size ", sb.toString());
        invalidate();
    }


    public enum TouchMode {
        IMAGE,
        DRAW
    }

    public interface OnTapListener {
        void onTap(float[] fArr, float f);
    }

    class TapScaleListener extends ScaleListener {
        TapScaleListener() {
            super();
        }

        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            if (ImageViewSingleTap.this.mTouchMode != TouchMode.DRAW) {
                return super.onScaleBegin(scaleGestureDetector);
            }
            ImageViewSingleTap.this.f224mX = scaleGestureDetector.getFocusX();
            ImageViewSingleTap.this.f225mY = scaleGestureDetector.getFocusY();
            ImageViewSingleTap imageViewSpotSingleTap = ImageViewSingleTap.this;
            imageViewSpotSingleTap.mStartX = imageViewSpotSingleTap.f224mX;
            ImageViewSingleTap imageViewSpotSingleTap2 = ImageViewSingleTap.this;
            imageViewSpotSingleTap2.mStartY = imageViewSpotSingleTap2.f225mY;
            ImageViewSingleTap.this.mCanceled = true;
            ImageViewSingleTap.this.postInvalidate();
            return true;
        }

        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            ImageViewSingleTap.this.mCanceled = false;
            super.onScaleEnd(scaleGestureDetector);
        }

        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            if (ImageViewSingleTap.this.mTouchMode != TouchMode.DRAW) {
                return super.onScale(scaleGestureDetector);
            }
            ImageViewSingleTap.this.f224mX = scaleGestureDetector.getFocusX();
            ImageViewSingleTap.this.f225mY = scaleGestureDetector.getFocusY();
            ImageViewSingleTap.this.postInvalidate();
            return true;
        }
    }
}

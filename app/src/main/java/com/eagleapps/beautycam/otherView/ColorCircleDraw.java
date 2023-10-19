package com.eagleapps.beautycam.otherView;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class ColorCircleDraw extends Drawable {
    private final Paint mPaint;
    private int mRadius = 0;

    public ColorCircleDraw(int i) {
        Paint paint = new Paint(1);
        this.mPaint = paint;
        paint.setColor(i);
    }

    public int getOpacity() {
        return -3;
    }

    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.drawCircle((float) bounds.centerX(), (float) bounds.centerY(), (float) this.mRadius, this.mPaint);
    }

    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mRadius = Math.min(rect.width(), rect.height()) / 2;
    }

    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }
}

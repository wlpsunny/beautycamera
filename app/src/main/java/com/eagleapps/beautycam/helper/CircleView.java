package com.eagleapps.beautycam.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CircleView extends View {
    public Paint paint = new Paint();

    public CircleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }


    public void onDraw(Canvas canvas) {
        canvas.drawCircle((float) (getWidth() / 2), (float) (getWidth() / 2), ((float) getWidth()) * 0.3f, this.paint);
    }
}

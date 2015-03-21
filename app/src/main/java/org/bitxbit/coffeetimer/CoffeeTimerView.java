package org.bitxbit.coffeetimer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CoffeeTimerView extends View {
    private int color;

    public CoffeeTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoffeeTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureUtils.getMeasurement(widthMeasureSpec, 320);
        int h = MeasureUtils.getMeasurement(heightMeasureSpec, 320);
        setMeasuredDimension(w, h);
    }

    void setColor(int color) {
        this.color = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10.0f);
        paint.setARGB(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));

        //Need to offset left/top by at least stroke width to ensure border is not shaved off
        canvas.drawArc(new RectF(10, 10, 310f, 310f), 0, 360, false, paint);
    }
}

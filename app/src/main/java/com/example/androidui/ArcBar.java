package com.example.androidui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ArcBar extends ProgressBar {
    private RectF mArcRect = new RectF();
    private Paint mArcPaint;
    private Paint mProgressPaint;

    public ArcBar(Context context) {
        this(context, null);
    }

    public ArcBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int progressColor = getResources().getColor(R.color.dark_blue);
        int ovalColor = getResources().getColor(R.color.ivory);

        // Init attributes for ArcBar
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ArcBar, 0, 0);
            ovalColor = a.getColor(R.styleable.ArcBar_arcColor, ovalColor);
            progressColor = a.getColor(R.styleable.ArcBar_progressColor, progressColor);
            a.recycle();
        }


        mArcPaint = new Paint();
        mArcPaint.setColor(ovalColor);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mProgressPaint = new Paint();
        mProgressPaint.setColor(progressColor);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius = Math.max(getWidth(), getHeight())/2;

        int x = getWidth() / 2;
        int y =  getHeight() / 2;

        float y1 = y + radius - (2 * radius *  getProgress()/ 100f -1);
        float x1 = x - (float) Math.sqrt(Math.pow(radius, 2) - Math.pow(y1 - y, 2));

        float angle = (float) Math.toDegrees(Math.atan((y - y1) / (x1 - x)));
        float startAngle = 180 - angle;

        float sweepAngle = 2 * angle - 180;

        canvas.drawArc(mArcRect, sweepAngle, 360-sweepAngle, false, mArcPaint);
        canvas.drawArc(mArcRect,startAngle, sweepAngle, false, mProgressPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int x = getWidth() / 2;
        int y = getHeight() / 2;
        int radius = Math.min(getWidth(), getHeight()) / 2;
        mArcRect.set(x-radius,y-radius,x+radius,y+radius);
    }
}
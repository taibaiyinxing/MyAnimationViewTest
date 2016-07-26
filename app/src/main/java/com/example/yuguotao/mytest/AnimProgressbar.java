package com.example.yuguotao.mytest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by yuguotao on 16/7/26.
 */
public class AnimProgressbar extends View {
    Paint bgPaint;
    Paint progressPaint;
    int bgColor = 0x77000000;
    int progressColor = Color.BLACK;
    Canvas mCanvas;

    float height;
    float width;

    float value;
    long duration = 1000;


    public AnimProgressbar(Context context) {
        super(context);
        init();
    }

    public AnimProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bgPaint = new Paint();
        bgPaint.setColor(bgColor);
        progressPaint = new Paint();
        progressPaint.setColor(progressColor);
    }

    private void initData() {
        height = getMeasuredHeight();
        width = getMeasuredWidth();
    }

    public void setColor(int color) {
        this.progressColor = color;
        progressPaint.setColor(progressColor);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initData();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        canvas.drawRoundRect(new RectF(0, 0, width, height), height / 2, height / 2, bgPaint);
        canvas.drawRoundRect(new RectF(0, 0, value, height), height / 2, height / 2, progressPaint);
    }

    public void moveToProgress(final float toValue) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ValueAnimator animator = ValueAnimator.ofFloat(value, toValue / 100f * (width));
                animator.setDuration(duration);
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        value = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                animator.start();
            }
        }, 400);
    }
}

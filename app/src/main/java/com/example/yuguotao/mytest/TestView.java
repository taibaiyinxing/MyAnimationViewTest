package com.example.yuguotao.mytest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by yuguotao on 16/7/22.
 */
public class TestView extends View {
    float height;
    float width;
    float R;
    float degree;
    float operatorDegree;
    int NumTime = 50;
    PointF leftPoint;
    PointF rightPoint;

    Paint p;

    public TestView(Context context) {
        super(context);
        init();
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.RED);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        initDate();

//        canvas.drawArc(new RectF(leftPoint.x-R,leftPoint.y-2*R,leftPoint.x+R,leftPoint.y),270,degree,false,p);
        final Path path = new Path();
        path.moveTo(leftPoint.x,leftPoint.y);
        path.arcTo(new RectF(leftPoint.x-R,leftPoint.y-2*R,leftPoint.x+R,leftPoint.y),90,operatorDegree,false);
        canvas.drawPath(path,p);

        canvas.drawCircle(leftPoint.x,leftPoint.y,20,p);
        canvas.drawCircle(rightPoint.x,rightPoint.y,20,p);
//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },20);

    }

    public void startAnim(){
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ValueAnimator animator = ValueAnimator.ofFloat(0,-degree);
                animator.setDuration(1000);
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        operatorDegree = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                animator.start();
            }
        },100);
    }

    private void initDate(){
        height = getMeasuredHeight();
        width = getMeasuredWidth();
        leftPoint = getLeftPoint();
        rightPoint = getRightPoint();
        R = getR();
        degree = getDegree();
    }

    private void drawArc(PointF left,PointF right,Path path){

    }

    private PointF getLeftPoint(){
        return new PointF(20*(getResources().getDisplayMetrics().density),height-20*(getResources().getDisplayMetrics().density));
    }

    private PointF getRightPoint(){
        return new PointF(width - 20*(getResources().getDisplayMetrics().density),40*(getResources().getDisplayMetrics().density));
    }

    private float getR(){
        return ((rightPoint.x - leftPoint.x)*(rightPoint.x - leftPoint.x) + (leftPoint.y - rightPoint.y)* (leftPoint.y - rightPoint.y))/ 2 / (leftPoint.y - rightPoint.y);
    }

    private float getDegree(){
        return (float) Math.toDegrees(Math.asin((rightPoint.x - leftPoint.x)/R));
    }
}

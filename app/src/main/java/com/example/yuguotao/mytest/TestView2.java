package com.example.yuguotao.mytest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

/**
 * Created by yuguotao on 16/7/22.
 */
public class TestView2 extends View {
    float height;
    float width;
    float R;
    float degree;
    float operatorDegree;
    float lastOperatorDegree;
    long duration = 1000;
    PointF leftPoint;
    PointF rightPoint;
    ArrayList<PointF> operationPoints = new ArrayList<>();
    ArrayList<PointF> keyPoints;

    Paint p;
    Paint gradientPaint;
    Paint keyPointPaint;
    LinearGradient shader;
    Path path;

    Canvas mCanvas;

    public TestView2(Context context) {
        super(context);
        init();
    }

    public TestView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.RED);
        p.setStrokeWidth(5 * getResources().getDisplayMetrics().density);
        gradientPaint = new Paint();
        keyPointPaint = new Paint();
        keyPointPaint.setColor(Color.YELLOW);
        keyPointPaint.setStyle(Paint.Style.FILL);
        path = new Path();
    }

    public void initKeyPoints(int Number) {

        if (keyPoints == null) {
            keyPoints = new ArrayList<>();
        } else {
            keyPoints.clear();
        }
        if (Number < 2) {
            return;
        }
        keyPoints.add(leftPoint);
        float itemLength = (rightPoint.x - leftPoint.x) / (Number - 1);
        for (int i = 1; i < Number; i++) {
            keyPoints.add(getPointWithLenth(itemLength * i));
        }
    }

    private PointF getPointWithLenth(float length) {
        float x = leftPoint.x + length;
        float y = (float) (leftPoint.y - (R - Math.sqrt(R * R - length * length)));
        return new PointF(x, y);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initDate();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
//        initDate();
        mCanvas = canvas;
        //画圆弧下面的阴影
        operationPoints.add(getOperatorPoint());
        for (int i = 1; i < operationPoints.size(); i++) {
            PointF operationPoint = operationPoints.get(i);
            PointF lastOperationPoint = operationPoints.get(i - 1);
            shader = new LinearGradient(operationPoint.x, operationPoint.y, operationPoint.x, leftPoint.y + 10 * getResources().getDisplayMetrics().density, Color.BLUE, Color.WHITE, Shader.TileMode.MIRROR);
            gradientPaint.setShader(shader);
            canvas.drawRect(lastOperationPoint.x, lastOperationPoint.y, operationPoint.x, leftPoint.y + 10 * getResources().getDisplayMetrics().density, gradientPaint);
        }

        //画圆弧

        path.moveTo(leftPoint.x, leftPoint.y);
        path.arcTo(new RectF(leftPoint.x - R, leftPoint.y - 2 * R, leftPoint.x + R, leftPoint.y), 90, operatorDegree, false);
        canvas.drawPath(path, p);

        //画关键点
        if (keyPoints != null && keyPoints.size() > 0) {
            for (PointF point : keyPoints) {
                canvas.drawCircle(point.x, point.y, 5 * getResources().getDisplayMetrics().density, keyPointPaint);
            }
        }
    }

    public void startInitAnim() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                operatorDegree = 0;
                lastOperatorDegree = 0;
                operationPoints.clear();
                ValueAnimator animator = ValueAnimator.ofFloat(0, -degree);
                animator.setDuration(duration);
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        lastOperatorDegree = operatorDegree;
                        operatorDegree = (float) animation.getAnimatedValue();
//                        invalidate();
                        drawView();
                    }
                });
                animator.start();
            }
        }, 100);
    }

    private void drawView(){
        path.arcTo(new RectF(leftPoint.x - R, leftPoint.y - 2 * R, leftPoint.x + R, leftPoint.y), lastOperatorDegree, operatorDegree-lastOperatorDegree, false);
        Log.e("mCanvas",mCanvas.toString());
        mCanvas.drawPath(path, p);
    }

    private void initDate() {
        height = getMeasuredHeight();
        width = getMeasuredWidth();
        leftPoint = getLeftPoint();
        rightPoint = getRightPoint();
        R = getR();
        degree = getDegree();
        initKeyPoints(4);
    }

    private PointF getOperatorPoint() {
        return new PointF((float) (leftPoint.x + R * Math.sin(Math.toRadians(Math.abs(operatorDegree)))), (float) (leftPoint.y - (R - R * Math.cos(Math.toRadians(Math.abs(operatorDegree))))));
    }

    private PointF getLeftPoint() {
        return new PointF(20 * (getResources().getDisplayMetrics().density), height - 20 * (getResources().getDisplayMetrics().density));
    }

    private PointF getRightPoint() {
        return new PointF(width - 20 * (getResources().getDisplayMetrics().density), 40 * (getResources().getDisplayMetrics().density));
    }

    private float getR() {
        return ((rightPoint.x - leftPoint.x) * (rightPoint.x - leftPoint.x) + (leftPoint.y - rightPoint.y) * (leftPoint.y - rightPoint.y)) / 2 / (leftPoint.y - rightPoint.y);
    }

    private float getDegree() {
        return (float) Math.toDegrees(Math.asin((rightPoint.x - leftPoint.x) / R));
    }
}


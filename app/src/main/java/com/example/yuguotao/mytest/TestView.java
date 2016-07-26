package com.example.yuguotao.mytest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
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
public class TestView extends View {
    private float height;
    private float width;
    private float R;
    private float degree;
    private float operatorDegree;
    private long duration = 1000;
    private float density;
    private PointF leftPoint;
    private PointF rightPoint;
    private ArrayList<PointF> operationPoints = new ArrayList<>();
    private ArrayList<PointF> keyPoints;
    private String[] keyPointTitles;

    private float fullScore = 5;
    private float mScore;
    private PointF mScorePoint;

    private Bitmap headerMap;
    private Bitmap helperHeaderMap;
    private Bitmap headerBG;

    private Paint p;
    private Paint gradientPaint;
    private Paint keyPointPaint;
    private Paint keyPointTitlePaint;
    private LinearGradient shader;

    private Canvas mCanvas;
    private int mType;

    private boolean isInitAnimationRunning;
    private boolean hasScore = false;

    public static int MONEY = 0;
    public static int LOVE = 1;
    public static int HEALTH = 2;

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

    public void setType(int type) {
        this.mType = type;
    }

    public void setScoreAndHeader(final float score, Bitmap headerMap) {
//        this.mScore = score;
        this.headerMap = headerMap;
        hasScore = true;

        postDelayed(new Runnable() {
            @Override
            public void run() {
                ValueAnimator animator = ValueAnimator.ofFloat(0,score);
                animator.setDuration(duration);
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mScore = (float) animation.getAnimatedValue();
                        mScorePoint = getPointWithLenth(mScore / fullScore * (rightPoint.x - leftPoint.x));
                        invalidate();
                    }
                });
                animator.start();
            }
        },400);
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;
        p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.WHITE);
        p.setStrokeWidth(3 * density);
        gradientPaint = new Paint();
        keyPointPaint = new Paint();
        keyPointPaint.setColor(Color.WHITE);
        keyPointPaint.setStyle(Paint.Style.FILL);
        keyPointTitlePaint = new Paint();
        keyPointTitlePaint.setTextSize(12 * density);
        keyPointTitlePaint.setColor(0xA3FFFFFF);
        p.setAntiAlias(true);
        gradientPaint.setAntiAlias(true);
    }

    private void initKeyPoints() {

        if (keyPoints == null) {
            keyPoints = new ArrayList<>();
        } else {
            keyPoints.clear();
        }
        if (keyPointTitles == null)
            return;
        int Number = keyPointTitles.length;
        if (Number < 2) {
            return;
        }
        keyPoints.add(leftPoint);
        float itemLength = (rightPoint.x - leftPoint.x) / (Number - 1);
        for (int i = 1; i < Number; i++) {
            keyPoints.add(getPointWithLenth(itemLength * i));
        }
    }

    public void setKeyPointTitles(String[] values) {
        this.keyPointTitles = values;
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
            shader = new LinearGradient(operationPoint.x, operationPoint.y, operationPoint.x, leftPoint.y + 10 * density, 0x77FFFFFF, 0x00FFFFFF, Shader.TileMode.MIRROR);
            gradientPaint.setShader(shader);
            canvas.drawRect(lastOperationPoint.x, lastOperationPoint.y, operationPoint.x, leftPoint.y + 10 * density, gradientPaint);
        }

        //画圆弧
        final Path path = new Path();
        path.moveTo(leftPoint.x, leftPoint.y);
        path.arcTo(new RectF(leftPoint.x - R, leftPoint.y - 2 * R, leftPoint.x + R, leftPoint.y), 90, operatorDegree, false);
        canvas.drawPath(path, p);

        //画关键点
        if (keyPoints != null && keyPoints.size() > 0) {
            for (int i = 0; i < keyPoints.size(); i++) {
                PointF point = keyPoints.get(i);
                canvas.drawCircle(point.x, point.y, 5 * density, keyPointPaint);
                String title = keyPointTitles[i];
                int l = title.length();
                Rect rect = new Rect();
                keyPointTitlePaint.getTextBounds(title, 0, l, rect);
                canvas.drawText(title, point.x - rect.width() / 2, point.y + 8 * density + rect.height(), keyPointTitlePaint);
            }
        }

        //画头像与分数
        if (hasScore){
            canvas.drawBitmap(headerBG,mScorePoint.x-25,mScorePoint.y-100,p);
            //TODO:画头像
        }
    }

    public void startInitAnim() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                operatorDegree = 0;
                isInitAnimationRunning = false;
                operationPoints.clear();
                ValueAnimator animator = ValueAnimator.ofFloat(0, -degree);
                animator.setDuration(duration);
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        operatorDegree = (float) animation.getAnimatedValue();
                        isInitAnimationRunning = operatorDegree != -degree;
                        invalidate();
                    }
                });
                animator.start();
            }
        }, 100);
    }

    private void initDate() {
        height = getMeasuredHeight();
        width = getMeasuredWidth();
        leftPoint = getLeftPoint();
        rightPoint = getRightPoint();
        R = getR();
        degree = getDegree();
        initKeyPoints();
        headerBG = BitmapFactory.decodeResource(getResources(), com.example.yuguotao.mytest.R.mipmap.result_head_frame);

    }

    private PointF getOperatorPoint() {
        return new PointF((float) (leftPoint.x + R * Math.sin(Math.toRadians(Math.abs(operatorDegree)))), (float) (leftPoint.y - (R - R * Math.cos(Math.toRadians(Math.abs(operatorDegree))))));
    }

    private PointF getLeftPoint() {
        return new PointF(20 * (density), height - 20 * (density));
    }

    private PointF getRightPoint() {
        return new PointF(width - 20 * (density), 40 * (density));
    }

    private float getR() {
        return ((rightPoint.x - leftPoint.x) * (rightPoint.x - leftPoint.x) + (leftPoint.y - rightPoint.y) * (leftPoint.y - rightPoint.y)) / 2 / (leftPoint.y - rightPoint.y);
    }

    private float getDegree() {
        return (float) Math.toDegrees(Math.asin((rightPoint.x - leftPoint.x) / R));
    }
}

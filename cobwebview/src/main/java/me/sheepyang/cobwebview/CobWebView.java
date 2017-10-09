package me.sheepyang.cobwebview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by SheepYang on 2017-10-09.
 */

public class CobWebView extends View {
    private static final int DEFAULT_POINT_NUMBER = 180;//小球数量
    private static final int ACCELERATION = 5;//小球运动的加速度
    private static final double MAX_DISTANCE = 500;
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private List<CobPoint> mPointList;
    private Config mConfig;
    private Random mRandom;
    private Context mContext;

    public CobWebView(Context context) {
        super(context);
        init(context, null);
    }

    public CobWebView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CobWebView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mConfig = new Config();
        mPointList = new ArrayList<CobPoint>();
        mRandom = new Random();
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CobWebView);
            mConfig.setPointNum(typedArray.getInt(R.styleable.CobWebView_cwb_point_num, DEFAULT_POINT_NUMBER));
            typedArray.recycle();
        }
        mPaint = new Paint();
        mPaint.setStrokeWidth(10);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(0xFF686868);
    }

    private void initPoint() {
        for (int i = 0; i < mConfig.getPointNum(); i++) {
            int width = (int) (mRandom.nextDouble() * mWidth);
            int height = (int) (mRandom.nextDouble() * mHeight);
            CobPoint point = new CobPoint(width, height);
            int xa = 0;
            int ya = 0;

            while (xa == 0) {
                xa = (int) ((mRandom.nextDouble() - 0.5) * ACCELERATION);
            }
            while (ya == 0) {
                ya = (int) ((mRandom.nextDouble() - 0.5) * ACCELERATION);
            }

            point.setXa(xa);
            point.setYa(ya);
            mPointList.add(point);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        initPoint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (CobPoint currentPoint :
                mPointList) {
            currentPoint.x += currentPoint.getXa();
            currentPoint.y += currentPoint.getYa();
            if (currentPoint.x <= 0 || currentPoint.x >= mWidth) {
                currentPoint.setXa(-currentPoint.getXa());
            }
            if (currentPoint.y <= 0 || currentPoint.y >= mHeight) {
                currentPoint.setYa(-currentPoint.getYa());
            }
            canvas.drawPoint(currentPoint.x, currentPoint.y, mPaint);
            for (int i = 0; i < mPointList.size(); i++) {
                CobPoint point = mPointList.get(i);
                if (currentPoint != point && Math.abs(Math.sqrt(currentPoint.x * currentPoint.x + currentPoint.y * currentPoint.y)) < MAX_DISTANCE) {
                }
            }
            postInvalidateDelayed(50);
        }
    }

    private class Config {
        private int mPointNum = DEFAULT_POINT_NUMBER;//小黑点个数

        public int getPointNum() {
            return mPointNum;
        }

        public void setPointNum(int pointNum) {
            mPointNum = pointNum;
        }
    }

    class CobPoint extends Point {
        private int xa;//水平方向的加速度
        private int ya;//垂直方向的加速度

        public CobPoint(int x, int y) {
            super(x, y);
        }

        public int getXa() {
            return xa;
        }

        public void setXa(int xa) {
            this.xa = xa;
        }

        public int getYa() {
            return ya;
        }

        public void setYa(int ya) {
            this.ya = ya;
        }
    }
}

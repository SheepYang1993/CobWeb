package me.sheepyang.cobwebview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by SheepYang on 2017-10-09.
 */

public class CobWebView extends View implements GestureDetector.OnGestureListener {
    private static final int DEFAULT_POINT_NUMBER = 250;//小球数量
    private static final int DEFAULT_ACCELERATION = 5;
    private static final int DEFAULT_MAX_DISTANCE = 250;
    private static final int DEFAULT_ALPHA = 150;
    private static final int DEFAULT_LINE_WIDTH = 15;
    private static final int DEFAULT_POINT_RADIUS = 5;
    private static final int DEFAULT_TOUCH_POINT_RADIUS = 5;
    private static final int DEFAULT_RANGE = 50;


    private int mWidth;
    private int mHeight;
    private Paint mPointPaint;
    private Paint mTouchPaint;
    private Paint mLinePaint;
    private List<CobPoint> mPointList;
    private Config mConfig;
    private Random mRandom;
    private Context mContext;
    private GestureDetector mGestureDetector;
    private float mTouchX = -1;
    private float mTouchY = -1;

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
        mGestureDetector = new GestureDetector(context, this);
        mPointList = new ArrayList<CobPoint>();
        mRandom = new Random();
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CobWebView);
            mConfig.setPointNum(typedArray.getInt(R.styleable.CobWebView_cwb_point_num, DEFAULT_POINT_NUMBER));
            typedArray.recycle();
        }
        initPaint();
    }

    private void initPaint() {
        mLinePaint = new Paint();
        mLinePaint.setStrokeWidth(mConfig.getLineWidth());
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setColor(0xEBFF94B9);

        mTouchPaint = new Paint();
        mTouchPaint.setStrokeWidth(mConfig.getTouchPointRadius());
        mTouchPaint.setStrokeCap(Paint.Cap.ROUND);
        mTouchPaint.setColor(0xD8FF7875);

        mPointPaint = new Paint();
        mPointPaint.setStrokeWidth(mConfig.getPointRadius());
        mPointPaint.setStrokeCap(Paint.Cap.ROUND);
        mPointPaint.setColor(0xEBFF4081);
    }

    private void initPoint() {
        for (int i = 0; i < mConfig.getPointNum(); i++) {
            int width = (int) (mRandom.nextDouble() * mWidth);
            int height = (int) (mRandom.nextDouble() * mHeight);
            CobPoint point = new CobPoint(width, height);
            int xa = 0;
            int ya = 0;

            while (xa == 0) {
                xa = (int) ((mRandom.nextDouble() - 0.5) * mConfig.getAcceleration());
            }
            while (ya == 0) {
                ya = (int) ((mRandom.nextDouble() - 0.5) * mConfig.getAcceleration());
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
        restart();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTouchX != -1 && mTouchY != -1) {
            canvas.drawPoint(mTouchX, mTouchY, mTouchPaint);
        }

        if (mPointList != null && mPointList.size() > 0) {
            for (CobPoint currentPoint :
                    mPointList) {
                currentPoint.x += currentPoint.getXa();
                currentPoint.y += currentPoint.getYa();

                if (mTouchX != -1 && mTouchY != -1) {
                    double x = Math.abs(currentPoint.x - mTouchX);
                    double y = Math.abs(currentPoint.y - mTouchY);
                    double distance = Math.sqrt(x * x + y * y);
                    if (distance < mConfig.getMaxDistance()) {
//                      dist < e.max
//                      && (e === current_point && dist >= e.max / 2
//                      && (r.x -= 0.03 * x_dist, r.y -= 0.03 * y_dist), //靠近的时候加速
                        if (distance >= mConfig.getMaxDistance() - mConfig.getRange()) {
                            if (currentPoint.x > mTouchX) {
                                currentPoint.x -= 0.03 * x;
                            } else {
                                currentPoint.x += 0.03 * x;
                            }
                            if (currentPoint.y > mTouchY) {
                                currentPoint.y -= 0.03 * y;
                            } else {
                                currentPoint.y += 0.03 * y;
                            }
                        }

                        int alpha = (int) ((1.0 - distance / (double) mConfig.getMaxDistance()) * mConfig.getAlpha());
                        mLinePaint.setAlpha(alpha);
                        canvas.drawLine(mTouchX, mTouchY, currentPoint.x, currentPoint.y, mLinePaint);
                    }
                }

                //如果小点超出了边界则反方向反弹
                if (currentPoint.x <= 0 || currentPoint.x >= mWidth) {
                    currentPoint.setXa(-currentPoint.getXa());
                }
                if (currentPoint.y <= 0 || currentPoint.y >= mHeight) {
                    currentPoint.setYa(-currentPoint.getYa());
                }

                canvas.drawPoint(currentPoint.x, currentPoint.y, mPointPaint);
                for (int i = 0; i < mPointList.size(); i++) {
                    CobPoint point = mPointList.get(i);
                    int x = Math.abs(currentPoint.x - point.x);
                    int y = Math.abs(currentPoint.y - point.y);
                    int distance = (int) Math.sqrt(x * x + y * y);
                    if (currentPoint != point && distance > 0 && distance < mConfig.getMaxDistance()) {
                        int alpha = (int) ((1.0 - distance / (double) mConfig.getMaxDistance()) * mConfig.getAlpha());
                        mLinePaint.setAlpha(alpha);
                        canvas.drawLine(currentPoint.x, currentPoint.y, point.x, point.y, mLinePaint);
                    }
                }
            }
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //GestureDetector没有处理up事件的方法，只能在这里处理了。
        if (event.getAction() == MotionEvent.ACTION_UP) {
            clearTouchPoint();
            return true;
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        mTouchX = motionEvent.getX();
        mTouchY = motionEvent.getY();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent1, MotionEvent motionEvent2, float v, float v1) {
        mTouchX = motionEvent2.getX();
        mTouchY = motionEvent2.getY();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float v, float v1) {
        mTouchX = motionEvent2.getX();
        mTouchY = motionEvent2.getY();
        return true;
    }

    public void clearTouchPoint() {
        mTouchX = -1;
        mTouchY = -1;
    }

    public void restart() {
        clearPoint();
        initPoint();
    }

    private void clearPoint() {
        mTouchX = -1;
        mTouchY = -1;
        mPointList.clear();
    }

    public void setPointNum(int num) {
        mConfig.setPointNum(num);
        restart();
    }

    public int getPointNum() {
        return mConfig.getPointNum();
    }

    public void setAcceleration(int acceleration) {
        mConfig.setAcceleration(acceleration);
        restart();
    }

    public int getAcceleration() {
        return mConfig.getAcceleration();
    }

    public void setMaxDistance(int maxDistance) {
        mConfig.setMaxDistance(maxDistance);
    }

    public int getMaxDistance() {
        return mConfig.getMaxDistance();
    }

    public void setLineAlpha(int alpha) {
        mConfig.setAlpha(alpha);
    }

    public int getLineAlpha() {
        return mConfig.getAlpha();
    }

    public void setLineWidth(int lineWidth) {
        mConfig.setLineWidth(lineWidth);
        mLinePaint.setStrokeWidth(mConfig.getLineWidth());
    }

    public int getLineWidth() {
        return mConfig.getLineWidth();
    }

    public void setPointRadius(int radius) {
        mConfig.setPointRadius(radius);
        mPointPaint.setStrokeWidth(mConfig.getPointRadius());
    }

    public int getPointRadius() {
        return mConfig.getPointRadius();
    }

    public void setTouchPointRadius(int radius) {
        mConfig.setTouchPointRadius(radius);
        mTouchPaint.setStrokeWidth(mConfig.getTouchPointRadius());
    }

    public int getTouchPointRadius() {
        return mConfig.getTouchPointRadius();
    }

    public void setPullBackRange(int range) {
        mConfig.setRange(range);
    }

    public int getPullBackRange() {
        return mConfig.getRange();
    }

    private class Config {
        private int range = DEFAULT_RANGE;
        private int mTouchPointRadius = DEFAULT_TOUCH_POINT_RADIUS;
        private int mPointRadius = DEFAULT_POINT_RADIUS;
        private int mLineWidth = DEFAULT_LINE_WIDTH;
        private int mAlpha = DEFAULT_ALPHA;
        private int mPointNum = DEFAULT_POINT_NUMBER;//小黑点个数
        private int mAcceleration = DEFAULT_ACCELERATION;//加速度
        private int maxDistance = DEFAULT_MAX_DISTANCE;//小点之间最长直线距离

        public int getRange() {
            return range;
        }

        public void setRange(int range) {
            this.range = range;
        }

        public int getTouchPointRadius() {
            return mTouchPointRadius;
        }

        public void setTouchPointRadius(int touchPointRadius) {
            this.mTouchPointRadius = touchPointRadius;
        }

        public int getPointRadius() {
            return mPointRadius;
        }

        public void setPointRadius(int pointRadius) {
            this.mPointRadius = pointRadius;
        }

        public int getLineWidth() {
            return mLineWidth;
        }

        public void setLineWidth(int lineWidth) {
            this.mLineWidth = lineWidth;
        }

        public int getAlpha() {
            return mAlpha;
        }

        public void setAlpha(int alpha) {
            mAlpha = alpha;
        }

        public int getMaxDistance() {
            return maxDistance;
        }

        public void setMaxDistance(int maxDistance) {
            this.maxDistance = maxDistance;
        }

        public int getAcceleration() {
            return mAcceleration;
        }

        public void setAcceleration(int acceleration) {
            mAcceleration = acceleration;
        }

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

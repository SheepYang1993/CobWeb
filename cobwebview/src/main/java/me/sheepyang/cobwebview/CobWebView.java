package me.sheepyang.cobwebview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
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
    private static final int ACCELERATION = 5;//小球运动的加速度
    private static final double MAX_DISTANCE =250;//小点之间最长直线距离
    private static final double LINE_ALPHA = 150;
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
        mLinePaint.setStrokeWidth(2);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setColor(0xFF870707);

        mTouchPaint = new Paint();
        mTouchPaint.setStrokeWidth(20);
        mTouchPaint.setStrokeCap(Paint.Cap.ROUND);
        mTouchPaint.setColor(0xFF000000);

        mPointPaint = new Paint();
        mPointPaint.setStrokeWidth(10);
        mPointPaint.setStrokeCap(Paint.Cap.ROUND);
        mPointPaint.setColor(0xFF686868);
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
        if (mTouchX != -1 && mTouchY != -1) {
            canvas.drawPoint(mTouchX, mTouchY, mTouchPaint);
        }


        for (CobPoint currentPoint :
                mPointList) {
            double tempX = Math.abs(currentPoint.x - mTouchX);
            double tempY = Math.abs(currentPoint.y - mTouchY);
            double tempDistance = Math.sqrt(tempX * tempX + tempY * tempY);
            currentPoint.x += currentPoint.getXa();
            currentPoint.y += currentPoint.getYa();

            if (mTouchX != -1 && mTouchY != -1) {
                double x = Math.abs(currentPoint.x - mTouchX);
                double y = Math.abs(currentPoint.y - mTouchY);
                double distance = Math.sqrt(x * x + y * y);
                if (distance < MAX_DISTANCE) {
//                    if ((distance2 > TOUCH_DISTANCE - 15 && distance2 <= TOUCH_DISTANCE) || (distance2 <= TOUCH_DISTANCE + 15 && distance2 > TOUCH_DISTANCE)) {
//                        if (currentPoint.x > mTouchX) {
//                            currentPoint.x += SUB_DISTANCE * Math.abs(currentPoint.getXa());
//                        } else {
//                            currentPoint.x -= SUB_DISTANCE * Math.abs(currentPoint.getXa());
//                        }
//                        if (currentPoint.y > mTouchY) {
//                            currentPoint.y += SUB_DISTANCE * Math.abs(currentPoint.getYa());
//                        } else {
//                            currentPoint.y -= SUB_DISTANCE * Math.abs(currentPoint.getYa());
//                        }
//                    }

//                    if (distance >= MAX_DISTANCE / 2) {
//                        if (distance > tempDistance) {//小点远离手指触摸点
//                        currentPoint.x += 1.3 * Math.abs(currentPoint.getXa());
//                        } else if (distance < tempDistance) {//小点接近手指触摸点
//                            currentPoint.x -= 1.3 * Math.abs(currentPoint.getXa());
//                        }
//                    }

//                      dist < e.max
//                      && (e === current_point && dist >= e.max / 2
//                      && (r.x -= 0.03 * x_dist, r.y -= 0.03 * y_dist), //靠近的时候加速
                    if (distance >= MAX_DISTANCE -50) {
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

                    int alpha = (int) ((1.0 - distance / MAX_DISTANCE) * LINE_ALPHA);
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
                if (currentPoint != point && distance > 0 && distance < MAX_DISTANCE) {
                    int alpha = (int) ((1.0 - distance / MAX_DISTANCE) * LINE_ALPHA);
                    mLinePaint.setAlpha(alpha);
                    canvas.drawLine(currentPoint.x, currentPoint.y, point.x, point.y, mLinePaint);
                }
            }
            postInvalidateDelayed(50);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //GestureDetector没有处理up事件的方法，只能在这里处理了。
        if (event.getAction() == MotionEvent.ACTION_UP) {
//            CobPoint point = new CobPoint((int) mTouchX, (int) mTouchY);
//            int xa = 0;
//            int ya = 0;
//            while (xa == 0) {
//                xa = (int) ((mRandom.nextDouble() - 0.5) * ACCELERATION);
//            }
//            while (ya == 0) {
//                ya = (int) ((mRandom.nextDouble() - 0.5) * ACCELERATION);
//            }
//
//            point.setXa(xa);
//            point.setYa(ya);
//            mPointList.add(point);

            mTouchX = -1;
            mTouchY = -1;
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

package com.example.zhanghongjie.radiogroupandfragment.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanghongjie
 * @date 2018/8/30
 * @descrition 绘制圆形浮动气泡及设定渐变背景的绘制对象
 */
public class BubbleDrawer {

    private final static String TAG = "BubbleDrawer";

    //渐变背景
    private GradientDrawable mGrradientDrawable;
    //渐变颜色数组
    private int[] mGradientColors;

    //抗锯齿画笔
    private Paint mPaint;
    //屏幕宽度
    private int mWidth, mHeight;
    //存放气泡的集合
    private List<BubbleCircle> mBubbles;

    public BubbleDrawer(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBubbles = new ArrayList<>();
    }

    public void setScreenSize(int width, int height) {
        if (mWidth != width || mHeight != height) {
            mWidth = width;
            mHeight = height;
            if (mGrradientDrawable != null) {
                mGrradientDrawable.setBounds(0,0, mWidth, mHeight);
            }
        }
        initDefaultBubble(width);
    }

    //将气泡写死在该方法中
    private void initDefaultBubble(int width) {
        if (mBubbles.size() == 0) {
            mBubbles.add(new BubbleCircle(0.20f * width, -0.30f * width, 0.10f * width, 0.042f * width, 0.56f * width,
                    0x80ffc7c7,0.0150f));
            mBubbles.add(new BubbleCircle(0.58f * width, -0.15f * width, -0.25f * width, 0.052f * width, 0.6f * width,
                    0x85fffc9e,0.00600f));
            mBubbles.add(new BubbleCircle(0.9f * width, -0.19f * width, 0.08f * width, -0.015f * width, 0.44f * width,
                     0x7596ff8f,0.00300f));
            mBubbles.add(new BubbleCircle(1.1f * width, 0.25f * width, -0.08f * width, -0.065f * width, 0.42f * width,
                    0x80c7dcff, 0.00200f));
            mBubbles.add(new BubbleCircle(0.20f * width, 0.50f * width, -0.02f * width, 0.042f * width, 0.42f * width,
                    0x70efc2ff, 0.0150f));
            mBubbles.add(new BubbleCircle(0.70f * width, 0.60f * width, 0.10f * width, 0.100f * width, 0.30f * width,
                    0x75E99161,0.0100f));
        }
    }

    //用画笔画气泡
    private void drawBubbleCircle(Canvas canvas, float alpha) {
        for (BubbleCircle circle : mBubbles) {
             circle.upDateAndDraw(canvas, mPaint, alpha);
        }
    }

    //设置渐变背景色（数量要大于2，不然无法渐变）
    public void setmGradientColors(int[] mGradientColors) {
        this.mGradientColors = mGradientColors;
    }

    //获取渐变色数组
    private int[] getmGradientColors() {
        return mGradientColors;
    }

    /**
    * 绘制渐变色背景
    *
    * @param canvas 画布
    * @param alpha 透明值
    * */
    private void drawGradientBackground(Canvas canvas, float alpha) {
        if (mGrradientDrawable == null) {
            //设置渐变模式和颜色
            mGrradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    getmGradientColors());
            //规定背景宽高 一般为整屏
            Log.d(TAG, "mWidth" + mWidth + ",mHeight" + mHeight);
            mGrradientDrawable.setBounds(0, 0, mWidth, mHeight);
        }
        //开始draw
        mGrradientDrawable.setAlpha(Math.round(alpha * 255f));
        mGrradientDrawable.draw(canvas);
    }

    public void drawBgAndBubble(Canvas canvas, float alpha) {
        drawGradientBackground(canvas, alpha);
        drawBubbleCircle(canvas, alpha);
    }
}

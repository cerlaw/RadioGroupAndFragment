package com.example.zhanghongjie.radiogroupandfragment.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.LongDef;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * @author zhanghongjie
 * @date 2018/8/30
 * @descrition 用圆形浮动气泡填充的View
 */
public class FloatBubbleView extends SurfaceView implements SurfaceHolder.Callback {

    private final static String TAG = "Bubble";

    //绘制线程
    private DrawThread mDrawThread;
    //上一次绘制对象
    private BubbleDrawer mPreDrawer;
    //现在绘制对象
    private BubbleDrawer mCurDrawer;
    //屏幕宽高
    private int mWidth, mHeight;
    //当前透明度（范围0f-1f）
    private float curDrawerAlpha = 0f;


    public FloatBubbleView(Context context) {
        super(context);
        initThreadAndHolder(context);
    }

    public FloatBubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initThreadAndHolder(context);
    }

    public FloatBubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initThreadAndHolder(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FloatBubbleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initThreadAndHolder(Context context) {
        mDrawThread = new DrawThread();
        SurfaceHolder holder = getHolder();
        //添加回调
        holder.addCallback(this);
        //渐变效果，就是显示Surface的时候从暗到明
        holder.setFormat(PixelFormat.RGBA_8888);
        //开启绘制线程
        mDrawThread.start();
    }

    //当view的大小发生变化的时候触发
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        synchronized (mDrawThread) {
            mDrawThread.holder = holder;
            mDrawThread.notify();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroy");
        synchronized (mDrawThread) {
            mDrawThread.holder = holder;
            mDrawThread.mQuit = true;
            mDrawThread.mRunning = false;
            mDrawThread.notify();
            while (mDrawThread.mActive) {
                try {
                    mDrawThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        holder.removeCallback(this);
    }

    public void onDrawResume() {
        synchronized (mDrawThread) {
            //运行状态
            mDrawThread.mRunning = true;
            mDrawThread.notify();
        }
    }

    public void onDrawStop() {
        synchronized (mDrawThread) {
            //不运行状态
            mDrawThread.mRunning = false;
            mDrawThread.notify();
        }
    }

    public void onDrawDestroy() {
        synchronized (mDrawThread) {
            //退出状态
            Log.d(TAG, "onDrawDestroy");
            mDrawThread.mQuit = true;
            mDrawThread.notify();
        }
    }

    public void setDrawer(BubbleDrawer drawer) {
        if (drawer == null) {
            return;
        }

        //完全透明
        curDrawerAlpha = 0f;
        //如果当前有正在绘制的对象，直接设置为前一次绘制对象
        if (mCurDrawer != null) {
            mPreDrawer = mCurDrawer;
        }
        //当前绘制对象为设置的对象
        mCurDrawer = drawer;
    }

    private class DrawThread extends Thread {
        SurfaceHolder holder;
        boolean mRunning, mQuit, mActive;//三种状态
        Canvas canvas;

        @Override
        public void run() {
            while (true) {
                Log.d(TAG, "running");
                synchronized (this) {
                    //根据该函数的值判断是否返回 不进行绘制
                    Log.d(TAG, mQuit + "");
                    if (!processDrawThreadState()) {
                        return;
                    }
                    //动画开始时间
                    long startTime = AnimationUtils.currentAnimationTimeMillis();
                    //处理画布并进行绘制
                    processDrawCanvas(canvas);
                    //绘制时间
                    long drawTime = AnimationUtils.currentAnimationTimeMillis() - startTime;
                    Log.d(TAG, drawTime + " ");
                    //处理一下线程需要睡眠的时间
                    processDrawThreadSleep(drawTime);
                }
            }
        }

        /**
        * 处理绘制线程状态
        * @return true:不结束继续绘制；false:结束且不绘制
        * */
        boolean processDrawThreadState() {
            //处理surface为null（没有运行）或者Holder为null的情况
            if (holder == null) {
                Log.d(TAG, "holder is null");
            }
            Log.d(TAG, "running:" + mRunning);
            while(holder == null || !mRunning) {
                if (mActive) {
                    mActive = false;
                    notify();//唤醒
                }
                if (mQuit) {
                    return false;
                }
                try {
                    wait();//等待
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!mActive) {
                mActive = true;
                notify();
            }
            if (mQuit) {
                return false;
            }
            return true;
        }


        /**
        * 处理画布与绘制过程，注意要在同步锁中进行
        * */
        void processDrawCanvas(Canvas c) {
            Log.d(TAG, "processDrawCanvas");
            try {
                c = holder.lockCanvas();//画布加锁
                Log.d(TAG, "c is null");
                if (c != null) {
                    Log.d(TAG, "process");
                    //清屏操作
                    c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    //真正开始画SurfaceView的地方
                    drawSurface(c);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                //释放canvas锁，显示画图
                if (c != null){
                    holder.unlockCanvasAndPost(c);
                }
            }
        }

        /**
        * 真正绘制画布
        * */
        void drawSurface(Canvas canvas) {
            Log.d(TAG, "drawSurface");
            //防空保护
            if (mHeight == 0 || mWidth == 0) {
                return;
            }

            //如果前一次绘制对象不为空，且当前绘制者有透明效果的话，绘制前一次对象即可
            if (mPreDrawer != null && curDrawerAlpha < 1f) {
                Log.d(TAG, "situation 1");
                mPreDrawer.setScreenSize(mWidth, mHeight);
                mPreDrawer.drawBgAndBubble(canvas, 1f - curDrawerAlpha);
            }

            //直到绘制不透明的时候将上一次绘制的置空
            if (curDrawerAlpha < 1f) {
                Log.d(TAG, "situation 2");
                curDrawerAlpha += 0.5f;
                if (curDrawerAlpha >= 1f) {
                    curDrawerAlpha = 1f;
                    mPreDrawer = null;
                }
            }

            //如果当前有绘制对象直接绘制即可
            if (mCurDrawer != null) {
                Log.d(TAG, "situation 3");
                mCurDrawer.setScreenSize(mWidth, mHeight);
                mCurDrawer.drawBgAndBubble(canvas, curDrawerAlpha);
//                mPreDrawer = mCurDrawer;
            }
            Log.d(TAG, "situation not full fill");
        }

        /**
         * 处理线程需要的睡眠时间
         * View通过刷新来重绘视图，在一些需要频繁刷新或执行大量逻辑操作时，超过16ms就会导致明显卡顿
         *
         * @param drawTime 绘制时间
         */
        void processDrawThreadSleep(long drawTime) {
            //需要睡眠时间
            final long sleepTime = 16 - drawTime;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

package com.example.zhanghongjie.radiogroupandfragment;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class AlphaPageTransformer implements ViewPager.PageTransformer {
    private static final float DEFAULT_MIN_ALPHA = 0.5f;
    private float mMinAlpha = DEFAULT_MIN_ALPHA;

    @Override
    public void transformPage(@NonNull View view, float v) {
        int width = view.getWidth();
        if (v < -1) {
            //[-infinity,-1]
            view.setAlpha(mMinAlpha);
//            view.setScrollX((int) (width * 0.75 * -1));
        }else if (v < 1) {
            if (v < 0) {
                //[-1,0]区间
                float factor = mMinAlpha + (1 + v) * (1 - mMinAlpha);
                view.setAlpha(factor);
//                view.setScrollX((int) (width * 0.75 * v));
            }else {
                //[0,1]区间
                float factor = mMinAlpha + (1 - v) * (1 - mMinAlpha);
                view.setAlpha(factor);
//                view.setScrollX((int) (width * 0.75 * v));
            }
        }else {
            //[1,infinity]
            view.setAlpha(mMinAlpha);
//            view.setScrollX((int) (width * 0.75 * v));
        }
    }
}

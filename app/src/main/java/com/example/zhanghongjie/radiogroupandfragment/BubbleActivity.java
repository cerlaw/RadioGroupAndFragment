package com.example.zhanghongjie.radiogroupandfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.zhanghongjie.radiogroupandfragment.View.BubbleDrawer;
import com.example.zhanghongjie.radiogroupandfragment.View.FloatBubbleView;

/**
 * @author zhanghongjie
 * @date 2018/8/30
 * @descrition
 */
public class BubbleActivity extends AppCompatActivity {

    FloatBubbleView mFloatBubbleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bubbleactivity_layout);
//        setContentView(R.layout.blank_layuout);
        mFloatBubbleView = findViewById(R.id.floatbubbleview);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFloatBubbleView.onDrawResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFloatBubbleView.onDrawStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFloatBubbleView.onDrawDestroy();
    }

    private void initData() {
        //气泡绘制者
        BubbleDrawer drawer = new BubbleDrawer(this);
        drawer.setmGradientColors(new int[]{0xFFEFD09E, 0xFFD2D8B3});
        mFloatBubbleView.setDrawer(drawer);
    }
}

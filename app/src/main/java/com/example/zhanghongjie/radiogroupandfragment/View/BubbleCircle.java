package com.example.zhanghongjie.radiogroupandfragment.View;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * @author zhanghongjie
 * @date 2018/8/30
 * @descrition 圆形浮动气泡
 */
public class BubbleCircle {
    //圆心坐标
    private final float cx, cy;
    //圆心偏移量
    private final float dx, dy;
    //半径
    private final float radius;
    //画笔颜色
    private final int color;
    //每帧变化量
    private final float variationOfFrame;
    //标志位（判断左右移动）
    private boolean isGrowing = true;
    //当前帧变化量
    private float curVariationOfFrame = 0f;

    //构造函数
    public BubbleCircle(float cx, float cy, float dx, float dy, float radius, int color, float variationOfFrame) {
        this.cx = cx;
        this.cy = cy;
        this.dx = dx;
        this.dy = dy;
        this.radius = radius;
        this.color = color;
        this.variationOfFrame = variationOfFrame;
    }

    public void upDateAndDraw(Canvas canvas, Paint paint, float alpha) {
        /*
        * 每次绘制都是根据标志位（isGrowing）和每帧偏移量（variationOfFrame）进行更新
        * 说白了就是每帧都变化一段距离，连在一起就产生了动画效果
        * */
        if (isGrowing) {
            curVariationOfFrame += variationOfFrame;
            if (curVariationOfFrame > 1f) {
                curVariationOfFrame = 1f;
                isGrowing = false;
            }
        }else {
            curVariationOfFrame -= variationOfFrame;
            if (curVariationOfFrame < 0f) {
                curVariationOfFrame = 0f;
                isGrowing = true;
            }
        }

        //根据当前帧偏移量计算出当前圆心偏移后的位置
        float curCx = cx + dx * curVariationOfFrame;
        float curCy = cy + dy * curVariationOfFrame;
        //设置画笔
        int curColor = convertAlphaColor(alpha * (Color.alpha(color) / 255f), color);
        paint.setColor(curColor);
        canvas.drawCircle(curCx, curCy, radius, paint);
    }

    //转成透明颜色的方法
    private static int convertAlphaColor(float precent, int originalColor) {
        int newAlpha = (int) (precent * 255) & 0xFF;
        return (newAlpha << 24) | (originalColor & 0xFFFFFF);
    }
}

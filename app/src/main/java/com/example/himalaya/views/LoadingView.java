package com.example.himalaya.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.himalaya.R;

/**
 * @author JonesYang
 * @Data 2020-10-25
 * @Function 拉去数据时，不断加载
 */
@SuppressLint("AppCompatCustomView")
public class LoadingView extends ImageView {

    //旋转角度
    private int rotateDegree = 0;
    //是否旋转
    private boolean isNeedRotate = false;


    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.load);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isNeedRotate = true;
        post(new Runnable() {
            @Override
            public void run() {
                rotateDegree += 15;
                rotateDegree = rotateDegree <= 360 ? rotateDegree : 0;
                invalidate();
                if (isNeedRotate) {
                    postDelayed(this, 200);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isNeedRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * 1、旋转角度
         * 2、宽度
         * 3、高度
         * */
        canvas.rotate(rotateDegree, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);

    }
}

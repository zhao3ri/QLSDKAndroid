package com.qinglan.sdk.android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by zhaoj on 2018/10/13.
 */
public abstract class BaseSplashActivity extends Activity {
    private LinearLayout linearLayout;
    private Handler handler = new Handler();
    private static final long DEFAULT_SPLASH_MILLIS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(p);
        linearLayout.setBackgroundColor(Color.WHITE);
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.FILL;
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(getSplashDrawableId());
        linearLayout.addView(imageView);
        setContentView(linearLayout);
        if (isDelay()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSplashStop();
                }
            }, getSplashMills());
        } else {
            onSplashStop();
        }
    }

    /**
     * 闪屏结束时需要做的操作
     */
    public abstract void onSplashStop();

    /**
     * 闪屏图片的drawable Id
     */
    public abstract int getSplashDrawableId();

    /**
     * 闪屏显示时间，单位毫秒
     */
    public long getSplashMills() {
        return DEFAULT_SPLASH_MILLIS;
    }

    /**
     * 是否延时
     * */
    public boolean isDelay() {
        return true;
    }
}

package com.qinglan.sdk.android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qinglan.sdk.android.common.ResContainer;

/**
 * Created by zhaoj on 2018/10/13.
 */
public abstract class BaseSplashActivity extends Activity {
    private LinearLayout linearLayout;
    private ImageView imageView;
    private Handler handler = new Handler();
    private static final String DEFAULT_DRAWABLE_SPLASH_NAME = "bg_crash_screen";
    private static final long DEFAULT_SPLASH_MILLIS = 2 * 1000;
    protected static final int TYPE_IMAGE = 0;
    protected static final int TYPE_ANIM = 1;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(p);
        linearLayout.setBackgroundColor(Color.WHITE);
        imageView = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.FILL;
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        linearLayout.addView(imageView);
        setContentView(linearLayout);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            show();
        }
    }

    private void show() {
        if (getSplashType() == TYPE_IMAGE && getSplashDrawableId() != 0) {
            imageView.setImageResource(getSplashDrawableId());
        } else if (getSplashType() == TYPE_ANIM && getAnimation(imageView) != null) {
            if (!getAnimation(imageView).hasStarted())
                getAnimation(imageView).start();
        } else {
            onSplashStop();
            return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onSplashStop();
                if (getSplashType() == TYPE_ANIM) {
                    stopAnim();
                }
            }
        }, getSplashMills());
    }

    /**
     * 闪屏结束时需要做的操作
     */
    public abstract void onSplashStop();

    /**
     * 闪屏图片的drawable Id
     */
    public int getSplashDrawableId() {
        int drawableId = 0;
        try {
            drawableId = ResContainer.getResourceId(getApplicationContext(), "drawable", DEFAULT_DRAWABLE_SPLASH_NAME);
        } catch (Exception e) {

        }
        return drawableId;
    }

    /**
     * 闪屏显示时间，单位毫秒
     */
    public long getSplashMills() {
        return DEFAULT_SPLASH_MILLIS;
    }

    public int getSplashType() {
        return TYPE_IMAGE;
    }

    public Animation getAnimation(ImageView view) {
        return null;
    }

    private void stopAnim() {
        Animation anim = getAnimation(imageView);
        if (anim != null && anim.hasStarted()) {
            anim.cancel();
        }
    }
}

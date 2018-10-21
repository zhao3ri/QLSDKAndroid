package com.qinglan.sdk.android.demo;

import android.content.Intent;

import com.qinglan.sdk.android.BaseSplashActivity;


public class MySplashActivity extends BaseSplashActivity {
    @Override
    public void onSplashStop() {
        //当显示结束时，需要做的操作
        Intent intent = new Intent(this, WelComeActivity.class);
        startActivity(intent);
    }

    @Override
    public int getSplashDrawableId() {
        //闪屏图片drawable id
        return R.drawable.img_crash_screen;
    }

    @Override
    public long getSplashMills() {
        //闪屏显示的时间，单位为毫秒，默认为2000，可重写此方法，根据自己的需要修改
        return 3*1000;
    }
}
